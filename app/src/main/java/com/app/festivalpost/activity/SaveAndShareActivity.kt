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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SaveAndShareActivity() : AppCompatActivity(), ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var ivimage: ImageView? = null
    var bmp: Bitmap? = null
    var saveImage = 0
    var shareImage = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_save_and_share)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        apiManager = ApiManager(this@SaveAndShareActivity)
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
        }
        val businessItem = Global.currentBusiness
        setActionbar()
        ivimage = findViewById(R.id.ivimage)
        if (bmp != null) {
            ivimage!!.setImageBitmap(bmp)
        }
        findViewById<View>(R.id.btnsubmit).setOnClickListener(
            View.OnClickListener {
                sharePhoto = false
                if (!Global.getPreference(Constant.PREF_PREMIUM, false)) {
                    val intent = Intent(this@SaveAndShareActivity, PremiumActivity::class.java)
                    intent.putExtra("businessdetails", businessItem)
                    intent.putExtra("saveImage", 0)
                    intent.putExtra("premium", 1)
                    Global.storePreference("premium_data", 2)
                    startActivity(intent)
                } else {
                    savePhoto()
                    Global.storePreference("premium_data", 0)
                }
            })
        findViewById<View>(R.id.btnshare).setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                sharePhoto = true
                if (!Global.getPreference(Constant.PREF_PREMIUM, false)) {
                    val intent = Intent(this@SaveAndShareActivity, PremiumActivity::class.java)
                    intent.putExtra("businessdetails", businessItem)
                    intent.putExtra("saveImage", 1)
                    intent.putExtra("premium", 1)
                    Global.storePreference("premium_data", 3)
                    startActivity(intent)
                } else {
                    savePhoto()
                    Global.storePreference("premium_data", 0)
                }
            }
        })
    }

    var sharePhoto = false
    var imagePath: String? = ""
    fun savePhoto() {
        Dexter.withActivity(this@SaveAndShareActivity)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        imagePath = ""
                        if (bmp != null) {
                            imagePath = SaveImage(bmp!!)
                            Log.d("Stored", imagePath!!)
                            if (imagePath != null && !imagePath.equals("", ignoreCase = true)) {
                                SavePhotoAsync(imagePath!!).execute()
                            }
                        }
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
                                    + filePath)
                        )
                    )
                )
            } else {
                sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse(
                            ("file://"
                                    + filePath)
                        )
                    )
                )
            }

//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(
                        "file://$filePath"
                    )
                )
            )
            MediaScannerConnection.scanFile(
                this,
                arrayOf(file.toString()),
                arrayOf(file.name),
                null
            )
            MediaScannerConnection.scanFile(
                this,
                arrayOf(file.toString()),
                arrayOf("image/*"),
                null
            )
            scanner(filePath)
            Global.storePreference("premium_data", 0)
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

    internal inner class SavePhotoAsync(image: String) : AsyncTask<Void?, Void?, Void?>() {
        private var image = ""
        override fun onPreExecute() {
            super.onPreExecute()
            Global.showProgressDialog(this@SaveAndShareActivity)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            val businessItem = Global.currentBusiness
            apiManager!!.savephotos(ApiEndpoints.savephotos, image, businessItem.busiId.toString())
            return null
        }

        init {
            this.image = image
        }
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                Global.dismissProgressDialog(this@SaveAndShareActivity)
                if (!isConnected) {
                    Global.noInternetConnectionDialog(this@SaveAndShareActivity)
                }
            }
        })
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                Global.dismissProgressDialog(this@SaveAndShareActivity)
                if (requestService.equals(ApiEndpoints.savephotos, ignoreCase = true)) {
                    try {
                        Log.d("response", (responseString)!!)
                        processResponse(responseString)
                        if (status) {
                            Global.showSuccessDialog(this@SaveAndShareActivity, message)
                            val detailAct =
                                Intent(this@SaveAndShareActivity, HomeActivity::class.java)
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
                                    if ((Global.getPreference(
                                            Constant.PREF_SHARE_MESSAGE,
                                            ""
                                        ) == "") && Global.getPreference(
                                            Constant.PREF_SHARE_MESSAGE, ""
                                        ) != null
                                    ) {
                                        share.putExtra(
                                            Intent.EXTRA_TEXT, Global.getPreference(
                                                Constant.PREF_SHARE_MESSAGE, ""
                                            )
                                        )
                                    }
                                    startActivity(Intent.createChooser(share, "Share Design!"))
                                }
                            }
                        } else {
                            Global.showFailDialog(this@SaveAndShareActivity, message)
                        }
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                Global.dismissProgressDialog(this@SaveAndShareActivity)
                Global.showFailDialog(this@SaveAndShareActivity, responseString)
            }
        })
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        val data = Global.getPreference("premium_data", 0)
        if (data == 2) {
            sharePhoto = false
            savePhoto()
        } else if (data == 3) {
            sharePhoto = true
            savePhoto()
        }
    }

    private fun scanner(path: String) {
        MediaScannerConnection.scanFile(this@SaveAndShareActivity, arrayOf(path), null,
            object : MediaScannerConnection.OnScanCompletedListener {
                override fun onScanCompleted(path: String, uri: Uri) {
                    Log.i("TAG", "Finished scanning $path")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse(
                        ("file://"
                                + Environment.getExternalStorageDirectory())
                    )
                )
            )
        } else {
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse(
                        ("file://"
                                + Environment.getExternalStorageDirectory())
                    )
                )
            )
        }
    }
}