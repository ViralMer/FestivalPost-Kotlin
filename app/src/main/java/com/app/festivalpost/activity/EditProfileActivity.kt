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
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.models.UserItem
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.Constants.SharedPref.USER_EMAIL
import com.app.festivalpost.utils.Constants.SharedPref.USER_NAME
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.json.JSONObject
import java.lang.Exception

class EditProfileActivity : AppBaseActivity() {

    var userItem: UserItem? = null
    var sessionManager: SessionManager? = null

    private var etName:AppCompatEditText?=null
    private var etEmail:AppCompatEditText?=null
    private var btn_save:TextView?=null
    var token : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_edit_profile
        )

        sessionManager=SessionManager(this)
        token=sessionManager!!.getValueString(USER_TOKEN)
        setActionbar()
        etEmail=findViewById(R.id.etEmail)
        etName=findViewById(R.id.etName)
        btn_save=findViewById(R.id.btn_save)
        val email=sessionManager!!.getValueString(USER_EMAIL)
        val name=sessionManager!!.getValueString(USER_NAME)
        etEmail!!.setText(email)
        etName!!.setText(name)

        btn_save!!.onClick {
            performRegister()
        }

    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as AppCompatImageView
        tvtitle.text = resources.getString(R.string.txt_edit_profile)
        ivBack.onClick {
            onBackPressed()
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




    fun performRegister() {
        if (etName!!.text.toString() == "") {
            Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
        } else if (etEmail!!.text.toString() != "" && !validEmail(etEmail!!.text.toString())) {
            Global.getAlertDialog(this, "Opps..!", "Enter valid e-mail!")
        } else {
            editprofile()
        }
    }

    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun editprofile()
    {
        showProgress(true)
        callApi(

            getRestApis().editmyprofile(etName!!.text.toString(),etEmail!!.text.toString(),token!!), onApiSuccess = {
                showProgress(false)
                sessionManager!!.setStringValue(USER_NAME,it.data.name!!)
                sessionManager!!.setStringValue(USER_EMAIL,it.data.email!!)

                onBackPressed()
            }, onApiError = {
                showProgress(false)

            }, onNetworkError = {
                showProgress(false)

            })
    }







}