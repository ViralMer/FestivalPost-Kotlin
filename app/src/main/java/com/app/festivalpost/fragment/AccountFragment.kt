package com.app.festivalpost.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.app.festivalpost.BuildConfig
import com.app.festivalpost.R
import com.app.festivalpost.activity.*
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.utils.Constants.KeyIntent.CURRENT_DATE
import com.app.festivalpost.utils.Constants.KeyIntent.IS_PREMIUM
import com.app.festivalpost.utils.Constants.KeyIntent.LOG_OUT
import com.app.festivalpost.utils.Constants.SharedPref.IS_LOGGED_IN
import com.app.festivalpost.utils.Constants.SharedPref.KEY_CURRENT_BUSINESS
import com.app.festivalpost.utils.Constants.SharedPref.USER_EMAIL
import com.app.festivalpost.utils.Constants.SharedPref.USER_NAME
import com.app.festivalpost.utils.Constants.SharedPref.USER_NUMBER
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.app.festivalpost.utils.SessionManager
import com.arthenica.mobileffmpeg.Config.getPackageName
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.onClick
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class AccountFragment : BaseFragment() {

    var sessionManager: SessionManager? = null
    var reviewInfo: ReviewInfo? = null
    var manager: ReviewManager? = null
    private var appUpdateManager: AppUpdateManager?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, null)
        val linearHelp = view.findViewById<View>(R.id.linearHelpSupport) as LinearLayout
        val linearMyBusiness = view.findViewById<View>(R.id.linearMyBusiness) as LinearLayout
        val linearMyPost = view.findViewById<View>(R.id.linearMyPost) as LinearLayout
        val linearShareus = view.findViewById<View>(R.id.linearShareus) as LinearLayout
        val linearRateus = view.findViewById<View>(R.id.linearRateus) as LinearLayout
        val layTerms = view.findViewById<View>(R.id.tvterms) as TextView
        val layPrivacy = view.findViewById<View>(R.id.tvPrivacy) as TextView
        val tvlogout = view.findViewById<View>(R.id.tvLogout) as TextView
        val tvusername = view.findViewById<View>(R.id.tvUserName) as TextView
        val tvusernaumber = view.findViewById<View>(R.id.tvUserNumber) as TextView
        val iv_edit = view.findViewById<View>(R.id.iv_edit) as AppCompatImageView

        sessionManager = SessionManager(activity!!)
        initializeFirebase()
        appUpdateManager = AppUpdateManagerFactory.create(activity!!)
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // For a flexible update, use AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                // Request the update.
                appUpdateManager!!.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    activity!!,
                    // Include a request code to later monitor this update request.
                    123)
            }
        }






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

        checkUpdate()


        linearRateus.onClick {
            val uri = Uri.parse("market://details?id=" + activity!!.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, "Couldn't launch the market", Toast.LENGTH_LONG).show()
            }


            ///////////********** In App Review Code ***********////////////////////
            /*manager= ReviewManagerFactory.create(activity!!)
            val request = manager!!.requestReviewFlow()
            request.addOnCompleteListener { request1 ->
                if (request.isSuccessful) {
                    // We got the ReviewInfo object
                    val reviewInfo = request1.result
                    val flow = manager!!.launchReviewFlow(activity!!, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }
                } else {
                    // There was some problem, continue regardless of the result.
                }
            }*/
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


        tvusername.text = sessionManager!!.getValueString(USER_NAME)
        tvusernaumber.text = sessionManager!!.getValueString(USER_NUMBER)
        linearHelp.onClick {
            showPopupDialog(activity!!)
        }

        linearMyBusiness.onClick {
            activity!!.launchActivity<ManageBusinessActivity> {

            }
        }

        iv_edit.onClick { activity!!.launchActivity<EditProfileActivity> { } }

        linearMyPost.onClick {
            activity!!.launchActivity<MyPostActivity> {

            }
        }





        tvlogout.onClick {
            performLogout()
        }


        return view
    }
    fun initializeFirebase() {
        if (FirebaseApp.getApps(activity!!).isEmpty()) {
            FirebaseApp.initializeApp(activity!!, FirebaseOptions.fromResource(activity!!)!!)
        }
        val config = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        config.setConfigSettings(configSettings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager?.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        Log.d("TAG", "Checking for updates")
        appUpdateInfoTask?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                Log.d("TAG", "Update available")
            } else {
                Log.d("TAG", "No Update available")
            }
        }
    }


    private fun performLogout() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!)
        builder.setTitle(activity!!.resources.getString(R.string.txt_logout_title))
            .setMessage(activity!!.resources.getString(R.string.txt_logout_message))
            .setPositiveButton(activity!!.resources.getString(R.string.txt_yes)) { dialog, which ->
                sessionManager!!.removeValue(USER_NAME)
                sessionManager!!.removeValue(USER_NUMBER)
                sessionManager!!.removeValue(KEY_CURRENT_BUSINESS)
                sessionManager!!.removeValue(CURRENT_DATE)
                sessionManager!!.removeValue(USER_EMAIL)
                sessionManager!!.removeValue(IS_LOGGED_IN)
                sessionManager!!.removeValue(USER_TOKEN)
                sessionManager!!.removeValue(IS_PREMIUM)
                sessionManager!!.removeValue(LOG_OUT)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                // If the update is cancelled or fails, you can request to start the update again.
                Log.e("TAG", "Update flow failed! Result code: $resultCode")
            }
        }
    }



}
