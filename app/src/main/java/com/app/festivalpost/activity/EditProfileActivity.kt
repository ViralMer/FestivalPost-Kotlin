package com.app.festivalpost.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.models.UserItem
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import org.json.JSONObject
import java.lang.Exception

class EditProfileActivity : AppCompatActivity(), View.OnClickListener, ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var userItem: UserItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_edit_profile
        )
        apiManager = ApiManager(this@EditProfileActivity)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                userItem = bundle["object"] as UserItem?
            }
        }
        setActionbar()
        findViewById<View>(R.id.btnsubmit).setOnClickListener(this)
        if (userItem != null) {
            edname.setText(userItem!!.name)
            edemail.setText(userItem!!.email)
            edmobile.setText(userItem!!.mobile)
        }
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_edit_profile)
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

    private val edemail: EditText
        private get() = findViewById<View>(R.id.edemail) as EditText
    private val edname: EditText
        private get() = findViewById<View>(R.id.edname) as EditText
    private val edmobile: EditText
        private get() = findViewById<View>(R.id.edmobile) as EditText

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnsubmit -> performRegister()
        }
    }

    fun performRegister() {
        if (edname.text.toString() == "") {
            Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
        } else if (edemail.text.toString() != "" && !validEmail(edemail.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Enter valid e-mail!")
        } else if (edmobile.text.toString().length != 10) {
            Global.getAlertDialog(this, "Opps..!", "Please Enter 10 Digit Mobile Number")
        } else {
            Global.showProgressDialog(this@EditProfileActivity)
            apiManager!!.editmyprofile(
                ApiEndpoints.editmyprofile,
                Global.getPreference(Constant.PREF_TOKEN, ""),
                edname.text.toString().trim { it <= ' ' },
                edemail.text.toString().trim { it <= ' ' },
                edmobile.text.toString().trim { it <= ' ' })
        }
    }

    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@EditProfileActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@EditProfileActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@EditProfileActivity)
            if (requestService.equals(ApiEndpoints.editmyprofile, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        Global.showSuccessDialog(this@EditProfileActivity, message)
                        onBackPressed()
                    } else {
                        Global.getAlertDialog(this@EditProfileActivity, "Opps..!", message)
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
            Global.dismissProgressDialog(this@EditProfileActivity)
            Global.showFailDialog(this@EditProfileActivity, responseString)
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