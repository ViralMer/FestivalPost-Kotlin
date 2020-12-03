package com.app.festivalpost.activity

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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener, ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var btnsubmit: Button? = null
    var message = ""
    private var tvsignup: TextView? = null
    private var tvError: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setActionbar()
        apiManager = ApiManager(this@LoginActivity)
        findViewById<View>(R.id.btnsubmit).setOnClickListener(this)
        btnsubmit = findViewById(R.id.btnsubmit)
        tvsignup = findViewById<View>(R.id.tvsignup) as TextView
        tvError = findViewById<View>(R.id.tvError) as TextView
        tvsignup!!.setOnClickListener {
            val detailact = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(detailact)
        }
        edmobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (edmobile.text.length == 10) {
                    btnsubmit!!.setClickable(true)
                    btnsubmit!!.setBackgroundResource(R.drawable.large_button_bg)
                } else {
                    btnsubmit!!.setClickable(false)
                    //tvError.setVisibility(View.GONE);
                    btnsubmit!!.setBackgroundResource(R.drawable.large_button_bg_light)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (edmobile.text.length == 10) {
                    btnsubmit!!.setClickable(true)
                    btnsubmit!!.setBackgroundResource(R.drawable.large_button_bg)
                } else {
                    btnsubmit!!.setClickable(false)
                    //tvError.setVisibility(View.GONE);
                    btnsubmit!!.setBackgroundResource(R.drawable.large_button_bg_light)
                }
            }
        })
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
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

    private val edmobile: EditText
        private get() = findViewById<View>(R.id.edmobile) as EditText

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnsubmit -> performLogin()
        }
    }

    fun performLogin() {
        if (edmobile.text.toString().equals("", ignoreCase = true)) {
            Global.getAlertDialog(
                this@LoginActivity,
                "Opps..!",
                resources.getString(R.string.txt_fill_all_details)
            )
        } else {
            Global.showProgressDialog(this@LoginActivity)
            apiManager!!.login(ApiEndpoints.login, edmobile.text.toString().trim { it <= ' ' })
        }
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@LoginActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@LoginActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@LoginActivity)
            if (requestService.equals(ApiEndpoints.login, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        val detailAct = Intent(this@LoginActivity, VerificationActivity::class.java)
                        detailAct.putExtra("phonenumber", "+91" + edmobile.text.toString())
                        detailAct.putExtra("isbusiness", isbusiness)
                        startActivity(detailAct)
                        //tvError.setVisibility(View.GONE);
                    } else {
                        //tvError.setVisibility(View.VISIBLE);
                        //tvError.setText(message);
                        if (message == "Mobile number not registered. Please register first") {
                            val materialAlertDialogBuilder = AlertDialog.Builder(this@LoginActivity)
                            val inflater = this@LoginActivity.getSystemService(
                                LAYOUT_INFLATER_SERVICE
                            ) as LayoutInflater
                            val view = inflater.inflate(R.layout.custom_error_dialog, null)
                            val tvTitle: TextView
                            val tvMessage: TextView
                            val btnOk: Button
                            tvTitle = view.findViewById(R.id.tvTitle)
                            tvMessage = view.findViewById(R.id.tvMessage)
                            btnOk = view.findViewById(R.id.btnOk)
                            tvTitle.text = "Opps..!"
                            tvMessage.text = message
                            materialAlertDialogBuilder.setView(view).setCancelable(true)
                            val b = materialAlertDialogBuilder.create()
                            btnOk.setOnClickListener {
                                b.dismiss()
                                val intent =
                                    Intent(this@LoginActivity, RegisterActivity::class.java)
                                intent.putExtra("number", "" + edmobile.text.toString())
                                startActivity(intent)
                            }
                            b.show()
                        } else {
                            Global.getAlertDialog(this@LoginActivity, "Opps..!", message)
                        }
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
            Global.dismissProgressDialog(this@LoginActivity)
            Global.showFailDialog(this@LoginActivity, responseString)
        }
    }

    override fun onItemClicked(`object`: Any, index: Int) {
        TODO("Not yet implemented")
    }

    var isbusiness = false
    fun processResponse(responseString: String?) {
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("isbusiness")) {
                isbusiness = jsonObject.getBoolean("isbusiness")
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