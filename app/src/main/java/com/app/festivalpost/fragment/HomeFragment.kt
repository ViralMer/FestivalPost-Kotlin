package com.app.festivalpost.fragment

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.festivalpost.R
import com.app.festivalpost.activity.*
import com.app.festivalpost.adapter.*
import com.app.festivalpost.models.*
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.Constants.KeyIntent.CURRENT_DATE
import com.app.festivalpost.utils.Constants.KeyIntent.IS_PREMIUM
import com.app.festivalpost.utils.Constants.KeyIntent.LOG_OUT
import com.app.festivalpost.utils.Constants.SharedPref.IS_LOGGED_IN
import com.app.festivalpost.utils.Constants.SharedPref.KEY_CURRENT_BUSINESS
import com.app.festivalpost.utils.Constants.SharedPref.KEY_FRAME_LIST
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


@Suppress("DEPRECATION")
class HomeFragment : BaseFragment() {

    private var sliderArrayList = arrayListOf<AdvertsieItem?>()
    private var festivalArrayList = arrayListOf<HomePageItem?>()
    private var categoryArrayList = arrayListOf<HomePageItem?>()
    private var viewPager: ViewPager? = null
    private var rcvCustomFestival: RecyclerView? = null
    private var rcvCustomCategory: RecyclerView? = null
    private var linearFestival: LinearLayout? = null
    private var linearCategory: LinearLayout? = null
    private var tvPremium: TextView? = null
    private var tvCustom: TextView? = null
    private var tvviewall: TextView? = null
    private var sessionManager: SessionManager? = null


    var token: String? = null
    var businessDialogItemAdapter: BusinessDialogItemAdapter? = null
    var currentBusinessItemList = arrayListOf<CurrentBusinessItem?>()
    var rcvBusinessItem: RecyclerView? = null

    private var currentPage = 0
    private var NUM_PAGES = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        openAddImageDialog()
        sessionManager = SessionManager(activity!!)
        token = sessionManager!!.getValueString(Constants.SharedPref.USER_TOKEN)
        rcvCustomCategory = view.findViewById(R.id.customCategory)
        rcvCustomFestival = view.findViewById(R.id.customFestival)
        viewPager = view.findViewById(R.id.sliderviewPager)
        tvPremium = view.findViewById(R.id.tvPremium)
        tvCustom = view.findViewById(R.id.tvCustom)
        imageLogo1 = view.findViewById(R.id.imageLogo)
        tvviewall = view.findViewById(R.id.tvviewall)
        linearCategory = view.findViewById(R.id.linearCategory)
        linearFestival = view.findViewById(R.id.linearFestival)


        tvPremium!!.onClick {
            activity!!.launchActivity<PremiumActivity> { }
        }

        imageLogo1!!.onClick {
            activity!!.launchActivity<ManageBusinessActivity> {

            }
        }

        tvviewall!!.onClick {
            activity!!.launchActivity<FestivalViewAllActivitty> { }
        }

        tvCustom!!.onClick {
            val currentBusinessItem =
                get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS, activity!!)
            if (currentBusinessItem == null) {
                val materialAlertDialogBuilder = AlertDialog.Builder(activity!!)
                val inflater =
                    activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.custom_add_busines_dialog, null)
                val tvTitle: TextView
                val tvMessage: TextView
                val btnOk: Button
                val btnCancel: Button
                tvTitle = view.findViewById(R.id.tvTitle)
                tvMessage = view.findViewById(R.id.tvMessage)
                btnOk = view.findViewById(R.id.btnOk)
                btnCancel = view.findViewById(R.id.btnCancel)
                tvTitle.text = "Sorry!!"
                tvMessage.text = "For making post please add your business details first."
                materialAlertDialogBuilder.setView(view).setCancelable(true)
                val b = materialAlertDialogBuilder.create()
                btnCancel.setOnClickListener { b.dismiss() }
                btnOk.setOnClickListener { activity!!.launchActivity<AddBusinessActivity> { } }
                b.show()
            } else {
                activity!!.launchActivity<ChooseFrameActivityNew> { }
            }
        }


        loadHomePageData()


        // Auto start of viewpager

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            viewPager!!.setCurrentItem(currentPage++, true)


        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 5000)

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        override fun getCount(): Int {
            return sliderArrayList.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_slider, container, false)
            val ivphoto = view.findViewById<RoundedImageView>(R.id.imgSlider)
            val ivWhatsapp = view.findViewById<ImageView>(R.id.ivWhatsapp)
            val festivalItem = sliderArrayList[position]
            if (festivalItem!!.adv_image != null && !festivalItem.adv_image.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                Glide.with(activity!!).load(festivalItem.adv_image)
                    .error(R.drawable.placeholder_img).placeholder(
                        R.drawable.placeholder_img
                    ).into(ivphoto)

            }
            ivphoto.onClick {
                if (festivalItem.adv_link == "") {

                } else {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(festivalItem.adv_link)
                    activity!!.startActivity(i)
                }
            }

            if (festivalItem.adv_number == "") {
                ivWhatsapp.hide()
            } else {
                ivWhatsapp.show()
            }
            ivWhatsapp.onClick {
                if (festivalItem.adv_number != null) {
                    if (isPackageInstalled("com.whatsapp.w4b", activity!!.packageManager)) {
                        val url =
                            "https://api.whatsapp.com/send?phone=" + festivalItem.adv_number + "&text=Inquiry from Festival Post&source=&data=&app_absent="
                        try {
                            val pm = activity!!.packageManager
                            pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES)
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            i.putExtra(Intent.EXTRA_TEXT, "Inquiry from FestivalPost")
                            startActivity(i)
                        } catch (e: PackageManager.NameNotFoundException) {
                            Toast.makeText(
                                activity!!,
                                "Whatsapp Business app not installed in your phone",
                                Toast.LENGTH_SHORT
                            ).show()
                            e.printStackTrace()
                        }
                    } else if (isPackageInstalled("com.whatsapp", activity!!.packageManager)) {
                        val url =
                            "https://api.whatsapp.com/send?phone=" + festivalItem.adv_number + "&text=Inquiry from Festival Post&source=&data=&app_absent="
                        try {
                            val pm = activity!!.packageManager
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
                }
            }



            container.addView(view)
            return view
        }

        private fun isPackageInstalled(
            packagename: String,
            packageManager: PackageManager
        ): Boolean {
            return try {
                packageManager.getPackageGids(packagename)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }


    private fun openAddImageDialog() {
        if(Build.VERSION.SDK_INT == 30 ){
            Dexter.withContext(activity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION,
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            Log.d("Permission", "Permission Granted")
                        } else {
                            Log.d("Permission", "Permission Not Granted1")
                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val intent =
                                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                                startActivity(intent)
                            }*/
                            //showSettingsDialog()
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
        else {
            Dexter.withContext(activity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }


    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.cancel()
                openSettings()
            }
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.cancel()
            }
        })
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity!!.applicationContext.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }


    private fun loadHomePageData() {
        showProgress()
        try {
            callApi(
                getRestApis().getHomePageData(token!!),
                onApiSuccess = { res ->
                    hideProgress()
                    if (res.status!!) {
                        sessionManager!!.setBooleanValue(LOG_OUT, res.logout!!)
                        sessionManager!!.setBooleanValue(IS_PREMIUM, res.premium!!)
                        sessionManager!!.setStringValue(CURRENT_DATE, res.current_date!!)
                        sessionManager!!.setStringValue(KEY_FRAME_LIST, Gson().toJson(res.frameList))
                        if (res.current_business.status == "1") {
                            put(res.current_business, KEY_CURRENT_BUSINESS, activity!!)
                        } else {
                            put(null, KEY_CURRENT_BUSINESS, activity!!)
                        }


                        val currentBusinessItem =
                            get<CurrentBusinessItem>(KEY_CURRENT_BUSINESS, activity!!)
                        if (currentBusinessItem != null) {
                            if (currentBusinessItem!!.plan_name == "Free") {
                                tvPremium!!.show()
                            } else {
                                tvPremium!!.hide()
                            }
                            Glide.with(this).load(currentBusinessItem!!.busi_logo)
                                .placeholder(R.drawable.placeholder_img).error(
                                    R.drawable.placeholder_img
                                ).into(imageLogo!!)
                        }

                        sliderArrayList = res.slider
                        festivalArrayList = res.festival
                        categoryArrayList = res.cateogry

                        NUM_PAGES = sliderArrayList.size

                        rcvCustomFestival!!.layoutManager = LinearLayoutManager(
                            activity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        val customFestivalAdapter =
                            CustomFestivalItemAdapter(activity!!, festivalArrayList)
                        rcvCustomFestival!!.adapter = customFestivalAdapter


                        rcvCustomCategory!!.layoutManager = GridLayoutManager(activity, 3)
                        val customCategoryAdapter = CategoryItemAdapter(activity!!, categoryArrayList)
                        rcvCustomCategory!!.adapter = customCategoryAdapter

                        if (sliderArrayList.size > 0) {
                            viewPager!!.adapter = PagerAdapter()
                            dots.attachViewPager(viewPager)
                            dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)
                            PagerAdapter().notifyDataSetChanged()
                        }

                        viewPager!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                            ) {
                                currentPage = position
                            }

                            override fun onPageSelected(position: Int) {

                            }

                            override fun onPageScrollStateChanged(state: Int) {

                            }

                        })

                        if (festivalArrayList.size > 0) {
                            linearFestival!!.show()
                        } else {
                            linearFestival!!.hide()

                        }

                        if (categoryArrayList.size > 0) {
                            linearCategory!!.show()
                        } else {
                            linearCategory!!.hide()
                        }

                    } else {
                        if (res.message == "user not valid") {
                            val intent = Intent(
                                activity, LoginActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            activity!!.finish()
                            sessionManager!!.setBooleanValue(IS_LOGGED_IN, false)

                        }
                        linearFestival!!.hide()
                        linearCategory!!.hide()
                    }
                },
                onApiError = {
                    if (activity == null) return@callApi
                    hideProgress()
                    linearCategory!!.hide()
                    linearFestival!!.hide()

                },
                onNetworkError = {
                    if (activity == null) return@callApi
                    hideProgress()
                })
        }
        catch (e:Exception)
        {

        }

    }


    private fun showBusinessCategoryDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_business_dialog, null)
        rcvBusinessItem = layout.findViewById<View>(R.id.rcvManageBusiness) as RecyclerView

        businessDialogItemAdapter = BusinessDialogItemAdapter(activity!!, currentBusinessItemList)
        rcvBusinessItem!!.adapter = businessDialogItemAdapter
        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(true)
        alertDialog = builder.create()
        alertDialog!!.show()


    }

    override fun onResume() {
        super.onResume()
        try {
            val currentBusinessItem = get<CurrentBusinessItem>(KEY_CURRENT_BUSINESS, activity!!)
            if (currentBusinessItem != null) {
                if (currentBusinessItem!!.busi_logo != null) {
                    Glide.with(this).load(currentBusinessItem!!.busi_logo)
                        .placeholder(R.drawable.placeholder_img).error(R.drawable.placeholder_img)
                        .into(
                            imageLogo1!!
                        )
                    if (currentBusinessItem.plan_name == "Free") {
                        tvPremium!!.show()
                    } else {
                        tvPremium!!.hide()
                    }
                }

            }
        } catch (e: Exception) {

        }

    }


    companion object {
        var alertDialog: AlertDialog? = null
        var imageLogo1: AppCompatImageView? = null
    }


}