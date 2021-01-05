package com.app.festivalpost.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.Constants.SharedPref.KEY_CURRENT_BUSINESS
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.app.festivalpost.utils.SessionManager
import com.emegamart.lelys.utils.extensions.get
import com.emegamart.lelys.utils.extensions.onClick
import com.emegamart.lelys.utils.extensions.toast
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*


class SaveAndShareActivity() : AppBaseActivity(), ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var ivimage: ImageView? = null
    var bmp: Bitmap? = null
    var image_type: String? = null
    var sessionManager: SessionManager? = null
    var banner_container: LinearLayout? = null
    private var adView: AdView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_save_and_share)
        sessionManager = SessionManager(this)
        apiManager = ApiManager(this@SaveAndShareActivity)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        banner_container = findViewById(R.id.banner_container)
        adView = AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50)


        // Find the Ad Container
        // Add the ad view to your activity layout
        if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!) {
            banner_container!!.addView(adView)
            adView!!.loadAd()
        }

        // Request an ad

        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("image")) {
                val filename = intent.getStringExtra("image")
                try {
                    val `is` = openFileInput(filename)
                    bmp = BitmapFactory.decodeStream(`is`)
                    `is`.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (bundle.containsKey("image_type"))
            {
                try {
                    image_type = intent.getStringExtra("image_type")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        val businessItem = get<CurrentBusinessItem>(KEY_CURRENT_BUSINESS, this)
        if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!
        ) {
            if (image_type != "0") {
                AlertDialog.Builder(this)
                    .setTitle("Sorry!!")
                    .setMessage("Buy Premium plan and remove watermark")
                    .setPositiveButton("Buy Premium") { dialog, which ->
                        val intent = Intent(
                            this,
                            PremiumActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        setActionbar()
        ivimage = findViewById(R.id.ivimage)
        if (bmp != null) {
            ivimage!!.setImageBitmap(bmp)
        }
        findViewById<View>(R.id.btnsubmit).setOnClickListener(
            View.OnClickListener {
                sharePhoto = false

                savePhoto()

            })
        val btnshare=findViewById<View>(R.id.btnshare)
        btnshare.onClick {

                sharePhoto = true
                savePhoto()


        }
    }



    var sharePhoto = false
    var imagePath: String? = ""
    private fun savePhoto() {
        Dexter.withContext(this@SaveAndShareActivity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        imagePath = ""
                        if (bmp != null) {
                            imagePath = SaveImage(bmp!!)
                            if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!) {
                                SavePhotoAsync(imagePath!!).execute()
                                Log.d("ImageCalled", "" + imagePath)
                            } else {
                                if (sharePhoto) {
                                    if (imagePath != null && !imagePath.equals(
                                            "",
                                            ignoreCase = true
                                        )
                                    ) {
                                        val uri = Uri.parse(imagePath)
                                        val share = Intent(Intent.ACTION_SEND)
                                        share.type = "image/*"
                                        share.putExtra(Intent.EXTRA_STREAM, uri)
                                        startActivity(Intent.createChooser(share, "Share Design!"))
                                        scanner(imagePath!!)

                                    }

                                } else {
                                    val detailAct =
                                        Intent(this@SaveAndShareActivity, HomeActivity::class.java)
                                    detailAct.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(detailAct)
                                    scanner(imagePath!!)
                                    finish()
                                }
                            }

                            toast("Image Saved Successfully")
                        }
                        /*}*/
                    } else {
                        Toast.makeText(
                            this@SaveAndShareActivity,
                            "Please grant permission to save image",
                            Toast.LENGTH_SHORT
                        ).show()
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@SaveAndShareActivity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.cancel()
                openSettings()
            }
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.cancel()
            }
        })
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun SaveImage(finalBitmap: Bitmap): String {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File(root + "/" + Constant.FOLDER_NAME)
        myDir.mkdirs()
        myDir.mkdir()
        val fname = "Image-" + Random().nextInt() + ".jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            val filePath = file.absolutePath
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse(
                            ("file://"
                                    + file.absolutePath)
                        )
                    )
                )
            } else {
                sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse(
                            ("file://"
                                    + file.absolutePath)
                        )
                    )
                )
            }
            MediaScannerConnection.scanFile(
                this,
                arrayOf(file.absolutePath),
                arrayOf("image/*"),
                null
            )



            scanner(file.absolutePath)


            return filePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_save_and_share)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }





    override fun onResume() {
        super.onResume()

    }

    private fun scanner(path: String) {
        MediaScannerConnection.scanFile(
            this@SaveAndShareActivity, arrayOf(path), null
        ) { path, uri -> Log.i("TAG", "Finished scanning $path") }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse(
                        ("file://"
                                + Environment.getExternalStorageDirectory() + "/FestivalPost")
                    )
                )
            )
        } else {
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse(
                        ("file://"
                                + Environment.getExternalStorageDirectory() + "/FestivalPost")
                    )
                )
            )
        }
    }

    internal inner class SavePhotoAsync(image: String) :
        AsyncTask<Void?, Void?, Void?>() {
        private var image = ""
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)
        }

        override fun doInBackground(vararg voids: Void?): Void? {

            val businessItem = com.emegamart.lelys.utils.extensions.get<CurrentBusinessItem>(
                KEY_CURRENT_BUSINESS,
                this@SaveAndShareActivity
            )
        apiManager!!.savephotos(
            ApiEndpoints.savephotos,
            image,
            businessItem!!.busi_id.toString(),
            sessionManager!!.getValueString(USER_TOKEN)
        )
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            showProgress(false)
        }

        init {
            this.image = image
        }
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@SaveAndShareActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@SaveAndShareActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@SaveAndShareActivity)
            if (requestService.equals(ApiEndpoints.savephotos, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        Global.showSuccessDialog(this@SaveAndShareActivity, message)
                        val detailAct = Intent(
                            this@SaveAndShareActivity,
                            HomeActivity::class.java
                        )
                        detailAct.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(detailAct)
                        finish()
                        if (sharePhoto) {
                            if (imagePath != null && !imagePath.equals("", ignoreCase = true)) {
                                val uri = Uri.parse(imagePath)
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/*"
                                share.putExtra(Intent.EXTRA_STREAM, uri)
                                startActivity(
                                    Intent.createChooser(
                                        share,
                                        "Share Design!"
                                    )
                                )
                            }
                        }
                    } else {
                        Global.showFailDialog(this@SaveAndShareActivity, message)
                    }
                } catch (e: java.lang.Exception) {
                }
            }
        }
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@SaveAndShareActivity)
            Global.showFailDialog(this@SaveAndShareActivity, responseString)
        }
    }

    fun processResponse(responseString: String?) {
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


}