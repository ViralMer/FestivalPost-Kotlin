@file:Suppress("DEPRECATION")

package com.app.festivalpost.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.adapter.BusinessCategoryItemAdapter

import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.BusinessCategory
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.onClick
import com.emegamart.lelys.utils.extensions.toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_account.*
import org.json.JSONObject


class AddBusinessActivity : AppBaseActivity(),OnItemClickListener,ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = true
    var message = ""
    var businessItem: CurrentBusinessItem? = null
    var ivlogo: ImageView? = null
    var profilePath = ""
    var edName:AppCompatEditText?=null
    var edEmail:AppCompatEditText?=null
    var edPhone:AppCompatEditText?=null
    var edAddress:AppCompatEditText?=null
    var edWebsite:AppCompatEditText?=null
    var edPhone2:AppCompatEditText?=null
    var edCategory:TextView?=null
    var frameChosse:FrameLayout?=null
    var category_value:String?=null
    var btn_next:TextView?=null
    var businessCategoryItemAdapter:BusinessCategoryItemAdapter?=null
    var businessCategoryList= arrayListOf<BusinessCategory?>()
    var rcvCategory: RecyclerView?=null
    var alertDialog: AlertDialog?=null
    var sessionManager: SessionManager?=null
    var token: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_business)
        sessionManager= SessionManager(this)
        token=sessionManager!!.getValueString(USER_TOKEN)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                businessItem = bundle["object"] as CurrentBusinessItem?
            }
        }


        setActionbar()
        apiManager= ApiManager(this)
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

        edCategory!!.onClick {
            loadBusinessCategoryData()
        }

        frameChosse!!.setOnClickListener({ openAddImageDialog() })
        if (businessItem != null) {
            edName!!.setText(businessItem!!.busi_name)
            edAddress!!.setText(businessItem!!.busi_address)
            if (!businessItem!!.busi_website.equals("")) {
                edWebsite!!.setText(businessItem!!.busi_website)
            }
            edPhone!!.setText(businessItem!!.busi_mobile)
            edPhone2!!.setText(businessItem!!.busi_mobile_second)
            edEmail!!.setText(businessItem!!.busi_email)
            category_value=businessItem!!.business_category
            edCategory!!.setText(businessItem!!.business_category)
            if (!businessItem!!.busi_logo.equals("")) {
                Glide.with(this).load(businessItem!!.busi_logo)
                    .into(ivlogo!!)
            }
        }

        btn_next!!.onClick {
            submitDetails()
            Log.d("Clicked", "Cliked")
        }

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
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as AppCompatImageView
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        if (businessItem == null) {
            tvtitle.text = resources.getString(R.string.txt_add_business)
        } else {
            tvtitle.text = resources.getString(R.string.txt_edit_business)
        }

        ivBack.onClick {
            onBackPressed()
        }

    }



    private fun submitDetails() {
        if (edName!!.text.toString() == "") {
            Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
        } /*else if (edEmail!!.text.toString() != "" && !validEmail(edEmail!!.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Enter valid e-mail!")
        } else if (edPhone!!.text.toString().length != 10) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter 10 Digit Mobile Number")
        }
        else if (edWebsite!!.text.toString() != "" && !validWebsite(edWebsite!!.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter valid Website address")
        }*/
        else if (edPhone!!.text.toString().length > 15) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter Mobile Number")
        }
        else if (category_value==null) {
            Global.getAlertDialog(this, "Opps..!", "Please Select Category")
        }
        else {
            Log.d("Business0", "" + businessItem)
            if (businessItem == null) {
                Log.d("Business01", "" + businessItem)
                AddBusinessAsync(
                    edName!!.text.toString(),
                    edEmail!!.text.toString(),
                    edPhone!!.text.toString(),
                    edPhone2!!.text.toString(),
                    edWebsite!!.text.toString(),
                    edAddress!!.text.toString(),
                    category_value!!,
                    profilePath
                ).execute()
            } else {
                UpdateBusinessAsync(
                    businessItem!!.busi_id.toString() + "",
                    edName!!.text.toString(),
                    edEmail!!.text.toString(),
                    edPhone!!.text.toString(),
                    edPhone2!!.text.toString(),
                    edWebsite!!.text.toString(),
                    edAddress!!.text.toString(),
                    category_value!!,
                    profilePath
                ).execute()
                Log.d("Business012", "" + businessItem)
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
            Log.d("Start", "Start Api")
            showProgress(true)
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            Log.d("Start", "Start Api2")
            apiManager!!.addbusiness(
                ApiEndpoints.addbusiness,
                name,
                email,
                mobile,
                mobile2,
                website,
                address,
                category,
                profilePath,token!!
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
                ApiEndpoints.updatebusiness,
                id,
                name,
                email,
                mobile,
                mobile2,
                website,
                address,
                category,
                profilePath,token!!
            )
            Log.d("mobile2",""+edPhone2!!.text.toString())

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


    @SuppressLint("SetTextI18n", "ResourceAsColor")
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
                            tvTitle.setTextColor(R.color.black)
                            tvMessage.text = message
                            tvTitle.setTextColor(R.color.black)
                        } else {
                            tvTitle.text = resources.getString(R.string.under_approval)
                            tvMessage.text = message
                            tvTitle.setTextColor(R.color.black)
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


    override fun onItemClicked(`object`: Any?, index: Int) {
        val photoItem = `object` as BusinessCategory
        Log.d("Item Cliked",""+index);
        category_value = photoItem.category_name
        edCategory!!.text=category_value
        alertDialog!!.dismiss()


    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {

        showProgress(false)
    }


    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        showProgress(false)
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

    private fun loadBusinessCategoryData()
    {
        showProgress(true)
        callApi(
            getRestApis().getAllBusinessCategory(token!!), onApiSuccess = {
                showProgress(false)
                if (it.status!!) {

                    showPopupBusinessCategoryDialog(this)
                    businessCategoryList = it.cateogry
                    businessCategoryItemAdapter =
                        BusinessCategoryItemAdapter(this, businessCategoryList)
                    rcvCategory!!.adapter = businessCategoryItemAdapter
                }
                else{
                    toast("Something Went Wrong")
                }


            }, onApiError = {
                showProgress(false)
                toast("Something Went Wrong")

            }, onNetworkError = {
                showProgress(false)
                toast("Something Went Wrong")

            })
    }

    private fun showPopupBusinessCategoryDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_business_category, null)
        rcvCategory = layout.findViewById<View>(R.id.rcvBusinessCategory) as RecyclerView
        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(true)
        alertDialog = builder.create()
        if (!isFinishing) {
            alertDialog!!.show()
        }


    }


}