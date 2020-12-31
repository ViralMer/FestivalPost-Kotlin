package com.app.festivalpost.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper.getMainLooper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.festivalpost.R
import com.app.festivalpost.activity.*
import com.app.festivalpost.adapter.*
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.Constants.KeyIntent.CURRENT_DATE
import com.app.festivalpost.utils.Constants.KeyIntent.IS_PREMIUM
import com.app.festivalpost.utils.Constants.KeyIntent.LOG_OUT
import com.app.festivalpost.utils.Constants.SharedPref.IS_LOGGED_ID
import com.app.festivalpost.utils.Constants.SharedPref.IS_LOGGED_IN
import com.app.festivalpost.utils.Constants.SharedPref.KEY_CURRENT_BUSINESS
import com.app.festivalpost.utils.Constants.SharedPref.KEY_FRAME_LIST
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.emegamart.lelys.utils.extensions.*
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


@Suppress("DEPRECATION")
class HomeFragment : BaseFragment() {

    private var sliderArrayList = arrayListOf<AdvertsieItem?>()
    private var festivalArrayList = arrayListOf<HomePageItem?>()
    private var categoryArrayList = arrayListOf<HomePageItem?>()
    private var viewPager: ViewPager? = null
    private var rcvCustomFestival: RecyclerView? = null
    private var rcvCustomCategory: RecyclerView? = null
    private var tvPremium: TextView? = null
    private var tvCustom: TextView? = null
    private var tvviewall: TextView? = null

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openAddImageDialog()

        rcvCustomCategory = view.findViewById(R.id.customCategory)
        rcvCustomFestival = view.findViewById(R.id.customFestival)

        viewPager = view.findViewById(R.id.sliderviewPager)
        tvPremium = view.findViewById(R.id.tvPremium)
        tvCustom = view.findViewById(R.id.tvCustom)
        imageLogo1 = view.findViewById(R.id.imageLogo)
        tvviewall = view.findViewById(R.id.tvviewall)


        tvPremium!!.onClick {
            activity!!.launchActivity<PremiumActivity> {  }
        }

        imageLogo!!.onClick {
            activity!!.launchActivity<ManageBusinessActivity> {

            }
        }

        tvviewall!!.onClick {
            activity!!.launchActivity<FestivalViewAllActivitty> {  }
        }

        tvCustom!!.onClick {
            val currentBusinessItem =
                get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS)
            if (currentBusinessItem == null) {
                val materialAlertDialogBuilder = AlertDialog.Builder(activity!!)
                val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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
                btnOk.setOnClickListener { activity!!.launchActivity<AddBusinessActivity> {  } }
                b.show()
            } else {
                activity!!.launchActivity<ChooseFrameActivityNew> { }
            }
        }

        val mainHandler = Handler(getMainLooper())
        val runnable= Runnable { loadHomePageData() }

        mainHandler.postDelayed(runnable, 0)


        NUM_PAGES = sliderArrayList.size

        // Auto start of viewpager

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === NUM_PAGES) {
                currentPage = 0
                
            }
            viewPager!!.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)


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
                Glide.with(activity!!).load(festivalItem.adv_image).error(R.drawable.placeholder_img).placeholder(
                    R.drawable.placeholder_img
                ).into(ivphoto)

            }
            ivphoto.onClick {

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(festivalItem.adv_link)
                activity!!.startActivity(i)
            }

            ivWhatsapp.onClick {
                val url =
                    "https://api.whatsapp.com/send?phone="+festivalItem.adv_number+"&text=Inquiry from FestivalPost&source=&data=&app_absent="
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



            container.addView(view)
            return view
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }


    private fun openAddImageDialog() {
        Dexter.withContext(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
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


    private fun loadHomePageData() {
        showProgress()
        callApi(
            getRestApis().getHomePageData(),
            onApiSuccess = { res ->
                hideProgress()
                if (res.status!!) {


                    getSharedPrefInstance().setValue(LOG_OUT, res.logout)

                    getSharedPrefInstance().setValue(IS_PREMIUM, res.premium)
                    getSharedPrefInstance().setValue(CURRENT_DATE, res.current_date)
                    getSharedPrefInstance().setValue(KEY_FRAME_LIST, Gson().toJson(res.frameList))
                    if (res.current_business.status=="1") {
                        put(res.current_business, KEY_CURRENT_BUSINESS)
                    }
                    else{
                        put(null, KEY_CURRENT_BUSINESS)
                    }


                    val currentBusinessItem = get<CurrentBusinessItem>(KEY_CURRENT_BUSINESS)
                    if (currentBusinessItem!=null) {
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








                    Log.d(
                        "GetApiToken",
                        getApiToken() + "    122" + res.frameList.size + " 123" + getCustomFrameList().size
                    )


                    sliderArrayList = res.slider
                    festivalArrayList = res.festival
                    categoryArrayList = res.cateogry

                    rcvCustomFestival!!.layoutManager = LinearLayoutManager(
                        activity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    val customFestivalAdapter =
                        CustomFestivalItemAdapter(activity!!, festivalArrayList)
                    rcvCustomFestival!!.adapter = customFestivalAdapter

                    rcvCustomCategory!!.layoutManager = GridLayoutManager(activity, 3)
                    val customCategoryAdapter =
                        CategoryItemAdapter(activity!!, categoryArrayList)
                    rcvCustomCategory!!.adapter = customCategoryAdapter

                    if (sliderArrayList.size > 0) {
                        viewPager!!.adapter = PagerAdapter()
                        dots.attachViewPager(viewPager)
                        dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)
                        PagerAdapter().notifyDataSetChanged()
                    }

                    if (festivalArrayList.size > 0) {
                        linearFestival.show()
                    } else {
                        linearFestival.hide()

                    }

                    if (categoryArrayList.size > 0) {
                        linearCategory.show()
                    } else {
                        linearCategory.hide()
                    }






                    Glide.with(this).asBitmap().load(res.current_business.busi_logo)
                        .into(object : CustomTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val root = Environment.getExternalStorageDirectory().absolutePath
                                val myDir = File("$root/Imagename")
                                myDir.mkdirs()
                                myDir.mkdir()
                                val fname = "logo.png"
                                val file = File(myDir, fname)
                                if (file.exists()) file.delete()
                                try {
                                    val out = FileOutputStream(file)
                                    resource.compress(Bitmap.CompressFormat.PNG, 100, out)
                                    out.flush()
                                    out.close()
                                } catch (e: Exception) {
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })


                    /* if (res.logout!!) {
                        val detailAct = Intent(
                            activity, LoginActivity::class.java
                        )
                        detailAct.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        activity!!.startActivity(detailAct)
                        activity!!.finish()
                    }*/


                } else {
                    if (res.message == "user not valid") {
                        val intent = Intent(
                            activity, LoginActivity::class.java
                        )
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity!!.finish()
                        getSharedPrefInstance().setValue(IS_LOGGED_IN,false)
                    }
                    linearFestival.hide()
                    linearCategory.hide()
                }
            },
            onApiError = {
                if (activity == null) return@callApi
                hideProgress()
                linearCategory.hide()
                linearFestival.hide()

            },
            onNetworkError = {
                if (activity == null) return@callApi
                hideProgress()
            })
    }

    private fun loadManageBusinessAllData()
    {
        showProgress()
        callApi(
            getRestApis().getAllMyBusiness(), onApiSuccess = {
                hideProgress()
                currentBusinessItemList = it.data
                showBusinessCategoryDialog(activity!!)


            }, onApiError = {
                hideProgress()


            }, onNetworkError = {
                hideProgress()


            })
    }

    private fun showBusinessCategoryDialog(context: Context) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_business_dialog, null)
        rcvBusinessItem = layout.findViewById<View>(R.id.rcvManageBusiness) as RecyclerView

        businessDialogItemAdapter= BusinessDialogItemAdapter(activity!!, currentBusinessItemList)
        rcvBusinessItem!!.adapter=businessDialogItemAdapter
        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(true)
        alertDialog = builder.create()
        alertDialog!!.show()




    }

    override fun onResume() {
        super.onResume()
        try {
            val currentBusinessItem = get<CurrentBusinessItem>(KEY_CURRENT_BUSINESS)
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
        }
        catch (e:Exception)
        {

        }

    }








    companion object{
        var alertDialog: AlertDialog? = null
        var imageLogo1: AppCompatImageView? = null
    }




}