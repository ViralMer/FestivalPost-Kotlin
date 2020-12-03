package com.app.festivalpost.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.RegisterVerificationActivity
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.globals.Global

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    private var tvsignin: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setActionbar()
        findViewById<View>(R.id.btnsubmit).setOnClickListener(this)
        tvsignin = findViewById<View>(R.id.tvsignin) as TextView
        if (Global.getPreference("pref_name", "") != "") {
            edname.setText(Global.getPreference("pref_name", ""))
            edemail.setText(Global.getPreference("pref_email", ""))
            edmobile.setText(Global.getPreference("pref_mobile", ""))
            edreferal.setText(Global.getPreference("pref_referal", ""))
        }
        tvsignin!!.setOnClickListener { onBackPressed() }
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_signup)
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
    private val edreferal: EditText
        private get() = findViewById<View>(R.id.edreferal) as EditText

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
    }

    private fun validEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}