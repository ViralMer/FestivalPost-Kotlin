package com.app.festivalpost.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log

import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView

import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R

import com.app.festivalpost.adapter.FontTypeAdapter
import com.app.festivalpost.adapter.FrameChooseAdapter

import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.app.festivalpost.photoeditor.OnPhotoEditorListener
import com.app.festivalpost.photoeditor.PhotoEditor
import com.app.festivalpost.photoeditor.PhotoEditorView
import com.app.festivalpost.photoeditor.ViewType
import com.app.festivalpost.utility.MultiTouchListenerNew
import com.app.festivalpost.utility.MultiTouchListenerNewNotRotate
import com.app.festivalpost.utility.MultiTouchListenerNotMoveble
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.getError
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.getFilePath
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.with
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class CustomPhotoFrameActivity : AppBaseActivity(), OnItemClickListener, FontOnItemClickListener,
    ColorPickerDialogListener {

    private var storeValue: String? = ""
    private var photoEditorView: PhotoEditorView? = null
    private var linearAddText: LinearLayout? = null
    private var linearTextcolor: LinearLayout? = null
    private var linearfonttype: LinearLayout? = null
    private var linearAddimage: LinearLayout? = null
    var views: MutableList<ViewData> = ArrayList()
    var layroot: LinearLayout? = null
    var mPhotoEditor: PhotoEditor? = null
    var profilePath: String? = ""
    var selected_color = Color.BLACK
    var textselected_color = Color.BLACK
    var rootTextView: View? = null
    var selectedFontTypeface: Typeface? = null
    var textselectedFontTypeface: Typeface? = null
    var phoneLine: TextView? = null
    var websiteLine: TextView? = null
    var ivbackground: ImageView? = null
    var selectedPosition = 0
    var llwatermark: LinearLayout? = null
    var tempEnteredText: String? = null
    var i = 0
    var plus = 0
    var index1 = 0
    var ivcall1: ImageView? = null
    var tvframephone1: TextView? = null
    var frameLayout: FrameLayout? = null
    var textView: TextView? = null
    var frameBorder: FrameLayout? = null
    var mainframeBorder: FrameLayout? = null
    var imageView: ImageView? = null
    var backpressed = true
    var photo_path: String? = ""
    var mask: Bitmap? = null
    var result: Bitmap? = null
    var original: Bitmap? = null
    var imageview_id: ImageView? = null
    var mFrameIv: ImageView? = null
    var mMovImage: ImageView? = null
    var frame_lot: FrameLayout? = null
    var llframe: LinearLayout? = null
    var frameContentItemDetail: CustomCategoryPostItem? = null
    var textviewSelected = true
    var ivframelogo: ImageView? = null
    var ivframebg: ImageView? = null
    var ivcall: ImageView? = null
    var ivEmail: ImageView? = null
    var ivWebsite: ImageView? = null
    var ivLocation: ImageView? = null
    var ivphoneselect: ImageView? = null
    var ivemailselect: ImageView? = null
    var ivwebsiteselect: ImageView? = null
    var ivlogoselect: ImageView? = null
    var ivaddressselect: ImageView? = null
    var ivnameSelect: ImageView? = null
    var ivphoneclose: ImageView? = null
    var ivemailclose: ImageView? = null
    var ivwebsiteclose: ImageView? = null
    var ivaddressclose: ImageView? = null
    var ivphotoclose: ImageView? = null
    var ivnameClose: ImageView? = null
    var tvframephone: TextView? = null
    var tvframeemail: TextView? = null
    var tvframeweb: TextView? = null
    var tvframelocation: TextView? = null
    var tvframename: TextView? = null
    var linearPhone: LinearLayout? = null
    var linearEmail: LinearLayout? = null
    var linearWebsite: LinearLayout? = null
    var linearAddress: LinearLayout? = null
    var linearLogo: LinearLayout? = null
    var linearName: LinearLayout? = null
    var framePhone: FrameLayout? = null
    var frameEmail: FrameLayout? = null
    var frameWebsite: FrameLayout? = null
    var frameAddress: FrameLayout? = null
    var frameLogo: FrameLayout? = null
    var frameName: FrameLayout? = null
    var phoneValue = false
    var emailValue = false
    var websiteValue = false
    var addressValue = false
    var textallSelected = false
    var nameValue = false
    var phoneTypeface: Typeface? = null
    var emailTypeface: Typeface? = null
    var websiteTypeface: Typeface? = null
    var addressTypeface: Typeface? = null
    var allfonttypeface: Typeface? = null
    var nametypeface: Typeface? = null
    var phoneselected_color = 0
    var emailselected_color = 0
    var websiteselected_color = 0
    var addressselected_color = 0
    var allselectedcolor = 0
    var namecolor = Color.BLACK
    var localFrameItemNew: LocalFrameItemNew? = null
    var framePreview: FramePreview? = null
    var status = false
    var message = ""
    var height = 0
    var width = 0
    var iWidht = 0
    var iHeight = 0
    var imageTopwidth = 0
    var imageLeftwidth = 0
    var textTopwidth = 0
    var textLeftWidht = 0
    var texttop = 0
    var textleft = 0
    var imageleft = 0
    var imageTop = 0
    var imageWidth = 0
    var imageHeight = 0
    var framePreviewArrayList = ArrayList<FramePreview>()
    var recyclerView: RecyclerView? = null
    var fontTypeAdapter: FontTypeAdapter? = null
    var fontTypeList = arrayListOf<FontTypeList?>()
    var rcvFont: RecyclerView? = null
    var alertDialog: AlertDialog? = null
    var sessionManager: SessionManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        setContentView(R.layout.activity_custom_photo_frame)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            Log.d("AppCrash", "" + thread.toString())
            Log.d("AppCrash1", "" + e.message.toString())
        }
        sessionManager=SessionManager(this)
        setActionbar()
        selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        textselectedFontTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        phoneTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        emailTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        websiteTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        addressTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        allfonttypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        /*        horizontalScrollView = findViewById(R.id.horizontal);

        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        });*/
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("photo_path")) {
                photo_path = bundle["photo_path"] as String?
            }
            if (bundle.containsKey("frame_contact_detail")) {
                frameContentItemDetail = bundle["frame_contact_detail"] as CustomCategoryPostItem?
            }
        }
        ivphoneselect = findViewById(R.id.ivMobileSelected)
        ivemailselect = findViewById(R.id.ivEmailSelected)
        ivaddressselect = findViewById(R.id.ivAddressSelected)
        ivwebsiteselect = findViewById(R.id.ivWebsiteSelected)
        ivlogoselect = findViewById(R.id.ivLogoSelected)
        ivnameSelect = findViewById(R.id.ivNameSelected)
        imageview_id = findViewById<View>(R.id.imageview_id) as ImageView
        mMovImage = findViewById<View>(R.id.iv_mov) as ImageView
        mFrameIv = findViewById<View>(R.id.mFrameIv) as ImageView
        //frame_lot = findViewById(R.id.frame_lot);
        llframe = findViewById<View>(R.id.llframe) as LinearLayout
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        llwatermark = findViewById<View>(R.id.llwatermark) as LinearLayout
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        frameLayout = findViewById(R.id.frameLayout)
        recyclerView = findViewById(R.id.rvdata)
        photoEditorView = findViewById<View>(R.id.photoEditorView) as PhotoEditorView
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView!!)
            .setPinchTextScalable(true)
            .build()
        linearAddText = findViewById<View>(R.id.linearAddText) as LinearLayout
        linearTextcolor = findViewById<View>(R.id.lineartextcolor) as LinearLayout
        linearfonttype = findViewById<View>(R.id.linearFonttype) as LinearLayout
        linearAddimage = findViewById<View>(R.id.linearAddimage) as LinearLayout
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
  /*      val params=layroot!!.layoutParams

        params.height=width
        params.width=width
  */      /*val params1=llframe!!.layoutParams
        params1.height=width
        params1.width=width
*/
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        Log.d("Phot_PATH",""+photo_path!!)
        if (photo_path != null) {
            Glide.with(this@CustomPhotoFrameActivity).load(photo_path).into(imageview_id!!)
        }
        frameLayout!!.setOnClickListener(View.OnClickListener {
            linearLogo!!.setBackgroundResource(0)
            linearEmail!!.setBackgroundResource(0)
            linearAddress!!.setBackgroundResource(0)
            linearPhone!!.setBackgroundResource(0)
            linearWebsite!!.setBackgroundResource(0)
            linearName!!.setBackgroundResource(0)
            ivphoneclose!!.visibility = View.GONE
            ivphotoclose!!.visibility = View.GONE
            ivemailclose!!.visibility = View.GONE
            ivwebsiteclose!!.visibility = View.GONE
            ivaddressclose!!.visibility = View.GONE
            ivnameClose!!.visibility = View.GONE
            emailValue = false
            phoneValue = false
            websiteValue = false
            addressValue = false
            textallSelected = true
            nameValue = false
            textviewSelected = false
        })



        var frameListItems1 = arrayListOf<FrameListItem1>()
        frameListItems1 = getCustomFrameList(this)
        Log.d("framesize", "" + getCustomFrameList(this).size)
        plus += frameListItems1.size
        for (i in frameListItems1.indices) {
            framePreviewArrayList.add(
                FramePreview(
                    R.layout.custom_frame_layout_dynamic,
                    frameListItems1[i].frame_url
                )
            )
        }
        Log.d("FrmaeSize", "" + framePreviewArrayList.size)
        try {
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_48, "frame_48.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_47, "frame_47.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_46, "frame_46.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_45, "frame_45.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_44, "frame_44.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_43, "frame_43.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_35, "frame_35.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_36, "frame_36.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_37, "frame_37.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_38, "frame_38.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_39, "frame_39.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_40, "frame_40.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_41, "frame_41.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_42, "frame_42.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_33, "frame_33.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_34, "frame_34.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_17,"frame_17.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_18,"frame_18.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_19,"frame_19.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_20,"frame_20.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_21,"frame_21.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_1,"frame_01.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_3,"frame_03.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_4,"frame_04.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_5,"frame_05.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_6,"frame_06.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_7,"frame_07.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_8,"frame_08.png"))
            /*framePreviewArrayList.add(FramePreview(R.layout.custom_frame_9,"frame_01.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_11,"frame_03.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_12,"frame_04.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_13,"frame_05.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_14,"frame_06.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_15,"frame_07.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_16,"frame_08.png"))*/

        } catch (e: OutOfMemoryError) {


        } catch (e: Exception) {
        }
        val frameChooseAdapter = FrameChooseAdapter(this, framePreviewArrayList)
        val horizontalLayoutManagaer = LinearLayoutManager(
            this@CustomPhotoFrameActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView!!.setLayoutManager(horizontalLayoutManagaer)
        recyclerView!!.setAdapter(frameChooseAdapter)
        //setFrameNEW(framePreviewArrayList[0])
        if (getCustomFrameList(this).isNotEmpty()) {
            val photoItem=framePreviewArrayList[0]
            setFrameNEW(framePreviewArrayList[0])
            if (photoItem.dynamic_images != null && !photoItem.dynamic_images.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                //setFrameNEW(photoItem);
                Glide.with(this@CustomPhotoFrameActivity).load(photoItem.dynamic_images).into(ivframebg!!)
            }

        }
        else{
            try {
                setFrameNEW(framePreviewArrayList[0])
            }catch (e: OutOfMemoryError) {
                alertDialog!!.show()
            } catch (e: Exception) {
            }

        }
        showProgress(true)
        runDelayedOnUiThread(1000) {

            if (frameContentItemDetail != null) {
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                height = displayMetrics.heightPixels
                width = displayMetrics.widthPixels
                val ttop = frameContentItemDetail!!.position_y!!.toInt()
                val tleft = frameContentItemDetail!!.position_x!!.toInt()
                val itop = frameContentItemDetail!!.img_position_y!!.toInt()
                val ileft = frameContentItemDetail!!.img_position_x!!.toInt()
                val iwidth1 = frameContentItemDetail!!.img_width!!.toInt()
                val iheight1 = frameContentItemDetail!!.img_height!!.toInt()
                Log.d("1234666545", "ttop :$ttop tleft :$tleft")
                textTopwidth = width * ttop //213
                imageTopwidth = width * itop //80
                textLeftWidht = width * tleft //115
                imageLeftwidth = width * ileft //105
                iWidht = width * iwidth1
                iHeight = width * iheight1
                texttop = textTopwidth / 300
                imageTop = imageTopwidth / 300
                textleft = textLeftWidht / 300
                imageleft = imageLeftwidth / 300
                imageHeight = iHeight / 300
                imageWidth = iWidht / 300
                makeMaskImage(
                    imageview_id,
                    frameContentItemDetail!!.banner_image,
                    frameContentItemDetail!!.banner_image,
                    textleft,
                    texttop
                )
                val imgparams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )

                imgparams.topMargin = imageTop
                imgparams.leftMargin = imageleft
                mMovImage!!.layoutParams = imgparams
                mMovImage!!.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24_new)
                showProgress(false)
            }
        }


        //AsyncTaskExampleNew().execute();
        val multiTouchListenerNew = MultiTouchListenerNew()
        multiTouchListenerNew.setOnGestureControl(object : MultiTouchListenerNew.OnGestureControl {
            override fun onClick() {
                openAddImageDialog()
            }

            override fun onLongClick() {}
            override fun onTouch() {}
        })
        mMovImage!!.setOnTouchListener(multiTouchListenerNew)
        framePreviewArrayList[0].isIs_selected=true
        mPhotoEditor!!.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onEditTextChangeListener(
                rootView: View?,
                text: String?,
                colorCode: Int,
                viewList: Int
            ) {
                for (i in views.indices) {
                    if (i == viewList) {
                        rootTextView = rootView
                        showAddTextDialog(text, colorCode)
                        break
                    }
                }
            }

            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
                backpressed = false
            }

            override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
                if (numberOfAddedViews == 0) {
                    i = 0
                    views.clear()
                    selectedPosition = 0

                    backpressed = true
                } else {
                    i -= 1
                    views.removeAt(selectedPosition)
                    selectedPosition = i - 1

                    backpressed = false
                }
            }

            override fun onStartViewChangeListener(
                viewType: ViewType?,
                numberOfAddedViews: Int,
                view: View?
            ) {
                selectedPosition = numberOfAddedViews
                emailValue = false
                phoneValue = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = false
                textviewSelected = true

            }

            override fun onStopViewChangeListener(
                viewType: ViewType?,
                numberOfAddedViews: Int,
                view: View?
            ) {
            }
        })

        linearAddimage!!.setOnClickListener { openAddImageDialog() }
        linearAddText!!.setOnClickListener {
            rootTextView = null
            showAddTextDialog("", selected_color)
        }
        linearTextcolor!!.setOnClickListener { ColorPickerDialog.newBuilder().show(this) }
        linearfonttype!!.setOnClickListener {
            var defaultText = ""
            if (defaultText.equals("", ignoreCase = true)) {
                defaultText = storeValue!!
            }
            fontTypeList.clear()
            fontTypeList.add(FontTypeList("fonts/aileron_light.otf"))
            fontTypeList.add(FontTypeList("fonts/avenir_book.otf"))
            fontTypeList.add(FontTypeList("fonts/breeserif_regular.otf"))
            fontTypeList.add(FontTypeList("fonts/canter_light.otf"))
            fontTypeList.add(FontTypeList("fonts/crimsontext_regular.ttf"))
            fontTypeList.add(FontTypeList("fonts/dense_regular.otf"))
            fontTypeList.add(FontTypeList("fonts/himalaya_regular.ttf"))
            fontTypeList.add(FontTypeList("fonts/opensans_regular.ttf"))
            fontTypeList.add(FontTypeList("fonts/sf_ui_regular.otf"))
            fontTypeList.add(FontTypeList("fonts/vollkorn_regular.ttf"))
            storeValue = "Festival Post"
            showPopupBusinessCategoryDialog(this, storeValue!!)
        }
        ivlogoselect!!.setOnClickListener(View.OnClickListener {
            if (ivlogoselect!!.drawable.constantState === resources.getDrawable(R.drawable.logo_select).constantState) {
                ivlogoselect!!.setImageResource(R.drawable.logo_deselect)
                linearLogo!!.setBackgroundResource(0)
                linearLogo!!.visibility = View.GONE
                ivframelogo!!.visibility = View.GONE
                frameLogo!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            } else {
                ivlogoselect!!.setImageResource(R.drawable.logo_select)
                linearLogo!!.setBackgroundResource(0)
                linearLogo!!.visibility = View.VISIBLE
                ivframelogo!!.visibility = View.VISIBLE
                frameLogo!!.visibility = View.VISIBLE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            }
        })
        ivemailselect!!.setOnClickListener(View.OnClickListener {
            if (ivemailselect!!.drawable.constantState === resources.getDrawable(R.drawable.email_select).constantState) {
                ivemailselect!!.setImageResource(R.drawable.email_deselect)

                linearEmail!!.setBackgroundResource(0)
                linearEmail!!.visibility = View.GONE
                ivEmail!!.visibility = View.GONE
                phoneLine!!.visibility = View.GONE
                tvframeemail!!.visibility = View.GONE
                frameEmail!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            } else {
                ivemailselect!!.setImageResource(R.drawable.email_select)
                linearEmail!!.setBackgroundResource(0)
                linearEmail!!.visibility = View.VISIBLE

                tvframeemail!!.visibility = View.VISIBLE
                frameEmail!!.visibility = View.VISIBLE
                if (index1 == 6 + plus) {
                    phoneLine!!.visibility = View.GONE
                    websiteLine!!.visibility=View.GONE
                    ivEmail!!.visibility = View.VISIBLE
                    linearEmail!!.hide()
                    frameEmail!!.hide()
                }
                else if (index1 == 14 + plus) {
                    phoneLine!!.visibility = View.GONE
                    websiteLine!!.visibility=View.GONE
                    ivEmail!!.visibility = View.VISIBLE
                } else if (index1 == 15 + plus) {
                    phoneLine!!.visibility = View.GONE;
                    websiteLine!!.visibility=View.GONE
                    ivEmail!!.visibility = View.VISIBLE
                }
                else if (index1 == 16 + plus) {
                    ivEmail!!.visibility = View.GONE
                    phoneLine!!.visibility = View.GONE
                } else if (index1 == 17 + plus) {
                    phoneLine!!.setVisibility(View.VISIBLE);
                    ivEmail!!.visibility = View.VISIBLE

                } else if (index1 == 18 + plus) {
                    phoneLine!!.setVisibility(View.VISIBLE);
                    ivEmail!!.visibility = View.VISIBLE

                } else {
                    phoneLine!!.visibility = View.GONE
                    ivEmail!!.visibility = View.VISIBLE
                }

                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            }
        })
        ivphoneselect!!.setOnClickListener(View.OnClickListener {
            if (ivphoneselect!!.drawable.constantState === resources.getDrawable(R.drawable.mobile_select).constantState) {
                ivphoneselect!!.setImageResource(R.drawable.mobile_deselect)

                linearPhone!!.visibility = View.GONE
                linearPhone!!.setBackgroundResource(0)
                ivcall!!.visibility = View.GONE
                tvframephone!!.visibility = View.GONE
                framePhone!!.visibility = View.GONE

                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            } else {

                ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                linearPhone!!.visibility = View.VISIBLE
                linearPhone!!.setBackgroundResource(0)
                ivcall!!.visibility = View.VISIBLE
                tvframephone!!.visibility = View.VISIBLE
                framePhone!!.visibility = View.VISIBLE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            }
        })
        ivwebsiteselect!!.setOnClickListener(View.OnClickListener {
            if (ivwebsiteselect!!.drawable.constantState === resources.getDrawable(R.drawable.website_select).constantState) {
                ivwebsiteselect!!.setImageResource(R.drawable.website_deselect)
                linearWebsite!!.visibility = View.GONE
                linearWebsite!!.setBackgroundResource(0)
                ivWebsite!!.visibility = View.GONE
                websiteLine!!.visibility = View.GONE
                tvframeweb!!.visibility = View.GONE
                frameWebsite!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            } else {
                ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                linearWebsite!!.visibility = View.VISIBLE
                tvframeweb!!.visibility = View.VISIBLE
                frameWebsite!!.visibility = View.VISIBLE
                linearWebsite!!.setBackgroundResource(0)
                if (index1 == 6 + plus) {
                    ivWebsite!!.visibility = View.VISIBLE
                    websiteLine!!.hide()
                    frameWebsite!!.hide()
                    linearWebsite!!.hide()
                }
                else if (index1 == 14 + plus) {
                    ivWebsite!!.visibility = View.VISIBLE
                    websiteLine!!.hide()
                } else if (index1 == 15 + plus) {
                    websiteLine!!.setVisibility(View.GONE);
                    ivWebsite!!.visibility = View.GONE
                    linearWebsite!!.visibility = View.GONE
                }else if (index1 == 16 + plus) {

                    websiteLine!!.setVisibility(View.GONE);
                    ivWebsite!!.visibility = View.GONE
                } else if (index1 == 17 + plus) {
                    websiteLine!!.visibility = View.GONE
                    ivWebsite!!.visibility = View.GONE
                } else if (index1 == 18 + plus) {
                    websiteLine!!.visibility = View.GONE
                    ivWebsite!!.visibility = View.GONE
                } else {
                    websiteLine!!.visibility = View.GONE
                    ivWebsite!!.visibility = View.VISIBLE
                }
                //ivWebsite!!.visibility = View.VISIBLE

                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            }
        })
        ivaddressselect!!.setOnClickListener(View.OnClickListener {
            if (ivaddressselect!!.drawable.constantState === resources.getDrawable(R.drawable.location_select).constantState) {
                ivaddressselect!!.setImageResource(R.drawable.location_deselect)

                linearAddress!!.visibility = View.GONE
                linearAddress!!.setBackgroundResource(0)
                ivLocation!!.visibility = View.GONE
                tvframelocation!!.visibility = View.GONE
                frameAddress!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            } else {
                ivaddressselect!!.setImageResource(R.drawable.location_select)
                if (index1 == 12 + plus) {
                    linearAddress!!.hide()
                    ivLocation!!.visibility = View.GONE
                } else {
                    ivLocation!!.visibility = View.VISIBLE
                }
                linearAddress!!.setBackgroundResource(0)
                linearAddress!!.visibility = View.VISIBLE

                tvframelocation!!.visibility = View.VISIBLE
                frameAddress!!.visibility = View.VISIBLE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            }
        })
        ivnameSelect!!.setOnClickListener(View.OnClickListener {
            if (ivnameSelect!!.drawable.constantState === resources.getDrawable(R.drawable.name_select).constantState) {
                ivnameSelect!!.setImageResource(R.drawable.name_deselect)

                frameName!!.visibility = View.GONE
                linearName!!.visibility = View.GONE
                linearName!!.setBackgroundResource(0)
                tvframename!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            } else {
                ivnameSelect!!.setImageResource(R.drawable.name_select)

                linearName!!.visibility = View.VISIBLE
                tvframename!!.visibility = View.VISIBLE
                frameName!!.visibility = View.VISIBLE
                linearName!!.setBackgroundResource(0)
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            }
        })
    }



    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@CustomPhotoFrameActivity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }


    fun openAddImageDialog() {
        Dexter.withContext(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val builder = AlertDialog.Builder(this@CustomPhotoFrameActivity)
                        builder.setTitle("Choose Photo")
                            val animals = arrayOf( "Camera", "Gallery")
                        builder.setItems(animals) { dialog, which ->
                            when (which) {
                                0 -> with(this@CustomPhotoFrameActivity)
                                    .cameraOnly()
                                    .crop()
                                    .start()

                                1 -> with(this@CustomPhotoFrameActivity)
                                    .galleryOnly()
                                    .crop()
                                    .start()
                            }
                        }
                        val dialog = builder.create()
                        dialog.show()
                    } else {
                        showSettingsDialog()
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

    var tvaction: TextView? = null
    override fun onResume() {
        super.onResume()
        llwatermark!!.visibility = View.GONE
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_select_frame)
        tvaction = toolbar.findViewById<View>(R.id.btn_next) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)
        tvaction!!.setOnClickListener {
            linearLogo!!.setBackgroundResource(0)
            linearEmail!!.setBackgroundResource(0)
            linearAddress!!.setBackgroundResource(0)
            linearPhone!!.setBackgroundResource(0)
            linearWebsite!!.setBackgroundResource(0)
            linearName!!.setBackgroundResource(0)
            mainframeBorder!!.setBackgroundResource(0)
            ivaddressclose!!.visibility = View.GONE
            ivphoneclose!!.visibility = View.GONE
            ivphotoclose!!.visibility = View.GONE
            ivwebsiteclose!!.visibility = View.GONE
            ivaddressclose!!.visibility = View.GONE
            ivnameClose!!.visibility = View.GONE
            try {

                if (imageView!=null) {
                    imageView!!.visibility = View.GONE
                }
            }
            catch (e: Exception)
            {

            }
            mPhotoEditor!!.clearHelperBox()
            if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!) {
                if (frameContentItemDetail!!.type!! == "0") {
                    llwatermark!!.visibility = View.GONE
                }else{
                    llwatermark!!.visibility = View.VISIBLE
                }
            } else {
                llwatermark!!.visibility = View.GONE
            }
            //Global.showProgressDialog(this@CustomPhotoFrameActivity)
            showProgress(true)
            val handler = Handler()
            handler.postDelayed({

                showProgress(false)
                //Global.dismissProgressDialog(this@CustomPhotoFrameActivity)
                frameLayout!!.isDrawingCacheEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                    frameLayout!!.buildDrawingCache(true)
                }
                val savedBmp = Bitmap.createBitmap(frameLayout!!.drawingCache)
                frameLayout!!.isDrawingCacheEnabled = false
                val newsaveBmp=scaleBitmap(savedBmp,1080,1080)
                try {
                    //Write file
                    val filename = "bitmap.png"
                    val stream = openFileOutput(filename, MODE_PRIVATE)
                    newsaveBmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)

                    //Cleanup
                    stream.close()
                    newsaveBmp.recycle()

                    //Pop intent
                    val in1 =
                        Intent(this@CustomPhotoFrameActivity, SaveAndShareActivity::class.java)
                    in1.putExtra("image", filename)
                    in1.putExtra("image_type", frameContentItemDetail!!.type)
                    startActivity(in1)
                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 1500)
        }
        //animateButton()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setFrameNEW(localFrameItem: FramePreview?) {
        Global.dismissProgressDialog(this@CustomPhotoFrameActivity)
        if (llframe!!.childCount > 0) llframe!!.removeAllViews()
        framePreview = localFrameItem
        textallSelected = true
        val frame_view = LayoutInflater.from(this@CustomPhotoFrameActivity).inflate(
            localFrameItem!!.layout_id,
            llframe, false
        )

        if (index1==0+plus)
        {
            ivcall1 = frame_view.findViewById(R.id.ivPhone1)
            tvframephone1 = frame_view.findViewById(R.id.tvframephone1)
        }
        if (index1==6+plus)
        {
            ivcall1 = frame_view.findViewById(R.id.ivPhone1)
            tvframephone1 = frame_view.findViewById(R.id.tvframephone1)
        }

        if (index1==14+plus)
        {
            ivcall1 = frame_view.findViewById(R.id.ivPhone1)
            tvframephone1 = frame_view.findViewById(R.id.tvframephone1)
        }

        if (index1==15+plus)
        {
            ivcall1 = frame_view.findViewById(R.id.ivPhone1)
            tvframephone1 = frame_view.findViewById(R.id.tvframephone1)
        }


        ivframebg = frame_view.findViewById(R.id.ivframebg)
        ivframelogo = frame_view.findViewById(R.id.ivframelogo)
        ivcall = frame_view.findViewById(R.id.ivPhone)
        ivEmail = frame_view.findViewById(R.id.ivEmail)
        ivWebsite = frame_view.findViewById(R.id.ivWebsite)
        ivLocation = frame_view.findViewById(R.id.ivAddress)
        tvframephone = frame_view.findViewById(R.id.tvframephone)
        tvframeemail = frame_view.findViewById(R.id.tvframeemail)
        tvframeweb = frame_view.findViewById(R.id.tvframeweb)
        tvframelocation = frame_view.findViewById(R.id.tvframelocation)
        tvframename = frame_view.findViewById(R.id.tvframename)
        phoneLine = frame_view.findViewById(R.id.viewPhone)
        websiteLine = frame_view.findViewById(R.id.viewwebsite)
        linearLogo = frame_view.findViewById(R.id.linearLogo)
        linearPhone = frame_view.findViewById(R.id.linearPhone)
        linearWebsite = frame_view.findViewById(R.id.linearWebsite)
        linearAddress = frame_view.findViewById(R.id.linearLocation)
        linearName = frame_view.findViewById(R.id.linearName)
        linearEmail = frame_view.findViewById(R.id.linearEmail)
        frameWebsite = frame_view.findViewById(R.id.frameWebsite)
        frameAddress = frame_view.findViewById(R.id.frameLocation)
        frameLogo = frame_view.findViewById(R.id.frameLogo)
        framePhone = frame_view.findViewById(R.id.framePhone)
        frameEmail = frame_view.findViewById(R.id.frameEmail)
        frameName = frame_view.findViewById(R.id.frameName)
        ivphotoclose = frame_view.findViewById(R.id.imgLogoClose)
        ivphoneclose = frame_view.findViewById(R.id.imgPhoneClose)
        ivemailclose = frame_view.findViewById(R.id.imgEmailClose)
        ivwebsiteclose = frame_view.findViewById(R.id.imgWebsiteClose)
        ivaddressclose = frame_view.findViewById(R.id.imgLocationClose)
        ivnameClose = frame_view.findViewById(R.id.imgNameClose)
        val imagemultiTouchListenerNew = MultiTouchListenerNewNotRotate()
        val phonemultiTouchListenerNew = MultiTouchListenerNotMoveble()
        val namemultiTouchListenerNer = MultiTouchListenerNotMoveble()
        val emailmultiTouchListenerNew = MultiTouchListenerNotMoveble()
        val websitemultiTouchListenerNew = MultiTouchListenerNotMoveble()
        val addressmultiTouchListenerNew = MultiTouchListenerNotMoveble()
        frameLogo!!.setVisibility(View.GONE)
        ivphotoclose!!.setVisibility(View.GONE)
        frameEmail!!.setVisibility(View.GONE)
        frameEmail!!.setVisibility(View.GONE)
        frameWebsite!!.setVisibility(View.GONE)
        frameAddress!!.setVisibility(View.GONE)
        framePhone!!.setVisibility(View.GONE)
        frameName!!.setVisibility(View.GONE)
        linearPhone!!.setBackgroundResource(0)
        linearEmail!!.setBackgroundResource(0)
        linearAddress!!.setBackgroundResource(0)
        linearWebsite!!.setBackgroundResource(0)
        linearLogo!!.setBackgroundResource(0)
        linearName!!.setBackgroundResource(0)

        /*val imgparams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        imgparams.height = width
        imgparams.width =  width
        ivframebg!!.layoutParams=imgparams*/


        imagemultiTouchListenerNew.setOnGestureControl(object :
            MultiTouchListenerNewNotRotate.OnGestureControl {
            override fun onClick() {
                linearPhone!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(R.drawable.contact_white_bg_image)
                frameLogo!!.setVisibility(View.VISIBLE)
                ivphotoclose!!.setVisibility(View.VISIBLE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                emailValue = false
                phoneValue = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = false
            }

            override fun onLongClick() {}
            override fun onTouch() {
                linearPhone!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(R.drawable.contact_white_bg_image)
                frameLogo!!.setVisibility(View.VISIBLE)
                ivphotoclose!!.setVisibility(View.VISIBLE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                emailValue = false
                phoneValue = false
                websiteValue = false
                addressValue = false
                nameValue = false
                textallSelected = false
            }
        })
        phonemultiTouchListenerNew.setOnGestureControl(object :
            MultiTouchListenerNotMoveble.OnGestureControl {
            override fun onClick() {
                linearPhone!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearEmail!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                framePhone!!.setVisibility(View.VISIBLE)
                ivphoneclose!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                emailValue = false
                phoneValue = true
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = false
            }

            override fun onLongClick() {}
            override fun onTouch() {
                linearPhone!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearEmail!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                framePhone!!.setVisibility(View.VISIBLE)
                ivphoneclose!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                emailValue = false
                phoneValue = true
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = false
            }
        })
        emailmultiTouchListenerNew.setOnGestureControl(object :
            MultiTouchListenerNotMoveble.OnGestureControl {
            override fun onClick() {
                linearEmail!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                frameEmail!!.setVisibility(View.VISIBLE)
                ivemailclose!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                emailValue = true
                phoneValue = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = false
            }

            override fun onLongClick() {}
            override fun onTouch() {
                linearEmail!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                frameEmail!!.setVisibility(View.VISIBLE)
                ivemailclose!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                emailValue = true
                phoneValue = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = false
            }
        })
        websitemultiTouchListenerNew.setOnGestureControl(object :
            MultiTouchListenerNotMoveble.OnGestureControl {
            override fun onClick() {
                linearWebsite!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                frameWebsite!!.setVisibility(View.VISIBLE)
                ivwebsiteclose!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                emailValue = false
                phoneValue = false
                websiteValue = true
                addressValue = false
                textallSelected = false
                nameValue = false
            }

            override fun onLongClick() {}
            override fun onTouch() {
                linearWebsite!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                frameWebsite!!.setVisibility(View.VISIBLE)
                ivwebsiteclose!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                emailValue = false
                phoneValue = false
                websiteValue = true
                addressValue = false
                textallSelected = false
                nameValue = false
            }
        })
        addressmultiTouchListenerNew.setOnGestureControl(object :
            MultiTouchListenerNotMoveble.OnGestureControl {
            override fun onClick() {
                linearAddress!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                emailValue = false
                phoneValue = false
                websiteValue = false
                addressValue = true
                textallSelected = false
                nameValue = false
                linearLogo!!.setBackgroundResource(0)
                frameAddress!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.VISIBLE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
            }

            override fun onLongClick() {}
            override fun onTouch() {
                linearAddress!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                emailValue = false
                phoneValue = false
                websiteValue = false
                addressValue = true
                textallSelected = false
                nameValue = false
                frameAddress!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.VISIBLE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
            }
        })
        namemultiTouchListenerNer.setOnGestureControl(object :
            MultiTouchListenerNotMoveble.OnGestureControl {
            override fun onClick() {
                linearName!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                emailValue = false
                phoneValue = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = true
                frameName!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.VISIBLE)
            }

            override fun onLongClick() {}
            override fun onTouch() {
                linearName!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(0)
                emailValue = false
                phoneValue = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = true
                frameName!!.setVisibility(View.VISIBLE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivphoneclose!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.VISIBLE)
            }
        })
        framePhone!!.setOnTouchListener(phonemultiTouchListenerNew)
        frameEmail!!.setOnTouchListener(emailmultiTouchListenerNew)
        frameWebsite!!.setOnTouchListener(websitemultiTouchListenerNew)
        frameAddress!!.setOnTouchListener(addressmultiTouchListenerNew)
        frameLogo!!.setOnTouchListener(imagemultiTouchListenerNew)
        frameName!!.setOnTouchListener(namemultiTouchListenerNer)
        val businessItem = get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS,this)
        if (businessItem != null) {
            try {
                if (index1 == 0 + plus) {
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_mobile_second != null && businessItem.busi_mobile_second != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall1!!.visibility = View.VISIBLE
                        tvframephone1!!.visibility = View.VISIBLE
                        tvframephone1!!.text = businessItem.busi_mobile_second
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != null && businessItem.busi_website != "") {
                        linearWebsite!!.visibility = View.VISIBLE
                        ivWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.visibility = View.VISIBLE
                        frameWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.text = businessItem.busi_website
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }

                }
                else if (index1 == 1 + plus) {
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }

                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }


                }
                else if (index1 == 2 + plus) {
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }

                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }


                }
                else if (index1 == 4 + plus) {

                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != null && businessItem.busi_website != "") {
                        linearWebsite!!.visibility = View.VISIBLE
                        ivWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.visibility = View.VISIBLE
                        frameWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.text = businessItem.busi_website
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }

                }
                else if (index1 == 6 + plus) {
                    linearEmail!!.hide();
                    frameEmail!!.hide();
                    linearWebsite!!.hide();
                    frameWebsite!!.hide();
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_mobile_second != null && businessItem.busi_mobile_second != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall1!!.visibility = View.VISIBLE
                        tvframephone1!!.visibility = View.VISIBLE
                        tvframephone1!!.text = businessItem.busi_mobile_second
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }

                }
                else if (index1 == 11 + plus) {
                    linearWebsite!!.hide();
                    frameWebsite!!.hide();

                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                }
                else if (index1 == 12 + plus) {

                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != null && businessItem.busi_website != "") {
                        linearWebsite!!.visibility = View.VISIBLE
                        ivWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.visibility = View.VISIBLE
                        frameWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.text = businessItem.busi_website
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                }
                else if (index1 == 13 + plus) {
                    linearWebsite!!.hide();
                    frameWebsite!!.hide();
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != null && businessItem.busi_website != "") {
                        linearWebsite!!.visibility = View.VISIBLE
                        ivWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.visibility = View.VISIBLE
                        frameWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.text = businessItem.busi_website
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                }
                else if (index1 == 14 + plus) {
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_mobile_second != null && businessItem.busi_mobile_second != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall1!!.visibility = View.VISIBLE
                        tvframephone1!!.visibility = View.VISIBLE
                        tvframephone1!!.text = businessItem.busi_mobile_second
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != null && businessItem.busi_website != "") {
                        linearWebsite!!.visibility = View.VISIBLE
                        ivWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.visibility = View.VISIBLE
                        frameWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.text = businessItem.busi_website
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                }
                else if (index1 == 15 + plus) {
                    frameWebsite!!.hide()
                    linearWebsite!!.hide()
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_mobile_second != null && businessItem.busi_mobile_second != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall1!!.visibility = View.VISIBLE
                        tvframephone1!!.visibility = View.VISIBLE
                        tvframephone1!!.text = businessItem.busi_mobile_second
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }

                }
                else if (index1 == 16 + plus) {
                    linearWebsite!!.hide()
                    linearEmail!!.hide()
                    linearAddress!!.hide()
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                }
                else if (index1 == 18 + plus) {
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != "") {
                        linearWebsite!!.visibility = View.VISIBLE
                        ivWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.visibility = View.VISIBLE
                        frameWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.text = businessItem.busi_website
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                }
                else if (index1 == 19 + plus) {
                    linearWebsite!!.hide()
                    linearAddress!!.hide()
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "") {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                }
                else if (index1 == 20 + plus) {
                    linearWebsite!!.hide()
                    linearAddress!!.hide()
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "") {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                }


                /*else if (index1 == 5) {

                    if (!businessItem.getBusiLogo().equals("") && businessItem.getBusiLogo() != null) {
                        linearLogo.setVisibility(View.VISIBLE);
                        ivframelogo.setVisibility(View.VISIBLE);
                        frameLogo.setVisibility(View.VISIBLE);
                        ivlogoselect.setImageResource(R.drawable.logo_select);
                        Glide.with(ChooseFrameForPhotoActivityNew.this).load(businessItem.getBusiLogo()).into(ivframelogo);
                    }


                    if (businessItem.getBusiMobile() != null && !businessItem.getBusiMobile().equals("")) {
                        framePhone.setVisibility(View.VISIBLE);
                        linearPhone.setVisibility(View.VISIBLE);
                        ivcall.setVisibility(View.VISIBLE);
                        tvframephone.setVisibility(View.VISIBLE);
                        tvframephone.setText(businessItem.getBusiMobile());
                        ivphoneselect.setImageResource(R.drawable.mobile_select);

                    }




                } else if (index1 == 6) {

                    if (!businessItem.getBusiName().equals("") && businessItem.getBusiName() != null) {
                        linearName.setVisibility(View.VISIBLE);
                        frameName.setVisibility(View.VISIBLE);
                        tvframename.setVisibility(View.VISIBLE);
                        tvframename.setText(businessItem.getBusiName());
                        ivnameSelect.setImageResource(R.drawable.businessname_select);

                    }

                    if (!businessItem.getBusiLogo().equals("") && businessItem.getBusiLogo() != null) {
                        linearLogo.setVisibility(View.VISIBLE);
                        ivframelogo.setVisibility(View.VISIBLE);
                        frameLogo.setVisibility(View.VISIBLE);
                        ivlogoselect.setImageResource(R.drawable.logo_select);
                        Glide.with(ChooseFrameForPhotoActivityNew.this).load(businessItem.getBusiLogo()).into(ivframelogo);
                    }


                    if (businessItem.getBusiMobile() != null && !businessItem.getBusiMobile().equals("")) {
                        framePhone.setVisibility(View.VISIBLE);
                        linearPhone.setVisibility(View.VISIBLE);
                        ivcall.setVisibility(View.VISIBLE);
                        tvframephone.setVisibility(View.VISIBLE);
                        tvframephone.setText(businessItem.getBusiMobile());
                        ivphoneselect.setImageResource(R.drawable.mobile_select);

                    }

                    if (businessItem.getBusiEmail() != null && !businessItem.getBusiEmail().equals("")) {
                        linearEmail.setVisibility(View.VISIBLE);
                        ivEmail.setVisibility(View.VISIBLE);
                        tvframeemail.setVisibility(View.VISIBLE);
                        frameEmail.setVisibility(View.VISIBLE);

                        tvframeemail.setText(businessItem.getBusiEmail());
                        ivemailselect.setImageResource(R.drawable.email_select);

                    }

                    if (businessItem.getBusiWebsite() != null && !businessItem.getBusiWebsite().equals("")) {
                        linearWebsite.setVisibility(View.VISIBLE);
                        ivWebsite.setVisibility(View.VISIBLE);
                        tvframeweb.setVisibility(View.VISIBLE);
                        frameWebsite.setVisibility(View.VISIBLE);
                        tvframeweb.setText(businessItem.getBusiWebsite());
                        ivwebsiteselect.setImageResource(R.drawable.website_select);
                    }
                }*/
                /*else if (index1 == 7) {


                    if (!businessItem.getBusiName().equals("") && businessItem.getBusiName() != null) {
                        linearName.setVisibility(View.VISIBLE);
                        frameName.setVisibility(View.VISIBLE);
                        tvframename.setVisibility(View.VISIBLE);
                        tvframename.setText(businessItem.getBusiName());
                        ivnameSelect.setImageResource(R.drawable.businessname_select);

                    }

                    if (!businessItem.getBusiLogo().equals("") && businessItem.getBusiLogo() != null) {
                        linearLogo.setVisibility(View.VISIBLE);
                        ivframelogo.setVisibility(View.VISIBLE);
                        frameLogo.setVisibility(View.VISIBLE);
                        ivlogoselect.setImageResource(R.drawable.logo_select);
                        Glide.with(ChooseFrameForPhotoActivityNew.this).load(businessItem.getBusiLogo()).into(ivframelogo);
                    }


                    if (businessItem.getBusiMobile() != null && !businessItem.getBusiMobile().equals("")) {
                        framePhone.setVisibility(View.VISIBLE);
                        linearPhone.setVisibility(View.VISIBLE);
                        ivcall.setVisibility(View.VISIBLE);
                        tvframephone.setVisibility(View.VISIBLE);
                        tvframephone.setText(businessItem.getBusiMobile());
                        ivphoneselect.setImageResource(R.drawable.mobile_select);

                    }





                }
                else if (index1 == 9) {
                    if (!businessItem.getBusiName().equals("") && businessItem.getBusiName() != null) {
                        linearName.setVisibility(View.VISIBLE);
                        frameName.setVisibility(View.VISIBLE);
                        tvframename.setVisibility(View.VISIBLE);
                        tvframename.setText(businessItem.getBusiName());
                        ivnameSelect.setImageResource(R.drawable.businessname_select);

                    }

                    if (!businessItem.getBusiLogo().equals("") && businessItem.getBusiLogo() != null) {
                        linearLogo.setVisibility(View.VISIBLE);
                        ivframelogo.setVisibility(View.VISIBLE);
                        frameLogo.setVisibility(View.VISIBLE);
                        ivlogoselect.setImageResource(R.drawable.logo_select);
                        Glide.with(ChooseFrameForPhotoActivityNew.this).load(businessItem.getBusiLogo()).into(ivframelogo);
                    }


                    if (businessItem.getBusiMobile() != null && !businessItem.getBusiMobile().equals("")) {
                        framePhone.setVisibility(View.VISIBLE);
                        linearPhone.setVisibility(View.VISIBLE);
                        ivcall.setVisibility(View.VISIBLE);
                        tvframephone.setVisibility(View.VISIBLE);
                        tvframephone.setText(businessItem.getBusiMobile());
                        ivphoneselect.setImageResource(R.drawable.mobile_select);

                    }

                    if (businessItem.getBusiAddress() != null && !businessItem.getBusiAddress().equals("")) {
                        linearAddress.setVisibility(View.VISIBLE);
                        ivLocation.setVisibility(View.VISIBLE);
                        tvframelocation.setVisibility(View.VISIBLE);
                        frameAddress.setVisibility(View.VISIBLE);
                        tvframelocation.setText(businessItem.getBusiAddress());
                        ivaddressselect.setImageResource(R.drawable.address_select);


                    }

                    if (businessItem.getBusiEmail() != null && !businessItem.getBusiEmail().equals("")) {
                        linearEmail.setVisibility(View.VISIBLE);
                        ivEmail.setVisibility(View.VISIBLE);
                        tvframeemail.setVisibility(View.VISIBLE);
                        frameEmail.setVisibility(View.VISIBLE);

                        tvframeemail.setText(businessItem.getBusiEmail());
                        ivemailselect.setImageResource(R.drawable.email_select);

                    }
                } else if (index1 == 10) {
                    if (!businessItem.getBusiName().equals("") && businessItem.getBusiName() != null) {
                        linearName.setVisibility(View.VISIBLE);
                        frameName.setVisibility(View.VISIBLE);
                        tvframename.setVisibility(View.VISIBLE);
                        tvframename.setText(businessItem.getBusiName());
                        ivnameSelect.setImageResource(R.drawable.businessname_select);

                    }

                    if (!businessItem.getBusiLogo().equals("") && businessItem.getBusiLogo() != null) {
                        linearLogo.setVisibility(View.VISIBLE);
                        ivframelogo.setVisibility(View.VISIBLE);
                        frameLogo.setVisibility(View.VISIBLE);
                        ivlogoselect.setImageResource(R.drawable.logo_select);
                        Glide.with(ChooseFrameForPhotoActivityNew.this).load(businessItem.getBusiLogo()).into(ivframelogo);
                    }


                    if (businessItem.getBusiMobile() != null && !businessItem.getBusiMobile().equals("")) {
                        framePhone.setVisibility(View.VISIBLE);
                        linearPhone.setVisibility(View.VISIBLE);
                        ivcall.setVisibility(View.VISIBLE);
                        tvframephone.setVisibility(View.VISIBLE);
                        tvframephone.setText(businessItem.getBusiMobile());
                        ivphoneselect.setImageResource(R.drawable.mobile_select);

                    }

                    if (businessItem.getBusiAddress() != null && !businessItem.getBusiAddress().equals("")) {
                        linearAddress.setVisibility(View.VISIBLE);
                        ivLocation.setVisibility(View.VISIBLE);
                        tvframelocation.setVisibility(View.VISIBLE);
                        frameAddress.setVisibility(View.VISIBLE);
                        tvframelocation.setText(businessItem.getBusiAddress());
                        ivaddressselect.setImageResource(R.drawable.address_select);


                    }

                    if (businessItem.getBusiEmail() != null && !businessItem.getBusiEmail().equals("")) {
                        linearEmail.setVisibility(View.VISIBLE);
                        ivEmail.setVisibility(View.VISIBLE);
                        tvframeemail.setVisibility(View.VISIBLE);
                        frameEmail.setVisibility(View.VISIBLE);

                        tvframeemail.setText(businessItem.getBusiEmail());
                        ivemailselect.setImageResource(R.drawable.email_select);

                    }

                    if (businessItem.getBusiWebsite() != null && !businessItem.getBusiWebsite().equals("")) {
                        linearWebsite.setVisibility(View.VISIBLE);
                        ivWebsite.setVisibility(View.VISIBLE);
                        tvframeweb.setVisibility(View.VISIBLE);
                        frameWebsite.setVisibility(View.VISIBLE);
                        tvframeweb.setText(businessItem.getBusiWebsite());
                        ivwebsiteselect.setImageResource(R.drawable.website_select);
                    }
                } else if (index1 == 11) {
                    if (!businessItem.getBusiName().equals("") && businessItem.getBusiName() != null) {
                        linearName.setVisibility(View.VISIBLE);
                        frameName.setVisibility(View.VISIBLE);
                        tvframename.setVisibility(View.VISIBLE);
                        tvframename.setText(businessItem.getBusiName());
                        ivnameSelect.setImageResource(R.drawable.businessname_select);

                    }

                    if (!businessItem.getBusiLogo().equals("") && businessItem.getBusiLogo() != null) {
                        linearLogo.setVisibility(View.VISIBLE);
                        ivframelogo.setVisibility(View.VISIBLE);
                        frameLogo.setVisibility(View.VISIBLE);
                        ivlogoselect.setImageResource(R.drawable.logo_select);
                        Glide.with(ChooseFrameForPhotoActivityNew.this).load(businessItem.getBusiLogo()).into(ivframelogo);
                    }


                    if (businessItem.getBusiMobile() != null && !businessItem.getBusiMobile().equals("")) {
                        framePhone.setVisibility(View.VISIBLE);
                        linearPhone.setVisibility(View.VISIBLE);
                        ivcall.setVisibility(View.VISIBLE);
                        tvframephone.setVisibility(View.VISIBLE);
                        tvframephone.setText(businessItem.getBusiMobile());
                        ivphoneselect.setImageResource(R.drawable.mobile_select);

                    }

                    if (businessItem.getBusiAddress() != null && !businessItem.getBusiAddress().equals("")) {
                        linearAddress.setVisibility(View.VISIBLE);
                        ivLocation.setVisibility(View.VISIBLE);
                        tvframelocation.setVisibility(View.VISIBLE);
                        frameAddress.setVisibility(View.VISIBLE);
                        tvframelocation.setText(businessItem.getBusiAddress());
                        ivaddressselect.setImageResource(R.drawable.address_select);


                    }

                    if (businessItem.getBusiEmail() != null && !businessItem.getBusiEmail().equals("")) {
                        linearEmail.setVisibility(View.VISIBLE);
                        ivEmail.setVisibility(View.VISIBLE);
                        tvframeemail.setVisibility(View.VISIBLE);
                        frameEmail.setVisibility(View.VISIBLE);

                        tvframeemail.setText(businessItem.getBusiEmail());
                        ivemailselect.setImageResource(R.drawable.email_select);

                    }

                    if (businessItem.getBusiWebsite() != null && !businessItem.getBusiWebsite().equals("")) {
                        linearWebsite.setVisibility(View.VISIBLE);
                        ivWebsite.setVisibility(View.VISIBLE);
                        tvframeweb.setVisibility(View.VISIBLE);
                        frameWebsite.setVisibility(View.VISIBLE);
                        tvframeweb.setText(businessItem.getBusiWebsite());
                        ivwebsiteselect.setImageResource(R.drawable.website_select);
                    }

                } else if (index1 == 12) {
                    if (!businessItem.getBusiName().equals("") && businessItem.getBusiName() != null) {
                        linearName.setVisibility(View.VISIBLE);
                        frameName.setVisibility(View.VISIBLE);
                        tvframename.setVisibility(View.VISIBLE);
                        tvframename.setText(businessItem.getBusiName());
                        ivnameSelect.setImageResource(R.drawable.businessname_select);

                    }

                    if (!businessItem.getBusiLogo().equals("") && businessItem.getBusiLogo() != null) {
                        linearLogo.setVisibility(View.VISIBLE);
                        ivframelogo.setVisibility(View.VISIBLE);
                        frameLogo.setVisibility(View.VISIBLE);
                        ivlogoselect.setImageResource(R.drawable.logo_select);
                        Glide.with(ChooseFrameForPhotoActivityNew.this).load(businessItem.getBusiLogo()).into(ivframelogo);
                    }


                    if (businessItem.getBusiMobile() != null && !businessItem.getBusiMobile().equals("")) {
                        framePhone.setVisibility(View.VISIBLE);
                        linearPhone.setVisibility(View.VISIBLE);
                        ivcall.setVisibility(View.VISIBLE);
                        tvframephone.setVisibility(View.VISIBLE);
                        tvframephone.setText(businessItem.getBusiMobile());
                        ivphoneselect.setImageResource(R.drawable.mobile_select);

                    }
                } */ else {
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.visibility = View.VISIBLE
                        frameName!!.visibility = View.VISIBLE
                        tvframename!!.visibility = View.VISIBLE
                        tvframename!!.text = businessItem.busi_name
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.visibility = View.VISIBLE
                        linearPhone!!.visibility = View.VISIBLE
                        ivcall!!.visibility = View.VISIBLE
                        tvframephone!!.visibility = View.VISIBLE
                        tvframephone!!.text = businessItem.busi_mobile
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.visibility = View.VISIBLE
                        ivLocation!!.visibility = View.VISIBLE
                        tvframelocation!!.visibility = View.VISIBLE
                        frameAddress!!.visibility = View.VISIBLE
                        tvframelocation!!.text = businessItem.busi_address
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.visibility = View.VISIBLE
                        ivEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.visibility = View.VISIBLE
                        frameEmail!!.visibility = View.VISIBLE
                        tvframeemail!!.text = businessItem.busi_email
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != null && businessItem.busi_website != "") {
                        linearWebsite!!.visibility = View.VISIBLE
                        ivWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.visibility = View.VISIBLE
                        frameWebsite!!.visibility = View.VISIBLE
                        tvframeweb!!.text = businessItem.busi_website
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                }
            } catch (e: Exception) {
            }
            ivphoneclose!!.setOnClickListener {
                framePhone!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivphoneselect!!.setImageResource(R.drawable.mobile_deselect)
            }
            ivphotoclose!!.setOnClickListener {
                frameLogo!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivlogoselect!!.setImageResource(R.drawable.logo_deselect)
            }
            ivemailclose!!.setOnClickListener {
                frameEmail!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivemailselect!!.setImageResource(R.drawable.email_deselect)
            }
            ivwebsiteclose!!.setOnClickListener {
                frameWebsite!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivwebsiteselect!!.setImageResource(R.drawable.website_deselect)
            }
            ivaddressclose!!.setOnClickListener {
                frameAddress!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivaddressselect!!.setImageResource(R.drawable.location_deselect)
            }
            ivnameClose!!.setOnClickListener {
                frameName!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                ivnameSelect!!.setImageResource(R.drawable.name_deselect)
            }
        }
        llframe!!.addView(frame_view)
    }

    fun showAddTextCustomDialog(text: String?, color: Int) {
        val dialog = Dialog(
            this@CustomPhotoFrameActivity,
            R.style.DialogAnimation
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.setContentView(R.layout.custom_add_text_dialog)
        dialog.setCancelable(true)
        val edtext = dialog.findViewById<View>(R.id.edtext) as EditText
        edtext.setTextColor(Color.BLACK)
        edtext.setText(text)
        val btndone = dialog.findViewById<View>(R.id.btndone) as TextView
        val btncancel = dialog.findViewById<View>(R.id.btncancel) as TextView
        btndone.setOnClickListener {
            if (edtext.text.toString() != "") {
                textView!!.text = edtext.text.toString()
                imageView!!.visibility = View.VISIBLE
                mainframeBorder!!.setBackgroundResource(R.drawable.rounded_border_tv)
                textviewSelected = true
            }

            dialog.dismiss()
        }
        emailValue = false
        addressValue = false
        phoneValue = false
        websiteValue = false
        textallSelected = false
        textviewSelected = true
        nameValue = false
        btncancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    override fun onBackPressed() {
        if (backpressed) {
            finish()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("If you are back your data will be lost?")
                .setPositiveButton("Ok") { dialog, which -> finish() }
                .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> if (backpressed) {
                onBackPressed()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("If you are back your changes will be lost?")
                    .setPositiveButton("Ok") { dialog, which -> finish() }
                    .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                    .show()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ResourceAsColor")
    fun showAddTextDialog(text: String?, color: Int) {
        val dialog = Dialog(
            this@CustomPhotoFrameActivity,
            R.style.DialogAnimation
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.setContentView(R.layout.custom_add_text_dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val edtext = dialog.findViewById<View>(R.id.edtext) as EditText
        edtext.setTextColor(R.color.colorWhite)
        edtext.setText(text)
        val btndone = dialog.findViewById<View>(R.id.btndone) as TextView
        val btncancel = dialog.findViewById<View>(R.id.btncancel) as TextView
        btndone.setOnClickListener {
            if (edtext.text.toString() != "") {
                tempEnteredText = edtext.text.toString()
                if (mPhotoEditor != null && !tempEnteredText.equals("", ignoreCase = true)) {
                    if (rootTextView == null) {
                        rootTextView = mPhotoEditor!!.addText(null, tempEnteredText, Color.BLACK)
                        i += 1
                        selectedPosition = i - 1
                        views.add(ViewData(rootTextView, edtext.text.toString(), i, Color.BLACK))
                    } else {
                        for (p in views.indices) {
                            if (p == selectedPosition) {
                                rootTextView = views[p].view
                                views[p] = ViewData(
                                    views[p].view,
                                    tempEnteredText,
                                    views[p].position,
                                    views[p].color
                                )
                                if (rootTextView != null) {
                                    mPhotoEditor!!.editText(
                                        rootTextView!!,
                                        null,
                                        views[p].text,
                                        views[p].color
                                    )
                                }
                            }
                        }
                    }
                }
            }


            //rootTextView = null;
            Global.hideSoftKeyboard(this@CustomPhotoFrameActivity, edtext)

            dialog.dismiss()
            emailValue = false
            addressValue = false
            phoneValue = false
            websiteValue = false
            textallSelected = false
            textviewSelected = false
            nameValue=false
        }
        btncancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    fun makeMaskImage(
        mImageView: ImageView?,
        maskimg: String?,
        frame: String?,
        leftMArgin: Int,
        topMargin: Int
    ) {
        Glide.with(this).asBitmap().load(frame)
            .into(mFrameIv!!)


        // frame of image
        val child16 = layoutInflater.inflate(R.layout.view_photo_textmove_text, null)
        frameBorder = child16.findViewById(R.id.frameLayout)
        mainframeBorder = child16.findViewById(R.id.frmBorder)
        val params1 = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        //params1.topMargin = Global.pxtoSdp(getApplicationContext(), topMargin);
        params1.topMargin = topMargin
        params1.leftMargin = leftMArgin
        //params1.leftMargin = Global.pxtoSdp(getApplicationContext(), leftMArgin);
        frameBorder!!.setLayoutParams(params1)
        textView = child16.findViewById(R.id.tvPhotoEditorText)
        textView!!.setText("Add Text")
        textView!!.setTextSize(15f)
        textView!!.setTextColor(selected_color)
        textView!!.setTypeface(selectedFontTypeface)
        imageView = child16.findViewById(R.id.imgPhotoEditorClose)
        imageView!!.setVisibility(View.GONE)
        mainframeBorder!!.setBackgroundResource(0)
        imageView!!.setOnClickListener(View.OnClickListener { frameBorder!!.setVisibility(View.GONE) })
        frameLayout!!.addView(child16)
        val multiTouchListenerNew = MultiTouchListenerNew()
        multiTouchListenerNew.setOnGestureControl(object : MultiTouchListenerNew.OnGestureControl {
            override fun onClick() {
                showAddTextCustomDialog(textView!!.getText().toString(), Color.BLACK)
            }

            override fun onLongClick() {
                showAddTextCustomDialog(textView!!.getText().toString(), Color.BLACK)
            }

            override fun onTouch() {}
        })
        child16.setOnTouchListener(multiTouchListenerNew)


        /*textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAddTextDialog(textView.getText().toString(),Color.BLACK);
                return false;
            }
        });*/try {
            result!!.recycle()
            result = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            mask!!.recycle()
            mask = null
        } catch (e: Exception) {
        }
        mask = getBitmapFromURL(maskimg)
        result = Bitmap.createBitmap(mask!!.width, mask!!.height, Bitmap.Config.ARGB_8888)
        //original = NativeStackBlur.process(getResizedBitmap(mask.getWidth(), mask.getHeight()), 25);
        val mCanvas = Canvas(result!!)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        //mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask!!, 0f, 0f, paint)
        paint.xfermode = null
        mImageView!!.scaleType = ImageView.ScaleType.CENTER
        try {
            mask!!.recycle()
            mask = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            original!!.recycle()
            original = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(
        requestcode: Int, resultcode: Int,
        imagereturnintent: Intent?
    ) {
        super.onActivityResult(requestcode, resultcode, imagereturnintent)
        try {
            if (resultcode == RESULT_OK) {
                val imageuri = imagereturnintent!!.data // Get intent
                // data
                Log.d("URI Path : ", "" + imageuri.toString())
                val realpath = getFilePath(imagereturnintent)
                //mMovImage.setImageURI(imageuri);
                profilePath = realpath
                val imgparams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                //RelativeLayout.LayoutParams imgparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                imgparams.height = imageHeight
                imgparams.width = imageWidth
                imgparams.topMargin = imageTop
                imgparams.leftMargin = imageleft

                mMovImage!!.layoutParams = imgparams
                mMovImage!!.setImageURI(imageuri)
                Log.d("Real Path : ", "" + realpath)
            } else if (resultcode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, "" + getError(imagereturnintent), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Image Cancelled", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
        }
    }


    override fun onItemClicked(`object`: Any?, index: Int) {
        Log.d("index123", "" + index)
        val photoItem = `object` as FramePreview
        index1 = index
        setFrameNEW(photoItem)
        if (index < plus) {
            if (photoItem.dynamic_images != null && !photoItem.dynamic_images.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                Glide.with(this@CustomPhotoFrameActivity).load(framePreview!!.dynamic_images).into(ivframebg!!)
            }
        }
    }

    private fun showPopupBusinessCategoryDialog(context: Context, text: String) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_font_type, null)
        rcvFont = layout.findViewById<View>(R.id.rcvFontType) as RecyclerView
        val ib_cancel = layout.findViewById<View>(R.id.ib_cancel) as AppCompatImageView
        fontTypeAdapter = FontTypeAdapter(this, fontTypeList, text)
        rcvFont!!.adapter = fontTypeAdapter
        val builder = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(true)
        alertDialog = builder.create()
        alertDialog!!.show()
        ib_cancel.onClick {
            alertDialog!!.dismiss()
        }


    }

    override fun onFontItemClicked(`object`: Any?, index: Int) {
        alertDialog!!.dismiss()
        val fontItem1 = `object` as FontTypeList
        val path = fontItem1.name
        Log.d("Abcd", "" + path)
        if (nameValue) {
            nametypeface = Typeface.createFromAsset(assets, path)
            tvframename!!.typeface = nametypeface
        } else if (textallSelected) {
            allfonttypeface = Typeface.createFromAsset(assets, path)
            tvframephone!!.typeface = allfonttypeface
            tvframeemail!!.typeface = allfonttypeface
            tvframeweb!!.typeface = allfonttypeface
            tvframelocation!!.typeface = allfonttypeface
            tvframename!!.typeface = allfonttypeface
            if (index1 == 0 + plus)
            {
                tvframephone1!!.typeface=allfonttypeface
            }
            if (index1 == 6 + plus)
            {
                tvframephone1!!.typeface=allfonttypeface
            }
            if (index1 == 14 + plus)
            {
                tvframephone1!!.typeface=allfonttypeface
            }
            if(index1== 15+plus)
            {
                tvframephone1!!.typeface=allfonttypeface

            }
        } else if (phoneValue) {
            phoneTypeface = Typeface.createFromAsset(assets, path)
            if (index1 == 0 + plus)
            {
                tvframephone1!!.typeface=phoneTypeface
            }
            if (index1 == 6 + plus)
            {
                tvframephone1!!.typeface=phoneTypeface
            }
            if (index1 == 14 + plus)
            {
                tvframephone1!!.typeface=phoneTypeface
            }
            if(index1== 15+plus)
            {
                tvframephone1!!.typeface=phoneTypeface

            }
            tvframephone!!.typeface = phoneTypeface
        }
        else if (textviewSelected) {
            textselectedFontTypeface = Typeface.createFromAsset(assets, path)
            textView!!.typeface = phoneTypeface
        }
        else if (emailValue) {
            emailTypeface = Typeface.createFromAsset(assets, path)
            tvframeemail!!.typeface = emailTypeface
        } else if (websiteValue) {
            websiteTypeface = Typeface.createFromAsset(assets, path)
            tvframeweb!!.typeface = websiteTypeface
        } else if (addressValue) {
            addressTypeface = Typeface.createFromAsset(assets, path)
            tvframelocation!!.typeface = addressTypeface
        } else {
            selectedFontTypeface =
                Typeface.createFromAsset(assets, path)
            for (p in views.indices) {
                if (p == selectedPosition) {
                    rootTextView = views[p].view
                    if (rootTextView != null) {
                        mPhotoEditor!!.editText(
                            rootTextView!!,
                            selectedFontTypeface,
                            views[p].text,
                            views[p].color
                        )
                    }
                }
            }
        }

    }

    override fun onColorSelected(dialogId: Int, color: Int) {

            namecolor = color;
            if (nameValue) {
                namecolor = color
                tvframename!!.setTextColor(namecolor)
            } else if (textallSelected) {
                allselectedcolor = color
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (index1 > 13 ) {
                        ivcall!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                        ivEmail!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                        ivWebsite!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                        ivLocation!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                    }

                    if (index1 == 14 + plus) {
                        ivcall1!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                    }
                    if (index1 == 15 + plus) {
                        ivcall1!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)

                    }
                }
                tvframephone!!.setTextColor(allselectedcolor)
                tvframeemail!!.setTextColor(allselectedcolor)
                tvframeweb!!.setTextColor(allselectedcolor)
                tvframelocation!!.setTextColor(allselectedcolor)
                tvframename!!.setTextColor(allselectedcolor)
                if (index1 == 0 + plus) {
                    tvframephone1!!.setTextColor(allselectedcolor)
                }
                if (index1 == 6 + plus) {
                    tvframephone1!!.setTextColor(allselectedcolor)
                }
                if (index1 == 14 + plus) {
                    tvframephone1!!.setTextColor(allselectedcolor)
                }
                if (index1 == 15 + plus) {
                    tvframephone1!!.setTextColor(allselectedcolor)

                }
            } else if (phoneValue) {
                phoneselected_color = color
                if (index1 == 0 + plus) {
                    tvframephone1!!.setTextColor(phoneselected_color)

                }
                if (index1 == 6 + plus) {
                    tvframephone1!!.setTextColor(phoneselected_color)

                }
                if (index1 == 14 + plus) {
                    tvframephone1!!.setTextColor(phoneselected_color)
                }
                if (index1 == 15 + plus) {
                    tvframephone1!!.setTextColor(phoneselected_color)

                }
                tvframephone!!.setTextColor(phoneselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (index1 > 13 ) {
                        ivcall!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)
                    }

                    if (index1 == 14 + plus) {
                        ivcall1!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)
                    }
                    if (index1 == 15 + plus) {
                        ivcall1!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)

                    }
                }
            } else if (textviewSelected) {
                textselected_color = color
                textView!!.setTextColor(textselected_color)
                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ivcall!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)
            }*/
            } else if (emailValue) {
                emailselected_color = color
                tvframeemail!!.setTextColor(emailselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (index1 > 13 ) {
                        ivEmail!!.backgroundTintList = ColorStateList.valueOf(emailselected_color)
                    }
                }
            } else if (websiteValue) {
                websiteselected_color = color
                tvframeweb!!.setTextColor(websiteselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (index1 > 13 ) {
                        ivWebsite!!.backgroundTintList =
                            ColorStateList.valueOf(websiteselected_color)
                    }
                }
            } else if (addressValue) {
                addressselected_color = color
                tvframelocation!!.setTextColor(addressselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    if (index1 > 13 ) {
                        ivLocation!!.backgroundTintList =
                            ColorStateList.valueOf(addressselected_color)
                    }
                }
            } else {
                selected_color = color
                Log.d("SelectedColor", "" + selected_color)
                /*if (rootTextView != null) {
                        mPhotoEditor.editText(rootTextView, selectedFontTypeface, tempEnteredText, selected_color);
                    }*/for (p in views.indices) {
                    if (p == selectedPosition) {
                        Log.d("SelectedColor12", "" + views[p].color)
                        rootTextView = views[p].view
                        views[p] = ViewData(
                            views[p].view,
                            views[p].text,
                            views[p].position,
                            selected_color
                        )
                        Log.d("SelectedColor1", "" + selected_color)
                        Log.d("SelectedColor11", "" + views[p].color)
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                null,
                                views[p].text,
                                views[p].color
                            )
                        }
                    }
                }
            }

        Log.d("ColorID", "Dialog id :$dialogId ColorId:$color");
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
        // GET CURRENT SIZE
        val width = bm.width
        val height = bm.height
        // GET SCALE SIZE
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)
        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    }

    companion object {
        fun getBitmapFromURL(src: String?): Bitmap? {
            return try {
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    inner class AsyncTaskExampleNew : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            //showProgress(true)
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): Void? {

            //showProgress(false)
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            //showProgress(false)
            super.onPostExecute(aVoid)
        }
    }

    fun scaleBitmap(bitmap: Bitmap, wantedWidth: Int, wantedHeight: Int): Bitmap? {
        val originalWidth = bitmap.width.toFloat()
        val originalHeight = bitmap.height.toFloat()
        val output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val m = Matrix()
        val scalex = wantedWidth / originalWidth
        val scaley = wantedHeight / originalHeight
        val xTranslation = 0.0f
        val yTranslation = (wantedHeight - originalHeight * scaley) / 2.0f
        m.postTranslate(xTranslation, yTranslation)
        m.preScale(scalex, scaley)
        // m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        val paint = Paint()
        paint.setFilterBitmap(true)
        canvas.drawBitmap(bitmap, m, paint)
        return output
    }
}