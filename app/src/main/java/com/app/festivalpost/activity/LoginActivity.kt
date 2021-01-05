package com.app.festivalpost.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.UserDataItem
import com.app.festivalpost.photoeditor.SmsBroadcastReceiver
import com.app.festivalpost.photoeditor.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.hbb20.CountryCodePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_number
import kotlinx.android.synthetic.main.activity_login.spinner
import kotlinx.android.synthetic.main.activity_register.*
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

   // var smsBroadcastReceiver:SmsBroadcastReceiver?=null
    val REQ_USER_CONSENT:Int?=1234



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)

        sessionManager= SessionManager(this)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        ivBack = toolbar.findViewById(R.id.ivBack)
        //smsBroadcastReceiver = SmsBroadcastReceiver()
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
            sessionManager!!.setStringValue(Constants.KeyIntent.DEVICE_TOKEN, token)

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

            getRestApis().login(
                etNumber!!.text.toString(),
                device_id!!,
                device_type!!,
                device_token!!
            ),
            onApiSuccess = {
                showProgress(false)
                if (it.status!!) {
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


                } else {
                    toast(it.message!!)
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
                //startSmsUserConsent()
                Toast.makeText(
                    this@LoginActivity,
                    "OTP sent to your mobile number.",
                    Toast.LENGTH_SHORT
                ).show()
                showPopupDialog(this@LoginActivity)
                //startSmsUserConsent()
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    //verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG)
                    .show()
                showProgress(false)
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

    private fun saveReadSmsPermission() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        //verifyCode("")

                    } else {


                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    /*private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcastReceiverListener = object : SmsBroadcastReceiverListener {
            override fun onSuccess(intent: Intent?) {
                startActivityForResult(intent, REQ_USER_CONSENT!!)
            }

            override fun onFailure() {}
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }*/

    override fun onStart() {
        super.onStart()
        //registerBroadcastReceiver();
    }


    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(smsBroadcastReceiver);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_USER_CONSENT) {
            if (resultCode == RESULT_OK && data != null) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                /*textViewMessage.setText(
                    String.format(
                        "%s - %s",
                        getString(android.R.string.received_message),
                        message
                    )
                )*/
                //getOtpFromMessage(message)
            }
        }
    }


}
