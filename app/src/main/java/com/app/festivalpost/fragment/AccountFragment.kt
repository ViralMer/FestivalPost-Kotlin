package com.app.festivalpost.fragment

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.ClipboardManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import com.app.festivalpost.*
import com.app.festivalpost.activity.*
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.fragment.AccountFragment
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.UserItem
import com.google.gson.Gson
import org.json.JSONObject

class AccountFragment : Fragment(), View.OnClickListener, ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    private var tvname: TextView? = null
    private var tvemail: TextView? = null
    private var tveditaccount: TextView? = null
    private var tvcode: TextView? = null
    private var ibShare: ImageButton? = null
    private var layManageBusinessDetails: LinearLayout? = null
    private var tvcredit: TextView? = null
    private var layShareApp: LinearLayout? = null
    private var layRate: LinearLayout? = null
    private var layHelp: LinearLayout? = null
    private var layPrivacy: LinearLayout? = null
    private var layTerms: LinearLayout? = null
    private var lay_my_posts: LinearLayout? = null
    var userItem: UserItem? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiManager = ApiManager(activity!!, this@AccountFragment)
        tvname = view.findViewById<View>(R.id.tvname) as TextView
        tvemail = view.findViewById<View>(R.id.tvemail) as TextView
        tveditaccount = view.findViewById<View>(R.id.tveditaccount) as TextView
        tvcode = view.findViewById<View>(R.id.tvcode) as TextView
        layManageBusinessDetails =
            view.findViewById<View>(R.id.lay_manage_business_details) as LinearLayout
        tvcredit = view.findViewById<View>(R.id.tvcredit) as TextView
        layShareApp = view.findViewById<View>(R.id.lay_share_app) as LinearLayout
        layRate = view.findViewById<View>(R.id.lay_rate) as LinearLayout
        layHelp = view.findViewById<View>(R.id.lay_help) as LinearLayout
        layPrivacy = view.findViewById<View>(R.id.lay_privacy) as LinearLayout
        layTerms = view.findViewById<View>(R.id.lay_terms) as LinearLayout
        lay_my_posts = view.findViewById<View>(R.id.lay_my_posts) as LinearLayout
        ibShare = view.findViewById(R.id.ibShare)
        view.findViewById<View>(R.id.btnsubmit).setOnClickListener(this)
        layShareApp!!.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage = """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        }
        layRate!!.setOnClickListener {
            val uri = Uri.parse("market://details?id=" + activity!!.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, "Couldn't launch the market", Toast.LENGTH_LONG).show()
            }
        }
        layTerms!!.setOnClickListener {
            val titleval = resources.getString(R.string.txt_terms_condition)
            val url = "http://festivalpost.in/admin/termsandcondition"
            val detailact = Intent(activity, WebBrowserActivity::class.java)
            detailact.putExtra("title", titleval)
            detailact.putExtra("url", url)
            startActivity(detailact)
        }
        layHelp!!.setOnClickListener {
            val detailact = Intent(activity, HelpandSupportActivity::class.java)
            startActivity(detailact)
        }
        layPrivacy!!.setOnClickListener {
            val titleval = resources.getString(R.string.txt_privacy_policy)
            val url = "http://festivalpost.in/admin/privacypolicy"
            val detailact = Intent(activity, WebBrowserActivity::class.java)
            detailact.putExtra("title", titleval)
            detailact.putExtra("url", url)
            startActivity(detailact)
        }
        tveditaccount!!.setOnClickListener {
            val detailact = Intent(activity, EditProfileActivity::class.java)
            detailact.putExtra("object", userItem)
            startActivity(detailact)
        }
        layManageBusinessDetails!!.setOnClickListener {
            val detailact = Intent(activity, ManageBusinessActivity::class.java)
            detailact.putExtra("object", userItem)
            Log.d("BusinessID1", "Default Id :" + userItem!!.default_business_id)
            startActivity(detailact)
        }
        tvcode!!.setOnClickListener {
            val cm = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.text = tvcode!!.text
            Toast.makeText(activity, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
        ibShare!!.setOnClickListener(View.OnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                var shareMessage =
                    "I am inviting you to the amazing business festival post creation app \n\n"
                shareMessage = """
                    ${shareMessage}My Festival Post app's referral code is:${tvcode!!.text}
                    
                    https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }
        })
        lay_my_posts!!.setOnClickListener {
            val detailact = Intent(activity, MyPostActivity::class.java)
            startActivity(detailact)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnsubmit -> performLogout()
        }
    }

    override fun onStart() {
        super.onStart()
        Global.showProgressDialog(activity)
        apiManager!!.getmyprofile(
            ApiEndpoints.getmyprofile,
            Global.getPreference(Constant.PREF_TOKEN, "")
        )
    }

    fun performLogout() {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(activity!!)
        builder.setTitle(activity!!.resources.getString(R.string.txt_logout_title))
            .setMessage(activity!!.resources.getString(R.string.txt_logout_message))
            .setPositiveButton(activity!!.resources.getString(R.string.txt_yes)) { dialog, which ->
                Global.storePreference(Constant.PREF_TOKEN, "")
                Global.storePreference(Constant.PREF_LOGIN, false)
                Global.storePreference(Constant.PREF_CURRENT_BUSINESS, "")
                Global.storePreference(Constant.PREF_SCORE, "")
                Global.storePreference(Constant.PREF_LOGOUT, false)
                val detailAct = Intent(activity, LoginActivity::class.java)
                detailAct.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity!!.startActivity(detailAct)
                activity!!.finish()
            }
            .setNegativeButton(activity!!.resources.getString(R.string.txt_no)) { dialog, which -> dialog.dismiss() }
            .show()
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(activity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(activity!!)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(activity)
            if (requestService.equals(ApiEndpoints.getmyprofile, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        if (userItem != null) {
                            tvname!!.text = userItem!!.name
                            tvemail!!.text = userItem!!.email
                            Global.storePreference(Constant.PREF_SCORE, userItem!!.userCredit)
                            tvcredit!!.text =
                                resources.getString(R.string.txt_credit) + " : " + userItem!!.userCredit
                            tvcode!!.text = "" + userItem!!.refCode
                        }
                    } else {
                        Global.showFailDialog(activity, message)
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
            Global.dismissProgressDialog(activity)
            Global.showFailDialog(activity, responseString)
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
                if (jsonObject.has("data")) {
                    val userJson = jsonObject.getJSONObject("data")
                    userItem = Gson().fromJson(userJson.toString(), UserItem::class.java)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(): AccountFragment {
            return AccountFragment()
        }
    }
}