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
import com.app.festivalpost.utils.Constants
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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_name
import kotlinx.android.synthetic.main.activity_login.et_number
import kotlinx.android.synthetic.main.activity_login.spinner
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class LoginActivity : AppBaseActivity(),View.OnFocusChangeListener{

    var ivBack:ImageView?=null
    private var verificationId: String? = null
    private var etOtp: AppCompatEditText? = null
    private var mAuth: FirebaseAuth? = null
    var token: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)
        et_number.onFocusChangeListener=this

        val toolbar=findViewById<View>(R.id.toolbar) as Toolbar
        ivBack=toolbar.findViewById(R.id.ivBack)

        mAuth = FirebaseAuth.getInstance();
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            getSharedPrefInstance().setValue(Constants.KeyIntent.DEVICE_TOKEN,token)
            // send it to server
        }
        if (isLoggedIn())
        {
            launchActivity<HomeActivity> {  }
            finish()
        }


        linearLogin.onClick {
            performLogin()
        }

        tvsignup.onClick {
            launchActivity<RegisterActivity> {  }
        }

        ivBack!!.onClick {
            onBackPressed()
        }





    }

private fun performLogin() {
    if (et_number.text.toString().equals("", ignoreCase = true)) {
        Global.getAlertDialog(
            this@LoginActivity,
            "Opps..!",
            resources.getString(R.string.txt_fill_all_details)
        )
    } else {
        Log.d("spinner","+" + spinner.selectedCountryCode + et_number.text.toString())
        sendVerificationCode("+" + spinner.selectedCountryCode + et_number.text.toString())
    }
}


    private fun login() {
        showProgress(true)
        callApi(

            getRestApis().login(et_number.text.toString()),
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
                }
                finish()


            },
            onApiError = {
                showProgress(false)

            },
            onNetworkError = {
                showProgress(false)

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
                token=forceResendingToken
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
    private fun resendVerificationCode(phoneNumber: String?, token: PhoneAuthProvider.ForceResendingToken?) {
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

        tvresendOtp.onClick { resendVerificationCode(et_number.text.toString(),token!!) }


        ib_cancel.onClick {
            alertDialog.dismiss()
        }

        alertDialog.show()


    }}