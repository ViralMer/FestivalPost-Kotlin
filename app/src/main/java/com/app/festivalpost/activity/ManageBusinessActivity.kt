package com.app.festivalpost.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.R
import com.app.festivalpost.adapter.BusinessItemAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.BusinessItem
import com.app.festivalpost.models.UserItem
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class ManageBusinessActivity : AppCompatActivity(), ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    private var lvdata: ListView? = null
    var businessItemArrayList = ArrayList<BusinessItem>()
    var userItem: UserItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_business)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                userItem = bundle["object"] as UserItem?
                Log.d("BusinessID12", "Default Id :" + userItem!!.default_business_id)
            }
        }
        apiManager = ApiManager(this@ManageBusinessActivity)
        setActionbar()
        lvdata = findViewById<View>(R.id.lvdata) as ListView
    }

    override fun onResume() {
        super.onResume()
        businesses
    }

    val businesses: Unit
        get() {
            Global.showProgressDialog(this@ManageBusinessActivity)
            apiManager!!.getmyallbusiness(
                ApiEndpoints.getmyallbusiness,
                Global.getPreference(Constant.PREF_TOKEN, "")
            )
        }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txtbusiness)
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction.text = resources.getString(R.string.txt_add)
        tvaction.setOnClickListener {
            if (businessItemArrayList.size <= 5) {
                val act = Intent(this@ManageBusinessActivity, AddBusinessActivity::class.java)
                startActivity(act)
            } else {
                val materialAlertDialogBuilder = AlertDialog.Builder(this@ManageBusinessActivity)
                val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.custom_error_dialog, null)
                val tvTitle: TextView
                val tvMessage: TextView
                val btnOk: Button
                tvTitle = view.findViewById(R.id.tvTitle)
                tvMessage = view.findViewById(R.id.tvMessage)
                btnOk = view.findViewById(R.id.btnOk)
                btnOk.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                btnOk.setTextColor(resources.getColor(R.color.colorBlack))
                tvTitle.text = "Maximum Limit reached"
                tvMessage.text = "You can only add 5 businesses"
                materialAlertDialogBuilder.setView(view).setCancelable(true)
                val b = materialAlertDialogBuilder.create()
                btnOk.setOnClickListener { b.dismiss() }
                b.show()
            }
        }
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

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ManageBusinessActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@ManageBusinessActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ManageBusinessActivity)
            if (requestService.equals(ApiEndpoints.getmyallbusiness, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        if (businessItemArrayList.size > 0) {
                            val adapter = BusinessItemAdapter(
                                this@ManageBusinessActivity,
                                businessItemArrayList,
                                userItem!!
                            )
                            lvdata!!.adapter = adapter
                            lvdata!!.visibility = View.VISIBLE
                        } else {
                            lvdata!!.visibility = View.GONE
                            Global.removePreference(Constant.PREF_CURRENT_BUSINESS)
                            Global.showFailDialog(this@ManageBusinessActivity, message)
                        }
                    } else {
                        lvdata!!.visibility = View.GONE
                        Global.showFailDialog(this@ManageBusinessActivity, message)
                    }
                } catch (e: Exception) {
                }
            }
            if (requestService.equals(ApiEndpoints.markascurrentbusiness, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processMarkCurrentResponse(responseString)
                    if (status) {
                        userItem!!.default_business_id = currentBusinessID
                        Global.showSuccessDialog(this@ManageBusinessActivity, message)
                        apiManager!!.getmyallbusiness(
                            ApiEndpoints.getmyallbusiness, Global.getPreference(
                                Constant.PREF_TOKEN, ""
                            )
                        )
                    } else {
                        Global.showFailDialog(this@ManageBusinessActivity, message)
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
            Global.dismissProgressDialog(this@ManageBusinessActivity)
            Global.showFailDialog(this@ManageBusinessActivity, responseString)
        }
    }



    fun processResponse(responseString: String?) {
        businessItemArrayList = ArrayList()
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
                if (jsonObject.has("data")) {
                    val jsonArray = jsonObject.getJSONArray("data")
                    if (jsonArray.length() > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val j = jsonArray.getJSONObject(i)
                            val b = Gson().fromJson(j.toString(), BusinessItem::class.java)
                            val tempBusinessID = java.lang.String.valueOf(b.busiId)
                            Log.d(
                                "BusinessID",
                                "" + tempBusinessID + "Default Id :" + userItem!!.default_business_id
                            )
                            if (tempBusinessID.equals(
                                    userItem!!.default_business_id,
                                    ignoreCase = true
                                )
                            ) {
                                b.isCurrentBusiness = true
                            } else {
                                b.isCurrentBusiness = false
                            }
                            businessItemArrayList.add(b)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun processMarkCurrentResponse(responseString: String?) {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var currentBusinessID = ""
    /*override fun onItemClicked(`object`: Any?, index: Int) {

    }*/

    override fun onItemClicked(`object`: Any, index: Int) {
        val b = `object` as BusinessItem
        currentBusinessID = "" + b.busiId
        Global.showProgressDialog(this@ManageBusinessActivity)
        apiManager!!.markascurrentbusiness(
            ApiEndpoints.markascurrentbusiness, Global.getPreference(
                Constant.PREF_TOKEN, ""
            ), b.busiId!!.toString()
        )
    }


}