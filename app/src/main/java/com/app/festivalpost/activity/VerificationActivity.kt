package com.app.festivalpost.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity(), View.OnClickListener {
    private var verificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    private var tvsignup: TextView? = null
    private var tvresendOtp: TextView? = null
    var progressDialog: ProgressDialog? = null
    var isbusiness = false
    var token: ForceResendingToken? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        FirebaseApp.initializeApp(this@VerificationActivity)
        setActionbar()
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("isbusiness")) {
                isbusiness = bundle["isbusiness"] as Boolean
            }
        }
        progressDialog = ProgressDialog(this@VerificationActivity)
        progressDialog!!.setMessage(resources.getString(R.string.txt_please_wait))
        mAuth = FirebaseAuth.getInstance()
        val phonenumber = intent.getStringExtra("phonenumber")
        sendVerificationCode(phonenumber)
        findViewById<View>(R.id.btnsubmit).setOnClickListener(this)
        tvsignup = findViewById<View>(R.id.tvsignup) as TextView
        tvresendOtp = findViewById<View>(R.id.tvresendOtp) as TextView
        tvresendOtp!!.isClickable = false
        tvsignup!!.setOnClickListener {
            val detailact = Intent(this@VerificationActivity, RegisterActivity::class.java)
            startActivity(detailact)
        }
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvresendOtp!!.text = "00: " + millisUntilFinished / 1000
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                tvresendOtp!!.isClickable = true
                tvresendOtp!!.text = "Resend OTP"
            }
        }.start()
        tvresendOtp!!.setOnClickListener {
            if (tvresendOtp!!.text.toString() == "Resend OTP") {
                resendVerificationCode(phonenumber, token)
            }
        }
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_sign_in)
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

    private val edotp: EditText
        private get() = findViewById<View>(R.id.edotp) as EditText
    private val edtmobile: EditText
        private get() = findViewById<View>(R.id.edtmobile) as EditText

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnsubmit -> performSubmit()
        }
    }

    private fun performSubmit() {
        val code = edotp.text.toString().trim { it <= ' ' }
        if (code.isEmpty() || code.length < 6) {
            Toast.makeText(this@VerificationActivity, "Please enter OTP", Toast.LENGTH_SHORT).show()
            edotp.requestFocus()
            return
        }
        verifyCode(code)
    }

    private fun verifyCode(code: String) {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                if (task.isSuccessful) {
                    Global.storePreference(Constant.PREF_LOGIN, true)
                    if (isbusiness) {
                        val intent = Intent(this@VerificationActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        val intent =
                            Intent(this@VerificationActivity, AddFirstBusinessActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@VerificationActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun sendVerificationCode(number: String?) {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number!!,
            60,
            TimeUnit.SECONDS,
            this,
            mCallBack
        )
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                verificationId = s
                token = forceResendingToken
                Toast.makeText(
                    this@VerificationActivity,
                    "OTP sent to your mobile number.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    edotp.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerificationActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    fun resendVerificationCode(phoneNumber: String?, token: ForceResendingToken?) {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            progressDialog!!.show()
        }
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