package com.app.festivalpost.activity


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.utils.Constants.SharedPref.IS_LOGGED_IN
import com.app.festivalpost.utils.Constants.SharedPref.KEY_USER_DATA
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.getSharedPrefInstance
import com.emegamart.lelys.utils.extensions.launchActivity
import com.google.android.gms.tasks.TaskExecutors.MAIN_THREAD
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class RegisterVerificationActivity : AppBaseActivity(),
    ApiResponseListener {
    private var verificationId: String? = null
    private var mAuth: FirebaseAuth? = null
    private var tvsignup: TextView? = null
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var progressDialog: ProgressDialog? = null

    var number:String?=null
    var name:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        FirebaseApp.initializeApp(this@RegisterVerificationActivity)
        setActionbar()
        mAuth = FirebaseAuth.getInstance()
        apiManager = ApiManager(this@RegisterVerificationActivity)
        progressDialog = ProgressDialog(this@RegisterVerificationActivity)
        progressDialog!!.setMessage(resources.getString(R.string.txt_please_wait))
        val phonenumber = intent.getStringExtra("phonenumber")
        number = intent.getStringExtra("number")
        name = intent.getStringExtra("name")
        sendVerificationCode(phonenumber)
        edMobile.setText(Global.getPreference("pref_mobile", ""))
        gettvEdit().text = Html.fromHtml("<u><font color=blue>Edit</u>")
        gettvEdit().setOnClickListener {
            val intent = Intent(this@RegisterVerificationActivity, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        tvsignup = findViewById<View>(R.id.tvsignup) as TextView
        tvsignup!!.setOnClickListener { onBackPressed() }
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
            R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val edotp: EditText
        private get() = findViewById<View>(R.id.edotp) as EditText

    private fun gettvEdit(): TextView {
        return findViewById<View>(R.id.tvedit) as TextView
    }

    private val edMobile: EditText
        private get() = findViewById<View>(R.id.edtmobile) as EditText


    private fun performSubmit() {
        val code = edotp.text.toString().trim { it <= ' ' }
        if (code.isEmpty() || code.length < 6) {
            Toast.makeText(
                this@RegisterVerificationActivity,
                "Please enter OTP",
                Toast.LENGTH_SHORT
            ).show()
            edotp.requestFocus()
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
                        this@RegisterVerificationActivity,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun loadRegisterData() {
        showProgress(true)
        callApi(
            getRestApis().register(name!!, number!!),
            onApiSuccess = {
                showProgress(false)
                launchActivity<HomeActivity> {
                    getSharedPrefInstance().setValue(IS_LOGGED_IN, true)
                    getSharedPrefInstance().setValue(KEY_USER_DATA, it.data)


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
        } catch (e: Exception) {
        }
    }

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                if (progressDialog != null && progressDialog!!.isShowing) {
                    progressDialog!!.dismiss()
                }
                verificationId = s
                Toast.makeText(
                    this@RegisterVerificationActivity,
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
                Toast.makeText(this@RegisterVerificationActivity, e.message, Toast.LENGTH_LONG)
                    .show()
            }
        }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@RegisterVerificationActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@RegisterVerificationActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@RegisterVerificationActivity)
            if (requestService.equals(ApiEndpoints.register, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        val intent = Intent(
                            this@RegisterVerificationActivity,
                            AddFirstBusinessActivity::class.java
                        )

                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Global.getAlertDialog(this@RegisterVerificationActivity, "Opps..!", message)
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
            Global.dismissProgressDialog(this@RegisterVerificationActivity)
            Global.showFailDialog(this@RegisterVerificationActivity, responseString)
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
            if (status) {
                if (jsonObject.has("token")) {
                    val token = jsonObject.getString("token")
                    Global.storePreference(Constant.PREF_TOKEN, token)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}