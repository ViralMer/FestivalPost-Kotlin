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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.github.dhaval2404.imagepicker.ImagePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.BusinessItem
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.adapter.BusinessCategoryItemAdapter
import com.app.festivalpost.models.BusinessCategory
import com.app.festivalpost.models.PhotoItem
import com.emegamart.lelys.utils.extensions.onClick
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.json.JSONObject


class AddBusinessActivity : AppCompatActivity(), View.OnClickListener, ApiResponseListener,OnItemClickListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var businessItem: BusinessItem? = null
    var ivlogo: ImageView? = null
    var profilePath = ""
    var edName:AppCompatEditText?=null
    var edEmail:AppCompatEditText?=null
    var edPhone:AppCompatEditText?=null
    var edAddress:AppCompatEditText?=null
    var edWebsite:AppCompatEditText?=null
    var edPhone2:AppCompatEditText?=null
    var edCategory:TextInputEditText?=null
    var frameChosse:FrameLayout?=null
    var category_value:String?=null
    var btn_next:TextView?=null
    var businessCategoryItemAdapter:BusinessCategoryItemAdapter?=null
    var businessCategoryList= arrayListOf<BusinessCategory?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                businessItem = bundle["object"] as BusinessItem?
            }
        }
        apiManager = ApiManager(this@AddBusinessActivity)
        setActionbar()
        findViewById<View>(R.id.btn_next).setOnClickListener(this)
        ivlogo = findViewById(R.id.ivlogo)


        edName=findViewById(R.id.et_business_name)
        edEmail=findViewById(R.id.et_business_email)
        edPhone=findViewById(R.id.et_number_one)
        edPhone2=findViewById(R.id.et_number_two)
        edAddress=findViewById(R.id.et_business_address)
        edWebsite=findViewById(R.id.et_business_website)
        edCategory=findViewById(R.id.etCategory)
        frameChosse=findViewById(R.id.framePhoto)
        btn_next=findViewById(R.id.btn_next)

        frameChosse!!.setOnClickListener({ openAddImageDialog() })
        if (businessItem != null) {
            edName!!.setText(businessItem!!.busiName)
            edAddress!!.setText(businessItem!!.busiAddress)
            if (!businessItem!!.busiWebsite.equals("")) {
                edWebsite!!.setText(businessItem!!.busiWebsite)
            }
            edPhone!!.setText(businessItem!!.busiMobile)
            edEmail!!.setText(businessItem!!.busiEmail)
            if (!businessItem!!.busiLogo.equals("")) {
                Glide.with(this@AddBusinessActivity).load(businessItem!!.busiLogo)
                    .into(ivlogo!!)
            }
        }

        btn_next!!.onClick {
            submitDetails()
        }
        businessCategoryList.add(BusinessCategory("IT"))
        businessCategoryList.add(BusinessCategory("IT"))
        businessCategoryList.add(BusinessCategory("IT"))
        businessCategoryList.add(BusinessCategory("IT"))
        businessCategoryList.add(BusinessCategory("IT"))
        businessCategoryList.add(BusinessCategory("IT"))
        businessCategoryList.add(BusinessCategory("IT"))
        category_value= businessCategoryList[0]!!.name
        businessCategoryItemAdapter= BusinessCategoryItemAdapter(this,businessCategoryList)
    }

    fun openAddImageDialog() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        ImagePicker.Companion.with(this@AddBusinessActivity)
                                    .galleryOnly()
                                    .crop()
                                    .start()


                    } else {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
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
        val builder = AlertDialog.Builder(this@AddBusinessActivity)
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
                val realpath: String = ImagePicker.Companion.getFilePath(imagereturnintent)!!
                profilePath = realpath
                Log.d("Real Path : ", "" + realpath)
            } else if (resultcode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(
                    this,
                    "" + ImagePicker.Companion.getError(imagereturnintent),
                    Toast.LENGTH_SHORT
                ).show()
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
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as AppCompatImageView
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        if (businessItem == null) {
            tvtitle.text = resources.getString(R.string.txt_add_business)
            tvaction.visibility = View.INVISIBLE
        } else {
            tvtitle.text = resources.getString(R.string.txt_edit_business)
            tvaction.visibility = View.VISIBLE
            tvaction.text = resources.getString(R.string.txt_delete)
        }

        ivBack.onClick {
            onBackPressed()
        }
        tvaction.setOnClickListener {
            val builder: AlertDialog.Builder
            builder = AlertDialog.Builder(this@AddBusinessActivity)
            builder.setTitle(resources.getString(R.string.txt_delete_title))
                .setMessage(resources.getString(R.string.txt_delete_message))
                .setPositiveButton(resources.getString(R.string.txt_yes)) { dialog, which ->
                    Global.showProgressDialog(this@AddBusinessActivity)
                    apiManager!!.removemybusiness(
                        ApiEndpoints.removemybusiness,
                        businessItem!!.busiId.toString() + ""
                    )
                }
                .setNegativeButton(resources.getString(R.string.txt_no)) { dialog, which -> dialog.dismiss() }
                .show()
        }
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_next -> submitDetails()
        }
    }

    private fun submitDetails() {
        if (edName!!.text.toString() == "") {
            Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
        } else if (edEmail!!.text.toString() != "" && !validEmail(edEmail!!.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Enter valid e-mail!")
        } else if (edPhone!!.text.toString().length != 10) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter 10 Digit Mobile Number")
        }
        else if (edPhone2!!.text.toString().length != 10) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter 10 Digit Mobile Number")
        }
        else if (edWebsite!!.text.toString() != "" && !validWebsite(edWebsite!!.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter valid Website address")
        } else {
            if (businessItem == null) {
                AddBusinessAsync(
                    edName!!.text.toString(),
                    edEmail!!.text.toString(),
                    edPhone!!.text.toString(),
                    edPhone2!!.text.toString(),
                    edWebsite!!.text.toString(),
                    edAddress!!.text.toString(),
                    category_value!!,
                    profilePath
                )
            } else {
                UpdateBusinessAsync(
                    businessItem!!.busiId.toString() + "",
                    edName!!.text.toString(),
                    edEmail!!.text.toString(),
                    edPhone!!.text.toString(),
                    edPhone2!!.text.toString(),
                    edWebsite!!.text.toString(),
                    edAddress!!.text.toString(),
                    category_value!!,
                    profilePath
                )
            }
        }
    }

    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return if (!email.isEmpty()) {
            pattern.matcher(email).matches()
        } else {
            false
        }
    }

    private fun validWebsite(website: String): Boolean {
        val pattern = Patterns.WEB_URL
        return if (!website.isEmpty()) {
            pattern.matcher(website).matches()
        } else {
            false
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class AddBusinessAsync(
        name: String,
        email: String,
        mobile: String,
        mobile2: String,
        website: String,
        address: String,
        category: String,
        profilePath: String
    ) : AsyncTask<Void?, Void?, Void?>() {
        private var name = ""
        private var email = ""
        private var mobile = ""
        private var mobile2 = ""
        private var website = ""
        private var address = ""
        private var category = ""
        private var profilePath = ""
        override fun onPreExecute() {
            super.onPreExecute()
            Global.showProgressDialog(this@AddBusinessActivity)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            apiManager!!.addbusiness(
                ApiEndpoints.addbusiness, name, email, mobile,mobile2,website, address,category ,profilePath
            )
            return null
        }

        init {
            this.name = name
            this.email = email
            this.mobile = mobile
            this.mobile2 = mobile2
            this.website = website
            this.address = address
            this.category = category
            this.profilePath = profilePath
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class UpdateBusinessAsync(
        id: String,
        name: String,
        email: String,
        mobile: String,
        mobile2: String,
        website: String,
        address: String,
        category: String,
        profilePath: String
    ) : AsyncTask<Void?, Void?, Void?>() {
        private var id = ""
        private var name = ""
        private var email = ""
        private var mobile = ""
        private var mobile2 = ""
        private var website = ""
        private var address = ""
        private var category = ""
        private var profilePath = ""
        override fun onPreExecute() {
            super.onPreExecute()
            Global.showProgressDialog(this@AddBusinessActivity)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            apiManager!!.updatebusiness(
                ApiEndpoints.updatebusiness, id, name, email, mobile,mobile2, website, address,category, profilePath
            )
            return null
        }

        init {
            this.id = id
            this.name = name
            this.email = email
            this.mobile = mobile
            this.mobile2 = mobile2
            this.website = website
            this.address = address
            this.category = category
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

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@AddBusinessActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@AddBusinessActivity)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@AddBusinessActivity)
            if (requestService.equals(ApiEndpoints.addbusiness, ignoreCase = true) ||
                requestService.equals(ApiEndpoints.removemybusiness, ignoreCase = true)
            ) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        Global.showSuccessDialog(this@AddBusinessActivity, message)
                        onBackPressed()
                    } else {
                        Global.showFailDialog(this@AddBusinessActivity, message)
                    }
                } catch (e: Exception) {
                }
            } else if (requestService.equals(ApiEndpoints.updatebusiness, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        val materialAlertDialogBuilder =
                            AlertDialog.Builder(this@AddBusinessActivity)
                        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val view = inflater.inflate(R.layout.custom_error_dialog, null)
                        val tvTitle: TextView
                        val tvMessage: TextView
                        val btnOk: Button
                        tvTitle = view.findViewById(R.id.tvTitle)
                        tvMessage = view.findViewById(R.id.tvMessage)
                        btnOk = view.findViewById(R.id.btnOk)
                        btnOk.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                        btnOk.setTextColor(resources.getColor(R.color.colorBlack))
                        if (message == "Business Information Update") {
                            tvTitle.text = "Approval Done"
                            tvMessage.text = message
                        } else {
                            tvTitle.text = resources.getString(R.string.under_approval)
                            tvMessage.text = message
                        }
                        materialAlertDialogBuilder.setView(view).setCancelable(true)
                        val b = materialAlertDialogBuilder.create()
                        btnOk.setOnClickListener {
                            b.dismiss()
                            onBackPressed()
                        }
                        b.show()
                        //Global.getAlertDialog(AddBusinessActivity.this,"Business Approval", message);
                    } else {
                        Global.showFailDialog(this@AddBusinessActivity, message)
                    }
                } catch (e: Exception) {
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
            Global.dismissProgressDialog(this@AddBusinessActivity)
            Global.showFailDialog(this@AddBusinessActivity, responseString)
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

    override fun onItemClicked(`object`: Any?, index: Int) {
        val photoItem = `object` as BusinessCategory
        category_value = photoItem.name

    }
}