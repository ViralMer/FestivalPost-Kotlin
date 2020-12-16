package com.app.festivalpost.activity

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_home.*

class RegisterActivity : AppBaseActivity() {

    private var tvsignin: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        et_name.onFocusChangeListener = this
        et_number.onFocusChangeListener = this


        tvsignin = findViewById<View>(R.id.tvsignin) as TextView

        tvsignin!!.setOnClickListener { onBackPressed() }

        btn_next.onClick {
            showProgress(true)
            callApi(
                getRestApis().getCategoryImages("token"),
                onApiSuccess = { res ->
                    showProgress(false)
                    if (res.status!!) {
                    } else {
                        linearFestival.hide()
                        linearCategory.hide()
                    }
                },
                onApiError = {
                    showProgress(false)
                    linearCategory.hide()
                    linearFestival.hide()

                },
                onNetworkError = {

                    showProgress(false)
                })
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


    /* fun performRegister() {
         if (edname.text.toString() == "") {
             Global.getAlertDialog(this, "Opps..!", "Please Enter Name")
         } else if (edemail.text.toString() != "" && !validEmail(edemail.text.toString())) {
             Global.getAlertDialog(this, "Opps..!", "Enter valid e-mail!")
         } else if (edmobile.text.toString().length != 10) {
             Global.getAlertDialog(this, "Opps..!", "Please Enter 10 Digit Mobile Number")
         } else {
             //Global.showProgressDialog(RegisterActivity.this);
             val detailAct = Intent(this@RegisterActivity, RegisterVerificationActivity::class.java)
             Global.storePreference("pref_mobile", edmobile.text.toString())
             Global.storePreference("pref_email", edemail.text.toString())
             Global.storePreference("pref_name", edname.text.toString())
             Global.storePreference("pref_referal", edreferal.text.toString())
             detailAct.putExtra("phonenumber", "+91" + edmobile.text.toString())
             startActivity(detailAct)
             finish()
             //apiManager.register(ApiEndpoints.register, getEdname().getText().toString().trim(), getEdemail().getText().toString().trim(), getEdmobile().getText().toString().trim(), getEdreferal().getText().toString().trim());
         }
     }*/

    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}