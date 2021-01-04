package com.app.festivalpost.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.UserDataItem
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_name
import kotlinx.android.synthetic.main.activity_login.et_number
import kotlinx.android.synthetic.main.activity_login.spinner
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LoginActivity : AppBaseActivity(), View.OnFocusChangeListener {

    var ivBack: ImageView? = null
    private var verificationId: String? = null
    private var etOtp: AppCompatEditText? = null
    private var mAuth: FirebaseAuth? = null
    var token: PhoneAuthProvider.ForceResendingToken? = null
    var sessionManager:SessionManager?=null

    private var etNumber:AppCompatEditText?=null
    private var spinnerCountry:CountryCodePicker?=null
    private var linearlogin:LinearLayout?=null
    private var tvsignup:TextView?=null

    var user_token : String?=null
    var device_token : String?=null
    var device_id : String?=null
    var device_type : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)

        sessionManager= SessionManager(this)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        ivBack = toolbar.findViewById(R.id.ivBack)

        user_token=sessionManager!!.getValueString(Constants.SharedPref.USER_TOKEN)
        device_token=sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_TOKEN)
        device_id=sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_ID)
        device_type=sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_TYPE)
        etNumber=findViewById(R.id.et_number)
        spinnerCountry=findViewById(R.id.spinner)
        linearlogin= findViewById<LinearLayout>(R.id.linearLogin)
        tvsignup= findViewById<TextView>(R.id.tvsignup)
        etNumber!!.onFocusChangeListener = this

        mAuth = FirebaseAuth.getInstance();
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            sessionManager!!.setStringValue(Constants.KeyIntent.DEVICE_TOKEN,token)

        }
        val isLoogedIn=sessionManager!!.getBooleanValue(Constants.SharedPref.IS_LOGGED_IN)

        if (isLoogedIn!!) {
            launchActivity<HomeActivity> { }
            finish()
        }



        linearlogin!!.onClick {
            performLogin()
        }

        tvsignup!!.onClick {
            launchActivity<RegisterActivity> { finish() }
        }

        ivBack!!.onClick {
            onBackPressed()
        }


    }

    private fun performLogin() {
        if (etNumber!!.text.toString().equals("", ignoreCase = true)) {
            Global.getAlertDialog(
                this@LoginActivity,
                "Opps..!",
                resources.getString(R.string.txt_fill_all_details)
            )
        } else {
            sendVerificationCode("+" + spinnerCountry!!.selectedCountryCode + etNumber!!.text.toString())
            //login()
        }
    }


    private fun login() {
        showProgress(true)
        callApi(

            getRestApis().login(etNumber!!.text.toString(),device_id!!,device_type!!,device_token!!),
            onApiSuccess = {
                showProgress(false)
                if (it.status!!)
                {
                launchActivity<HomeActivity> {
                    try {
                        sessionManager!!.setStringValue(Constants.SharedPref.USER_TOKEN,it.token!!)

                        var dataarraylist= arrayListOf<UserDataItem?>()
                        dataarraylist=it.data
                        for (i in 0 until dataarraylist.size) {
                            sessionManager!!.setStringValue(Constants.SharedPref.USER_NAME,dataarraylist[i]!!.name!!)
                            sessionManager!!.setStringValue(Constants.SharedPref.USER_NUMBER,dataarraylist[i]!!.mobile!!)
                            sessionManager!!.setStringValue(Constants.SharedPref.USER_EMAIL,dataarraylist[i]!!.email!!)

                        }
                        sessionManager!!.setBooleanValue(Constants.SharedPref.IS_LOGGED_IN, true)
                        finish()
                    } catch (e: java.lang.Exception) {

                    }

                }





                }
                else{
                    if (it.message!!.equals("Mobile number not registered. Please register first")) {
                        toast(it.message)
                    }
                }


            },
            onApiError = {
                showProgress(false)
                toast("Something Went Wrong")

            },
            onNetworkError = {
                showProgress(false)
                toast("Please Connect your network")

            })
    }


    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        if (hasFocus) {
            (v as EditText).setTextColor(this.color(R.color.colorPrimary))
            v.background = resources.getDrawable(R.drawable.edit_text_border_selected)
        } else {
            (v as EditText).setTextColor(this.color(R.color.black))
            v.background = resources.getDrawable(R.drawable.edit_text_border)
        }
    }

    private fun performSubmit() {
        val code = etOtp!!.text.toString().trim()
        if (code.isEmpty() || code.length < 6) {
            Toast.makeText(
                this,
                "Please enter OTP",
                Toast.LENGTH_SHORT
            ).show()
            etOtp!!.requestFocus()
            return
        }
        verifyCode(code)
    }

    private fun verifyCode(code: String) {
        try {
            showProgress(true)
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            signInWithCredential(credential)
        } catch (e: Exception) {
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                showProgress(false)
                if (task.isSuccessful) {
                    login()
                } else {
                    Toast.makeText(
                        this,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun sendVerificationCode(number: String?) {
        try {
            showProgress(true)
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number!!,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
            )
        } catch (e: Exception) {
        }
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                showProgress(false)
                verificationId = s
                token = forceResendingToken
                Toast.makeText(
                    this@LoginActivity,
                    "OTP sent to your mobile number.",
                    Toast.LENGTH_SHORT
                ).show()
                showPopupDialog(this@LoginActivity)
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    etOtp!!.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }

    private fun resendVerificationCode(
        phoneNumber: String?,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        showProgress(true)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber!!,  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,
            this,
            mCallBack,
            token
        ) // resending with token got at previous call's `callbacks` method `onCodeSent`
    }

    private fun showPopupDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_otp, null)

        etOtp = layout.findViewById<View>(R.id.et_otp) as AppCompatEditText
        val tvresendOtp = layout.findViewById<View>(R.id.tvresendOtp) as TextView
        val btn_done = layout.findViewById<View>(R.id.btn_done) as TextView
        val ib_cancel = layout.findViewById<View>(R.id.ib_cancel) as ImageView


        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(false)


        val alertDialog = builder.create()

        btn_done.onClick {
            performSubmit()
        }

        tvresendOtp.onClick {
            resendVerificationCode(
                "+" + spinner.selectedCountryCode + "" + et_number.text.toString(),
                token!!
            )
        }


        ib_cancel.onClick {
            alertDialog.dismiss()
        }

        alertDialog.show()


    }


}
