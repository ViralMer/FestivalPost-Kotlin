package com.app.festivalpost.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import com.app.festivalpost.BuildConfig
import com.app.festivalpost.R
import com.app.festivalpost.activity.LoginActivity
import com.app.festivalpost.activity.ManageBusinessActivity
import com.app.festivalpost.activity.MyPostActivity
import com.app.festivalpost.activity.WebBrowserActivity
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*

class AccountFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, null)
        val linearHelp=view.findViewById<View>(R.id.linearHelpSupport) as LinearLayout
        val linearMyBusiness=view.findViewById<View>(R.id.linearMyBusiness) as LinearLayout
        val linearMyPost=view.findViewById<View>(R.id.linearMyPost) as LinearLayout
        val linearShareus=view.findViewById<View>(R.id.linearShareus) as LinearLayout
        val linearRateus=view.findViewById<View>(R.id.linearRateus) as LinearLayout
        val layTerms=view.findViewById<View>(R.id.tvterms) as TextView
        val layPrivacy=view.findViewById<View>(R.id.tvPrivacy) as TextView
        val tvlogout=view.findViewById<View>(R.id.tvLogout) as TextView
        val tvusername=view.findViewById<View>(R.id.tvUserName) as TextView
        val tvusernaumber=view.findViewById<View>(R.id.tvUserNumber) as TextView


        linearShareus.onClick {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: java.lang.Exception) {
                //e.toString();
            }
        }
        linearRateus.onClick {
            val uri = Uri.parse("market://details?id=" + activity!!.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, "Couldn't launch the market", Toast.LENGTH_LONG).show()
            }
        }

        layTerms.setOnClickListener(View.OnClickListener {
            val titleval = resources.getString(R.string.txt_terms_condition)
            val url = "http://festivalpost.in/admin/termsandcondition"
            val detailact = Intent(
                activity,
                WebBrowserActivity::class.java
            )
            detailact.putExtra("title", titleval)
            detailact.putExtra("url", url)
            startActivity(detailact)
        })



        layPrivacy.setOnClickListener(View.OnClickListener {
            val titleval = resources.getString(R.string.txt_privacy_policy)
            val url = "http://festivalpost.in/admin/privacypolicy"
            val detailact = Intent(
                activity,
                WebBrowserActivity::class.java
            )
            detailact.putExtra("title", titleval)
            detailact.putExtra("url", url)
            startActivity(detailact)
        })



        for (i in 0 until getUserData().size )
        {
            getSharedPrefInstance().setValue(Constants.SharedPref.USER_NAME, getUserData()[i].name)
            getSharedPrefInstance().setValue(
                Constants.SharedPref.USER_NUMBER,
                getUserData()[i].mobile
            )
        }

        tvusername.text = getUserName()
        tvusernaumber.text = getMobileNumber()
        linearHelp.onClick {
            showPopupDialog(activity!!)
        }

        linearMyBusiness.onClick {
            activity!!.launchActivity<ManageBusinessActivity> {

            }
        }

        linearMyPost.onClick {
            activity!!.launchActivity<MyPostActivity> {

            }
        }





        tvlogout.onClick {
            performLogout()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun performLogout() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        builder.setTitle(activity!!.resources.getString(R.string.txt_logout_title))
            .setMessage(activity!!.resources.getString(R.string.txt_logout_message))
            .setPositiveButton(activity!!.resources.getString(R.string.txt_yes)) { dialog, which ->
                clearLoginPref()
                val detailAct = Intent(activity, LoginActivity::class.java)
                detailAct.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity!!.startActivity(detailAct)
                activity!!.finish()
            }
            .setNegativeButton(activity!!.resources.getString(R.string.txt_no)) { dialog, which -> dialog.dismiss() }
            .show()
    }


    private fun showPopupDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_help_support, null)

        val linearCall = layout.findViewById<View>(R.id.linearCall) as LinearLayout
        val linearWhatsapp = layout.findViewById<View>(R.id.linearWhatsapp) as LinearLayout
        val linearEmail = layout.findViewById<View>(R.id.linearemail) as LinearLayout
        val ibCancel = layout.findViewById<View>(R.id.ib_cancel) as AppCompatImageView


        val builder = AlertDialog.Builder(activity!!)
            .setView(layout)
            .setCancelable(true)

        val alertDialog = builder.create()

        linearEmail.onClick {
            val subject = resources.getString(R.string.txt_help_support)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constant.ADMIN_MAIL))
            startActivity(intent)
        }

        linearCall.onClick {
            val u = Uri.parse("tel:" + Constant.ADMIN_PHONE)
            val i = Intent(Intent.ACTION_DIAL, u)
            try {
                startActivity(i)
            } catch (s: SecurityException) {
                Toast.makeText(activity!!, s.message, Toast.LENGTH_LONG)
                    .show()
            }
        }

        linearWhatsapp.onClick {
            val url =
                "https://api.whatsapp.com/send?phone=918070794444&text=Inquiry from FestivalPost&source=&data=&app_absent="
            try {
                val pm = activity!!.applicationContext.packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                i.putExtra(Intent.EXTRA_TEXT, "Inquiry from FestivalPost")
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    activity!!,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }



        ibCancel.onClick {
            alertDialog.dismiss()
        }

        alertDialog.show()


    }


    private fun loadAccoutData() {
        showProgress()
        callApi(

            getRestApis().getProfile(), onApiSuccess = {
                hideProgress()

            }, onApiError = {
                hideProgress()

            }, onNetworkError = {
                hideProgress()

            })
    }
}