package com.app.festivalpost.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.globals.Global
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.getSharedPrefInstance
import com.emegamart.lelys.utils.extensions.isLoggedIn
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.onClick
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit

class RegisterActivity : AppBaseActivity() {

    private var verificationId: String? = null
    private var etOtp: AppCompatEditText? = null
    private var mAuth: FirebaseAuth? = null
    var token: ForceResendingToken? = null
    var ivBack:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        FirebaseApp.initializeApp(this)


        et_name.onFocusChangeListener = this
        et_number.onFocusChangeListener = this
        mAuth = FirebaseAuth.getInstance()
        val toolbar=findViewById<View>(R.id.toolbar) as Toolbar
        ivBack=toolbar.findViewById(R.id.ivBack)

        if (isLoggedIn())
        {
            launchActivity<HomeActivity> {  }
            finish()
        }

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            getSharedPrefInstance().setValue(Constants.KeyIntent.DEVICE_TOKEN, token)
            // send it to server
        }

        linearRegister.onClick {
            performRegister()

        }

        tvsignin!!.onClick {
            launchActivity<LoginActivity> {  }
        }

        ivBack!!.onClick {
            onBackPressed()
        }


    }




    private fun showPopupDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_otp, null)

        etOtp = layout.findViewById<View>(R.id.et_otp) as AppCompatEditText
        val tvresendOtp = layout.findViewById<View>(R.id.tvresendOtp) as TextView
        val btn_done = layout.findViewById<View>(R.id.btn_done) as TextView



        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(false)


        val alertDialog = builder.create()

        btn_done.onClick {
            performSubmit()
        }

        tvresendOtp.onClick { resendVerificationCode(et_number.text.toString(),token!!) }

        alertDialog.show()


    }


     private fun performRegister() {
         if (et_name.text.toString() == "") {
             Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
         } else if (et_number.text.toString().length != 10) {
             Global.getAlertDialog(this, "Opps..!", "Enter valid Number!")
         } else {
             sendVerificationCode("+" + spinner.selectedCountryCode + et_number.text.toString())
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
        showProgress(true)
        callApi(
            getRestApis().register(et_name.text.toString(), et_number.text.toString()),
            onApiSuccess = {
                showProgress(false)
                launchActivity<HomeActivity> {
                    getSharedPrefInstance().setValue(Constants.SharedPref.IS_LOGGED_IN, true)
                    getSharedPrefInstance().setValue(Constants.SharedPref.KEY_USER_DATA, it.data)
                    getSharedPrefInstance().setValue(Constants.SharedPref.USER_TOKEN, it.token)

                    for (i in 0 until it.data.size )
                    {
                        getSharedPrefInstance().setValue(Constants.SharedPref.USER_NAME,it.data[i]!!.name)
                        getSharedPrefInstance().setValue(Constants.SharedPref.USER_NUMBER, it.data[i]!!.mobile)
                        getSharedPrefInstance().setValue(Constants.SharedPref.USER_EMAIL, it.data[i]!!.email)
                    }
                    finish()
                }
            },
            onApiError = {
                showProgress(false)

            },
            onNetworkError = {
                showProgress(false)

            })
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