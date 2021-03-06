package com.app.festivalpost.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.UserDataItem
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.iid.FirebaseInstanceId

import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppBaseActivity() {

    private var verificationId: String? = null
    private var etOtp: AppCompatEditText? = null
    private var mAuth: FirebaseAuth? = null
    var token: ForceResendingToken? = null
    var ivBack:ImageView?=null
    var sessionManager: SessionManager?=null

    private var etNumber:AppCompatEditText?=null
    private var etName:AppCompatEditText?=null
    private var spinnerCountry: CountryCodePicker?=null

    private var linearregister: LinearLayout?=null
    private var tvsignin:TextView?=null

    var user_token : String?=""
    var device_token : String?=""
    var device_id : String?=""
    var device_type : String?=""
    var mobileNumber : String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sessionManager= SessionManager(this)
        mAuth = FirebaseAuth.getInstance()

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            sessionManager!!.setStringValue(Constants.KeyIntent.DEVICE_TOKEN,token)
            // send it to server
        }

        device_token=sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_TOKEN)
        device_id=sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_ID)
        device_type=sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_TYPE)


        val toolbar=findViewById<View>(R.id.toolbar) as Toolbar
        ivBack=toolbar.findViewById(R.id.ivBack)

        etNumber=findViewById(R.id.et_number)
        spinnerCountry=findViewById(R.id.spinner)
        etName=findViewById(R.id.et_name)
        linearregister= findViewById<LinearLayout>(R.id.linearRegister)
        tvsignin= findViewById<TextView>(R.id.tvsignin)

        etName!!.onFocusChangeListener = this
        etNumber!!.onFocusChangeListener = this

        val bundle=intent.extras
        if (bundle!=null)
        {
            if (bundle.containsKey("mobile_number"))
            {
                mobileNumber=bundle["mobile_number"] as String?
            }
            etNumber!!.setText(mobileNumber)
        }
        else
        {
            mobileNumber=null
        }







        linearregister!!.setOnClickListener {
            performRegister()

        }

        tvsignin!!.setOnClickListener {
            launchActivity<LoginActivity> {  }
        }

        ivBack!!.setOnClickListener {
            onBackPressed()
        }


    }




    private fun showPopupDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_otp, null)

        etOtp = layout.findViewById<View>(R.id.et_otp) as AppCompatEditText
        val tvresendOtp = layout.findViewById<View>(R.id.tvresendOtp) as TextView
        val btn_done = layout.findViewById<View>(R.id.btn_done) as TextView
        val btn_cancel = layout.findViewById<View>(R.id.btn_cancel) as TextView


        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(false)


        val alertDialog = builder.create()

        btn_done.setOnClickListener {
            performSubmit()
        }

        btn_cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        tvresendOtp.setOnClickListener { resendVerificationCode("+" +spinnerCountry!!.selectedCountryCode+""+et_number.text.toString(),token!!) }

        alertDialog.show()


    }


     private fun performRegister() {
         if (etName!!.text.toString() == "") {
             Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
         } else if (etNumber!!.text.toString().length != 10) {
             Global.getAlertDialog(this, "Opps..!", "Enter valid Number!")
         } else {
             if (mobileNumber==null) {
                 sendVerificationCode("+" + spinnerCountry!!.selectedCountryCode + etNumber!!.editableText.toString())
             }else{
                 sendVerificationCode("+" + spinnerCountry!!.selectedCountryCode + etNumber!!.editableText.toString())
                 //loadRegisterData()
             }
             //performSubmit()
             //loadRegisterData()
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
            if (credential==null)
            {
                toast("Please try Again")
            }
            else{
                signInWithCredential(credential)

            }
        } catch (e: Exception) {
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                showProgress(false)
                if (task.isSuccessful) {
                    loadRegisterData()
                } else {
                    Toast.makeText(
                        this,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
    }

    private fun loadRegisterData() {
        if (device_token==null)
        {
            device_token="abc"
            showProgress(true)
            callApi(
                getRestApis().register(
                    etName!!.editableText.toString(),
                    etNumber!!.editableText.toString(),
                    device_id!!,
                    device_type!!,
                    device_token!!
                ),
                onApiSuccess = {
                    if (it.status!!) {
                        showProgress(false)
                        launchActivity<HomeActivity> {
                            try {
                                sessionManager!!.setStringValue(
                                    Constants.SharedPref.USER_TOKEN,
                                    it.token!!
                                )

                                var dataarraylist = arrayListOf<UserDataItem?>()
                                dataarraylist = it.data
                                for (i in 0 until dataarraylist.size) {
                                    sessionManager!!.setStringValue(
                                        Constants.SharedPref.USER_NAME,
                                        dataarraylist[i]!!.name!!
                                    )
                                    sessionManager!!.setStringValue(
                                        Constants.SharedPref.USER_NUMBER,
                                        dataarraylist[i]!!.mobile!!
                                    )
                                    sessionManager!!.setStringValue(
                                        Constants.SharedPref.USER_EMAIL,
                                        dataarraylist[i]!!.email!!
                                    )

                                }
                                sessionManager!!.setBooleanValue(
                                    Constants.SharedPref.IS_LOGGED_IN,
                                    true
                                )
                                finish()
                            } catch (e: java.lang.Exception) {

                            }

                        }
                    }
                },
                onApiError = {
                    showProgress(false)

                },
                onNetworkError = {
                    showProgress(false)

                })
        }
        else {


            showProgress(true)
            callApi(
                getRestApis().register(
                    etName!!.editableText.toString(),
                    etNumber!!.editableText.toString(),
                    device_id!!,
                    device_type!!,
                    device_token!!
                ),
                onApiSuccess = {
                    if (it.status!!) {
                        showProgress(false)
                        launchActivity<HomeActivity> {
                            try {
                                sessionManager!!.setStringValue(
                                    Constants.SharedPref.USER_TOKEN,
                                    it.token!!
                                )

                                var dataarraylist = arrayListOf<UserDataItem?>()
                                dataarraylist = it.data
                                for (i in 0 until dataarraylist.size) {
                                    sessionManager!!.setStringValue(
                                        Constants.SharedPref.USER_NAME,
                                        dataarraylist[i]!!.name!!
                                    )
                                    sessionManager!!.setStringValue(
                                        Constants.SharedPref.USER_NUMBER,
                                        dataarraylist[i]!!.mobile!!
                                    )
                                    sessionManager!!.setStringValue(
                                        Constants.SharedPref.USER_EMAIL,
                                        dataarraylist[i]!!.email!!
                                    )

                                }
                                sessionManager!!.setBooleanValue(
                                    Constants.SharedPref.IS_LOGGED_IN,
                                    true
                                )
                                finish()
                            } catch (e: java.lang.Exception) {

                            }

                        }
                    }
                },
                onApiError = {
                    showProgress(false)

                },
                onNetworkError = {
                    showProgress(false)

                })
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
                token=forceResendingToken
                Toast.makeText(
                    this@RegisterActivity,
                    "OTP sent to your mobile number.",
                    Toast.LENGTH_SHORT
                ).show()
                showPopupDialog(this@RegisterActivity)
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //signInWithCredential(phoneAuthCredential)
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    etOtp!!.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
    private fun resendVerificationCode(phoneNumber: String?, token: ForceResendingToken?) {
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
}