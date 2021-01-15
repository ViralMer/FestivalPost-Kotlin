package com.app.festivalpost.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.adapter.BusinessItemAdapter
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.Constants.SharedPref.KEY_FRAME_LIST
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_account.*
import java.util.*

class ManageBusinessActivity : AppBaseActivity(),OnItemClickListener {
    private var lvdata: RecyclerView? = null
    var businessItemArrayList = arrayListOf<CurrentBusinessItem?>()
    var businessItemAdapter:BusinessItemAdapter?=null
    var sessionManager:SessionManager?=null
    var token : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_business)
        sessionManager= SessionManager(this)
        token=sessionManager!!.getValueString(Constants.SharedPref.USER_TOKEN)
        setActionbar()
        val linearHelp=findViewById<View>(R.id.linearHelpSupport) as LinearLayout
        loadManageBusinessAllData()
        lvdata = findViewById<View>(R.id.lvdata) as RecyclerView
        linearHelp.onClick {
            showPopupDialog(this@ManageBusinessActivity)
        }

    }

    override fun onResume() {
        super.onResume()
        loadManageBusinessAllData()
    }

    private fun loadManageBusinessAllData()
    {
        showProgress(true)
        callApi(
            getRestApis().getAllMyBusiness(token!!), onApiSuccess = {
                showProgress(false)
                lvdata!!.show()
                businessItemArrayList = it.data
                businessItemAdapter = BusinessItemAdapter(this, businessItemArrayList)
                lvdata!!.adapter = businessItemAdapter


            }, onApiError = {
                showProgress(false)
                lvdata!!.hide()

            }, onNetworkError = {
                showProgress(false)
                lvdata!!.hide()

            })
    }

    private fun isPackageInstalled(packagename: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageGids(packagename)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun showPopupDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_help_support, null)

        val linearCall = layout.findViewById<View>(R.id.linearCall) as LinearLayout
        val linearWhatsapp = layout.findViewById<View>(R.id.linearWhatsapp) as LinearLayout
        val linearEmail = layout.findViewById<View>(R.id.linearemail) as LinearLayout
        val ibCancel = layout.findViewById<View>(R.id.ib_cancel) as AppCompatImageView


        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(true)

        val alertDialog = builder.create()

        linearEmail.onClick {
            val subject = resources.getString(R.string.txt_help_support)
            val content = ""
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constant.ADMIN_MAIL))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            startActivity(intent)
        }

        linearCall.onClick {
            val u = Uri.parse("tel:" + Constant.ADMIN_PHONE)
            val i = Intent(Intent.ACTION_DIAL, u)
            try {
                startActivity(i)
            } catch (s: SecurityException) {
                Toast.makeText(context, s.message, Toast.LENGTH_LONG)
                    .show()
            }
        }

        linearWhatsapp.onClick {

            if(isPackageInstalled("com.whatsapp.w4b",packageManager))
            {
                val url =
                    "https://api.whatsapp.com/send?phone=918070794444&text=Inquiry from Festival Post&source=&data=&app_absent="
                try {
                    val pm = packageManager
                    pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES)
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    i.putExtra(Intent.EXTRA_TEXT, "Inquiry from FestivalPost")
                    startActivity(i)
                } catch (e: PackageManager.NameNotFoundException) {
                    Toast.makeText(
                        this@ManageBusinessActivity,
                        "Whatsapp Business app not installed in your phone",
                        Toast.LENGTH_SHORT
                    ).show()
                    e.printStackTrace()
                }
            }
            else if (isPackageInstalled("com.whatsapp",packageManager)) {
                val url =
                    "https://api.whatsapp.com/send?phone=918070794444&text=Inquiry from Festival Post&source=&data=&app_absent="
                try {
                    val pm = packageManager
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    i.putExtra(Intent.EXTRA_TEXT, "Inquiry from FestivalPost")
                    startActivity(i)
                } catch (e: PackageManager.NameNotFoundException) {
                    Toast.makeText(
                        this@ManageBusinessActivity,
                        "Whatsapp app not installed in your phone",
                        Toast.LENGTH_SHORT
                    ).show()
                    e.printStackTrace()
                }
            }
        }



        ibCancel.onClick {
            alertDialog.dismiss()
        }

        alertDialog.show()


    }


    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as ImageView
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView

        ivBack.onClick { onBackPressed() }
        tvaction.setOnClickListener {
            if (businessItemArrayList.size <= 4) {
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

    var currentBusinessID = ""
    override fun onItemClicked(`object`: Any?, index: Int) {
        val b = `object` as CurrentBusinessItem?
        currentBusinessID = "" + b!!.busi_id
        showProgress(true)
        if (b.plan_name=="Premium")
        {
            sessionManager!!.setBooleanValue(Constants.KeyIntent.IS_PREMIUM,true)
        }
        else
        {
            sessionManager!!.setBooleanValue(Constants.KeyIntent.IS_PREMIUM,false)
        }
        callApi(

            getRestApis().markascurrentbusiness(currentBusinessID,token!!), onApiSuccess = {
                sessionManager!!.setStringValue(KEY_FRAME_LIST, Gson().toJson(it.frameList))

                put(it.current_business, Constants.SharedPref.KEY_CURRENT_BUSINESS,this)
                showProgress(false)
                if (it.status!!)
                {
                    b.is_current_business=1
                }
                loadManageBusinessAllData()
                businessItemAdapter!!.notifyDataSetChanged()
            }, onApiError = {
                showProgress(false)

            }, onNetworkError = {
                showProgress(false)

            })
    }


}