@file:Suppress("DEPRECATION")

package com.app.festivalpost.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.festivalpost.HomeActivity
import com.app.festivalpost.festivalpost.R
import com.app.festivalpost.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.festivalpost.globals.Global
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.getError
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.getFilePath
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.with
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.json.JSONObject

class AddFirstBusinessActivity : AppCompatActivity(), View.OnClickListener, ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var ivlogo: ImageView? = null
    var profilePath: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business)
        apiManager = ApiManager(this@AddFirstBusinessActivity)
        setActionbar()
        findViewById<View>(R.id.btnsubmit).setOnClickListener(this)
        ivlogo = findViewById(R.id.ivlogo)
        ivlogo!!.setOnClickListener { openAddImageDialog() }
    }

    fun openAddImageDialog() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val builder = AlertDialog.Builder(this@AddFirstBusinessActivity)
                        builder.setTitle("Choose")
                        val animals = arrayOf("Camera", "Gallery")
                        builder.setItems(animals) { dialog, which ->
                            when (which) {
                                0 -> with(this@AddFirstBusinessActivity)
                                    .cameraOnly()
                                    .crop()
                                    .start()
                                1 -> with(this@AddFirstBusinessActivity)
                                    .galleryOnly()
                                    .crop()
                                    .start()
                            }
                        }
                        val dialog = builder.create()
                        dialog.show()
                    } else {
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
        val builder = AlertDialog.Builder(this@AddFirstBusinessActivity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, _ ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(
        requestcode: Int, resultcode: Int,
        imagereturnintent: Intent?
    ) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent)
        try {
            if (resultcode == RESULT_OK) {
                val imageuri = imagereturnintent!!.data // Get intent
                // data
                Log.d("URI Path : ", "" + imageuri.toString())
                ivlogo!!.setImageURI(imageuri)
                val realpath = getFilePath(imagereturnintent)
                profilePath = realpath
                Log.d("Real Path : ", "" + realpath)
            } else if (resultcode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, "" + getError(imagereturnintent), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Image Cancelled", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
        }
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvtitle.text = resources.getString(R.string.txt_add_business)
        tvaction.visibility = View.INVISIBLE
    }

    private val edname: EditText
         get() = findViewById<View>(R.id.edname) as EditText
    private val edemail: EditText
         get() = findViewById<View>(R.id.edemail) as EditText
    private val edphone: EditText
         get() = findViewById<View>(R.id.edphone) as EditText
    private val edwebsite: EditText
         get() = findViewById<View>(R.id.edwebsite) as EditText
    private val edaddress: EditText
         get() = findViewById<View>(R.id.edaddress) as EditText

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnsubmit -> submitDetails()
        }
    }

    private fun submitDetails() {
        if (edname.text.toString() == "") {
            Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
        } else if (edemail.text.toString() != "" && !validEmail(edemail.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Enter valid e-mail!")
        } else if (edphone.text.toString().length != 10) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter 10 Digit Mobile Number")
        } else if (edwebsite.text.toString() != "" && !validWebsite(edwebsite.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter valid Website address")
        } /*else if (getEdaddress().getText().toString().equals("")) {
            //Toast.makeText(AddFirstBusinessActivity.this, "Enter valid Address!", Toast.LENGTH_LONG).show();
            Global.getAlertDialog(this, "Opps..!", "Enter valid Address!");

        }*/ else {
            AddBusinessAsync(
                edname.text.toString(),
                edemail.text.toString(),
                edphone.text.toString(),
                edwebsite.text.toString(),
                edaddress.text.toString(),
                profilePath!!
            )
        }
    }

    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun validWebsite(website: String): Boolean {
        val pattern = Patterns.WEB_URL
        return pattern.matcher(website).matches()
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class AddBusinessAsync(
        name: String,
        email: String,
        mobile: String,
        website: String,
        address: String,
        profilePath: String
    ) : AsyncTask<Void?, Void?, Void?>() {
        private var name = ""
        private var email = ""
        private var mobile = ""
        private var website = ""
        private var address = ""
        private var profilePath = ""
        override fun onPreExecute() {
            super.onPreExecute()
            Global.showProgressDialog(this@AddFirstBusinessActivity)
        }

        override fun doInBackground(vararg po: Void?): Void? {
            apiManager!!.addbusiness(
                ApiEndpoints.addbusiness, name, email, mobile, website, address, profilePath
            )
            return null
        }

        init {
            this.name = name
            this.email = email
            this.mobile = mobile
            this.website = website
            this.address = address
            this.profilePath = profilePath
        }
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

    override fun isConnected(requestService: String, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@AddFirstBusinessActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@AddFirstBusinessActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String,
        responseString: String,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@AddFirstBusinessActivity)
            if (requestService.equals(ApiEndpoints.addbusiness, ignoreCase = true)) {
                try {
                    Log.d("response", responseString)
                    processResponse(responseString)
                    if (status) {
                        Global.showSuccessDialog(this@AddFirstBusinessActivity, message)
                        val detailAct =
                            Intent(this@AddFirstBusinessActivity, HomeActivity::class.java)
                        startActivity(detailAct)
                        finish()
                    } else {
                        Global.showFailDialog(this@AddFirstBusinessActivity, message)
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun onErrorResponse(
        requestService: String,
        responseString: String,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@AddFirstBusinessActivity)
            Global.showFailDialog(this@AddFirstBusinessActivity, responseString)
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}