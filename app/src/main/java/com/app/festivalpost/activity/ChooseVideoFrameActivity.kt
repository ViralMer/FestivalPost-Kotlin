package com.app.festivalpost.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
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
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.app.festivalpost.photoeditor.PhotoEditor
import com.app.festivalpost.photoeditor.PhotoEditorView
import com.app.festivalpost.utility.MultiTouchListenerNewNotRotate
import com.app.festivalpost.utility.MultiTouchListenerNotMoveble
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.potyvideo.library.AndExoPlayerView
import kotlinx.android.synthetic.main.activity_choose_video_frame.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ChooseVideoFrameActivity : AppBaseActivity(), OnItemClickListener,FontOnItemClickListener,ColorPickerDialogListener {
    private var photoEditorView: PhotoEditorView? = null
    private var linearAddText: LinearLayout? = null
    private var linearTextcolor: LinearLayout? = null
    private var linearfonttype: LinearLayout? = null



    var video_path: String? = null
    var video_type: String? = null
    var video_item: VideoListItem? = null

    var recyclerView: RecyclerView? = null
    var llframe: LinearLayout? = null
    var layroot: LinearLayout? = null

    var views: MutableList<ViewData> = ArrayList()
    var mPhotoEditor: PhotoEditor? = null
    var tempEnteredText: String? = null
    var selectedPosition = 0
    var selected_color = Color.BLACK
    var rootTextView: View? = null
    var selectedFontTypeface: Typeface? = null

    var index1: Int? = 0
    var i = 0
    var plus = 0
    var backpressed = true
    var storeValue: String? = null
    var framePreview: FramePreview? = null
    var ivframelogo: ImageView? = null
    var frameNew: FrameLayout? = null
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
    var ivSocialIcons: ImageView? = null
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
    var linearSocialIcons: LinearLayout? = null
    var linearName: LinearLayout? = null
    var framePhone: FrameLayout? = null
    var frameEmail: FrameLayout? = null
    var frameWebsite: FrameLayout? = null
    var frameAddress: FrameLayout? = null
    var frameLogo: FrameLayout? = null
    var frameName: FrameLayout? = null
    var phoneLine: TextView? = null
    var websiteLine: TextView? = null
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
    var framePreviewArrayList = ArrayList<FramePreview>()
    var fontTypeAdapter: FontTypeAdapter? = null
    var fontTypeList = arrayListOf<FontTypeList?>()
    var rcvFont: RecyclerView? = null
    var alertDialog: AlertDialog? = null
    var width = 0
    var height = 0
    var sessionManager:SessionManager?=null
    var ivVideo:AndExoPlayerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_video_frame)
        sessionManager= SessionManager(this)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("video_path")) {
                video_path = intent.getStringExtra("video_path")
            }
            if (bundle.containsKey("video_type")) {
                video_type = bundle["video_type"] as String?
            }
        }
        Log.d("video_path", "" + video_path)
        setActionbar()
        photoEditorView = findViewById<View>(R.id.photoEditorView) as PhotoEditorView
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView!!)
            .setPinchTextScalable(true)
            .build()
        linearAddText = findViewById<View>(R.id.linearAddText) as LinearLayout
        linearTextcolor = findViewById<View>(R.id.lineartextcolor) as LinearLayout
        linearfonttype = findViewById<View>(R.id.linearFonttype) as LinearLayout
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        llframe = findViewById<View>(R.id.llframe) as LinearLayout
        recyclerView = findViewById<RecyclerView>(R.id.rvdata)
        ivVideo =findViewById(R.id.ivvideo) as AndExoPlayerView
        ivVideo!!.setSource(video_path)




        var frameListItems1 = arrayListOf<FrameListItem1>()
        frameListItems1 = getCustomFrameList(this)

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
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_17, "frame_17.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_18, "frame_18.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_19, "frame_19.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_20, "frame_20.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_21, "frame_21.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_1, "frame_01.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_3, "frame_03.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_4, "frame_04.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_5, "frame_05.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_6, "frame_06.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_7, "frame_07.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_8, "frame_08.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_9, "frame_01.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_11, "frame_03.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_12, "frame_04.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_13, "frame_05.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_14, "frame_06.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_15, "frame_07.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_16, "frame_08.png"))

        } catch (e: OutOfMemoryError) {


        } catch (e: Exception) {
        }


        val frameChooseAdapter = FrameChooseAdapter(this, framePreviewArrayList)
        val horizontalLayoutManagaer = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        framePreviewArrayList[0].isIs_selected=true

        recyclerView!!.setLayoutManager(horizontalLayoutManagaer)
        recyclerView!!.setAdapter(frameChooseAdapter)
        ivphoneselect = findViewById(R.id.ivMobileSelected)
        ivemailselect = findViewById(R.id.ivEmailSelected)
        ivaddressselect = findViewById(R.id.ivAddressSelected)
        ivwebsiteselect = findViewById(R.id.ivWebsiteSelected)
        ivlogoselect = findViewById(R.id.ivLogoSelected)
        ivnameSelect = findViewById(R.id.ivNameSelected)
        linearAddText!!.setOnClickListener {
            rootTextView = null
            showAddTextDialog("", selected_color)
        }
        if (getCustomFrameList(this).isNotEmpty()) {
            val photoItem=framePreviewArrayList[0]
            setFrameNEW(framePreviewArrayList[0])
            if (photoItem.dynamic_images != null && !photoItem.dynamic_images.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                //setFrameNEW(photoItem);
                Glide.with(this).load(photoItem.dynamic_images).into(ivframebg!!)
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
        //setFrameNEW(framePreviewArrayList[0])
        linearTextcolor!!.setOnClickListener {
            ColorPickerDialog.newBuilder().show(this)
        }
        linearfonttype!!.setOnClickListener {
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
            storeValue="Festival Post"
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
                if (index1 == 0 + plus) {
                    ivEmail!!.visibility = View.GONE
                    phoneLine!!.visibility = View.GONE
                } else if (index1 == 1 + plus) {
                    phoneLine!!.setVisibility(View.VISIBLE);
                    ivEmail!!.visibility = View.VISIBLE

                } else if (index1 == 2 + plus) {
                    phoneLine!!.setVisibility(View.VISIBLE);
                    ivEmail!!.visibility = View.VISIBLE

                } else {
                    phoneLine!!.visibility = View.GONE
                    ivEmail!!.visibility = View.VISIBLE
                }
                linearEmail!!.setBackgroundResource(0)
                linearEmail!!.visibility = View.VISIBLE

                tvframeemail!!.visibility = View.VISIBLE
                frameEmail!!.visibility = View.VISIBLE
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
                linearWebsite!!.setBackgroundResource(0)
                if (index1 == 0 + plus) {
                    ivWebsite!!.visibility = View.GONE
                    websiteLine!!.hide()
                } else if (index1 == 1 + plus) {

                    websiteLine!!.setVisibility(View.VISIBLE);
                    ivWebsite!!.visibility = View.VISIBLE
                } else if (index1 == 2 + plus) {

                    websiteLine!!.setVisibility(View.VISIBLE);
                    ivWebsite!!.visibility = View.VISIBLE
                } else if (index1 == 3 + plus) {

                    websiteLine!!.visibility = View.GONE
                    ivWebsite!!.visibility = View.GONE
                } else if (index1 == 3 + plus) {

                    websiteLine!!.visibility = View.GONE
                    ivWebsite!!.visibility = View.GONE
                } else {
                    websiteLine!!.visibility = View.GONE
                    ivWebsite!!.visibility = View.VISIBLE
                }
                //ivWebsite!!.visibility = View.VISIBLE
                tvframeweb!!.visibility = View.VISIBLE
                frameWebsite!!.visibility = View.VISIBLE
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
                if (index1 == 0 + plus) {
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

    fun setFrameNEW(localFrameItem: FramePreview?) {
        showProgress(false)
        if (llframe!!.childCount > 0) llframe!!.removeAllViews()
        framePreview = localFrameItem

        textallSelected = true
        val frame_view = LayoutInflater.from(this).inflate(
            localFrameItem!!.layout_id,
            llframe, false
        )
        ivframelogo = frame_view.findViewById(R.id.ivframelogo)
        ivframebg = frame_view.findViewById(R.id.ivframebg)

        ivcall = frame_view.findViewById(R.id.ivPhone)
        ivEmail = frame_view.findViewById(R.id.ivEmail)
        ivWebsite = frame_view.findViewById(R.id.ivWebsite)
        ivLocation = frame_view.findViewById(R.id.ivAddress)
        tvframephone = frame_view.findViewById(R.id.tvframephone)
        tvframeemail = frame_view.findViewById(R.id.tvframeemail)
        tvframeweb = frame_view.findViewById(R.id.tvframeweb)
        tvframelocation = frame_view.findViewById(R.id.tvframelocation)
        tvframename = frame_view.findViewById(R.id.tvframename)
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
        phoneLine = frame_view.findViewById(R.id.viewPhone)
        websiteLine = frame_view.findViewById(R.id.viewwebsite)
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
        ivframebg!!.layoutParams=imgparams
        frameNew!!.layoutParams=imgparams
*/


        /*if (index1==1)
        {
            phoneLine.setVisibility(View.VISIBLE);
            websiteLine.setVisibility(View.VISIBLE);
        }
        if (index1==2)
        {
            phoneLine.setVisibility(View.VISIBLE);
            websiteLine.setVisibility(View.VISIBLE);
        }
        if (index1==8)
        {
            phoneLine.setVisibility(View.VISIBLE);
            websiteLine.setVisibility(View.VISIBLE);
        }
        if (index1==11)
        {
            phoneLine.setVisibility(View.VISIBLE);
            websiteLine.setVisibility(View.VISIBLE);
        }
        if (index1==15)
        {
            phoneLine.setVisibility(View.VISIBLE);
            websiteLine.setVisibility(View.VISIBLE);
        }*/

        val businessItem = get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS,this)

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
                storeValue = businessItem!!.busi_mobile
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
                storeValue = businessItem!!.busi_mobile
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
                storeValue = businessItem!!.busi_email
                linearEmail!!.setBackgroundResource(R.drawable.rounded_border_tv_new)
                linearPhone!!.setBackgroundResource(0)
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
                storeValue = businessItem!!.busi_email
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
                storeValue = businessItem!!.busi_website
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
                storeValue = businessItem!!.busi_website
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
                storeValue = businessItem!!.busi_address
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
                storeValue = businessItem!!.busi_address
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
                ivphotoclose!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
            }
        })
        namemultiTouchListenerNer.setOnGestureControl(object :
            MultiTouchListenerNotMoveble.OnGestureControl {
            override fun onClick() {
                storeValue = businessItem!!.busi_name
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
                storeValue = businessItem!!.busi_name
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

        if (businessItem != null) {
            try {
                if (index1 == 0 + plus) {
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.setVisibility(View.VISIBLE)
                        frameName!!.setVisibility(View.VISIBLE)
                        tvframename!!.setVisibility(View.VISIBLE)
                        tvframename!!.setText(businessItem.busi_name)
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busi_mobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                } else if (index1 == 2 + plus) {
                    if (businessItem.busi_name != "" && businessItem.busi_name != null) {
                        linearName!!.setVisibility(View.VISIBLE)
                        frameName!!.setVisibility(View.VISIBLE)
                        tvframename!!.setVisibility(View.VISIBLE)
                        tvframename!!.setText(businessItem.busi_name)
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busi_mobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.setVisibility(View.VISIBLE)
                        ivLocation!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setVisibility(View.VISIBLE)
                        frameAddress!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setText(businessItem.busi_address)
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busi_email)
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != "") {
                        linearWebsite!!.setVisibility(View.VISIBLE)
                        ivWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setVisibility(View.VISIBLE)
                        frameWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setText(businessItem.busi_website)
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                } else if (index1 == 3 + plus) {
                    if (businessItem.busi_logo != "") {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busi_mobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_email != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busi_email)
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                } else if (index1 == 4 + plus) {
                    if (businessItem.busi_name != "") {
                        linearName!!.setVisibility(View.VISIBLE)
                        frameName!!.setVisibility(View.VISIBLE)
                        tvframename!!.setVisibility(View.VISIBLE)
                        tvframename!!.setText(businessItem.busi_name)
                        ivnameSelect!!.setImageResource(R.drawable.name_select)
                    }
                    if (businessItem.busi_logo != "") {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busi_mobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busi_email)
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                } /*else if (index1 == 5) {

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
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this).load(businessItem.busi_logo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busi_mobile != null && businessItem.busi_mobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busi_mobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busi_address != null && businessItem.busi_address != "") {
                        linearAddress!!.setVisibility(View.VISIBLE)
                        ivLocation!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setVisibility(View.VISIBLE)
                        frameAddress!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setText(businessItem.busi_address)
                        ivaddressselect!!.setImageResource(R.drawable.location_select)
                    }
                    if (businessItem.busi_email != null && businessItem.busi_email != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busi_email)
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busi_website != null && businessItem.busi_website != "") {
                        linearWebsite!!.setVisibility(View.VISIBLE)
                        ivWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setVisibility(View.VISIBLE)
                        frameWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setText(businessItem.busi_website)
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                }
            } catch (e: Exception) {
            }
            ivphoneclose!!.setOnClickListener(View.OnClickListener {
                framePhone!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivphoneselect!!.setImageResource(R.drawable.mobile_select)
            })
            ivphotoclose!!.setOnClickListener(View.OnClickListener {
                frameLogo!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivlogoselect!!.setImageResource(R.drawable.logo_select)
            })
            ivemailclose!!.setOnClickListener(View.OnClickListener {
                frameEmail!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivemailselect!!.setImageResource(R.drawable.email_select)
            })
            ivwebsiteclose!!.setOnClickListener(View.OnClickListener {
                frameWebsite!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivwebsiteselect!!.setImageResource(R.drawable.website_select)
            })
            ivaddressclose!!.setOnClickListener(View.OnClickListener {
                frameAddress!!.setVisibility(View.GONE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivaddressselect!!.setImageResource(R.drawable.location_select)
            })
            ivnameClose!!.setOnClickListener(View.OnClickListener {
                frameName!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                ivnameSelect!!.setImageResource(R.drawable.name_select)
            })
        }
        llframe!!.addView(frame_view)
    }




    var tvaction: TextView? = null
    @SuppressLint("ResourceAsColor")
    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvaction = toolbar.findViewById<View>(R.id.btn_next) as TextView
        tvtitle.text = resources.getString(R.string.txt_select_frame)
        tvaction!!.text = resources.getString(R.string.txt_next)
        tvaction!!.setOnClickListener {
            showProgress(true)
            ivVideo!!.stopPlayer()

            linearAddress!!.setBackgroundResource(0)
            linearEmail!!.setBackgroundResource(0)
            linearPhone!!.setBackgroundResource(0)
            linearAddress!!.setBackgroundResource(0)
            linearLogo!!.setBackgroundResource(0)
            linearName!!.setBackgroundResource(0)
            ivaddressclose!!.visibility = View.GONE
            ivphoneclose!!.visibility = View.GONE
            ivphotoclose!!.visibility = View.GONE
            ivwebsiteclose!!.visibility = View.GONE
            ivaddressclose!!.visibility = View.GONE
            ivnameClose!!.visibility = View.GONE

            val handler = Handler()
            handler.postDelayed({

                mPhotoEditor!!.clearHelperBox()

                showProgress(false)
                layroot!!.setBackgroundColor(R.color.transparent)
                layroot!!.isDrawingCacheEnabled = true
                layroot!!.buildDrawingCache(true)

                val savedBmp = Bitmap.createBitmap(
                    layroot!!.drawingCache
                )
                layroot!!.isDrawingCacheEnabled = false
                val newsaveBmp = scaleBitmap(savedBmp, 1080, 1080)
                try {
                    showProgress(true)
                    //Write file
                    val filename = "video_bitmap.png"
                    val stream = openFileOutput(filename, MODE_PRIVATE)
                    newsaveBmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)


                    //Cleanup
                    stream.close()
                    newsaveBmp!!.recycle()

                    if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!
                    ) {
                        if (video_type!! == "0") {
                            sessionManager!!.setStringValue(
                                "image_name",
                                "/data/data/com.app.festivalpost/files/$filename"
                            )
                            download()
                        } else {
                            AlertDialog.Builder(this)
                                .setTitle("Sorry!!")
                                .setMessage("Please buy premium plan and save video.")
                                .setPositiveButton("Buy Premium") { dialog, which ->
                                    val intent = Intent(
                                        this,
                                        PremiumActivity::class.java
                                    )
                                    startActivity(intent)
                                }
                                .setNegativeButton(
                                    "Cancel"
                                ) { dialog, _ -> dialog.dismiss() }
                                .show()
                        }
                        showProgress(false)
                    } else {
                        sessionManager!!.setStringValue(
                            "image_name",
                            "/data/data/com.app.festivalpost/files/$filename"
                        )
                        download()

                    }

                    //Pop intent


                } catch (e: Exception) {
                    showProgress(false)
                    e.printStackTrace()
                }
            }, 1500)
        }

    }

    @SuppressLint("ResourceAsColor")
    fun showAddTextDialog(text: String?, color: Int) {
        val dialog = Dialog(
            this,
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
        edtext.setTextColor(R.color.colorBlack)
        edtext.setText(text)
        val btndone = dialog.findViewById<View>(R.id.btndone) as TextView
        val btncancel = dialog.findViewById<View>(R.id.btncancel) as TextView
        btndone.setOnClickListener {
            try {
                if (edtext.text.toString() != "") {
                    tempEnteredText = edtext.text.toString()
                    if (mPhotoEditor != null && !tempEnteredText.equals("", ignoreCase = true)) {
                        if (rootTextView == null) {
                            rootTextView =
                                mPhotoEditor!!.addText(null, tempEnteredText, Color.BLACK)
                            i += 1
                            selectedPosition = i - 1
                            views.add(
                                ViewData(
                                    rootTextView,
                                    edtext.text.toString(),
                                    i,
                                    Color.BLACK
                                )
                            )
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
            } catch (e: IndexOutOfBoundsException) {
                Log.d("IndexArray", "" + e.message)
            } catch (e: Exception) {
                Log.d("IndexArray1", "" + e.message)
            }

            //rootTextView = null;
            Global.hideSoftKeyboard(this, edtext)
            emailValue = false
            addressValue = false
            phoneValue = false
            websiteValue = false
            nameValue = false
            textallSelected = false
            dialog.dismiss()
        }
        btncancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showPopupBusinessCategoryDialog(context: Context, text: String) {
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_font_type, null)
        rcvFont = layout.findViewById<View>(R.id.rcvFontType) as RecyclerView
        val ib_cancel = layout.findViewById<View>(R.id.ib_cancel) as AppCompatImageView
        fontTypeAdapter= FontTypeAdapter(this, fontTypeList, text)
        rcvFont!!.adapter=fontTypeAdapter
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
        val fontItem1=`object` as FontTypeList
        val path=fontItem1.name
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
        } else if (phoneValue) {
            phoneTypeface = Typeface.createFromAsset(assets, path)
            tvframephone!!.typeface = phoneTypeface
        } else if (emailValue) {
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
        namecolor=color;
        if (nameValue) {
            namecolor = color
            tvframename!!.setTextColor(namecolor)
        } else if (textallSelected) {
            allselectedcolor = color
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ivcall!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                ivEmail!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                ivWebsite!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                ivLocation!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
            }
            tvframephone!!.setTextColor(allselectedcolor)
            tvframeemail!!.setTextColor(allselectedcolor)
            tvframeweb!!.setTextColor(allselectedcolor)
            tvframelocation!!.setTextColor(allselectedcolor)
            tvframename!!.setTextColor(allselectedcolor)
        } else if (phoneValue) {
            phoneselected_color = color
            tvframephone!!.setTextColor(phoneselected_color)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ivcall!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)
            }
        } else if (emailValue) {
            emailselected_color = color
            tvframeemail!!.setTextColor(emailselected_color)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ivEmail!!.backgroundTintList = ColorStateList.valueOf(emailselected_color)
            }
        } else if (websiteValue) {
            websiteselected_color = color
            tvframeweb!!.setTextColor(websiteselected_color)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ivWebsite!!.backgroundTintList = ColorStateList.valueOf(websiteselected_color)
            }
        } else if (addressValue) {
            addressselected_color = color
            tvframelocation!!.setTextColor(addressselected_color)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ivLocation!!.backgroundTintList = ColorStateList.valueOf(addressselected_color)
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

    override fun onItemClicked(`object`: Any?, index: Int) {
        Log.d("index123", "" + index)
        val photoItem=`object` as FramePreview
        index1 = index
        setFrameNEW(photoItem)
        if (index < plus) {
            if (photoItem.dynamic_images != null && !photoItem.dynamic_images.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                //setFrameNEW(photoItem);
                Glide.with(this).load(framePreview!!.dynamic_images)
                    .placeholder(
                        R.drawable.placeholder_img
                    ).error(R.drawable.placeholder_img).into(
                        ivframebg!!
                    )
            }
        }
    }

    private val videoName: String
        private get() = "video_demo.mp4"

    private fun download() {
        val DB = DownloadVideo()
        DB.execute()
    }

    private inner class DownloadVideo : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)
        }

        override fun doInBackground(vararg p0: String?): String? {
            val vidurl = video_path
            downloadfile(vidurl!!)
            return null
        }

        override fun onPostExecute(s: String?) {
            showProgress(false)
            super.onPostExecute(s)
        }
    }


    private fun downloadfile(vidurl: String) {
        try {
            val url = URL(vidurl)
            val c = url.openConnection() as HttpURLConnection
            c.requestMethod = "GET"
            c.doOutput = true
            c.connect()
            val videoname = "/data/data/com.app.festivalpost/files/$videoName"
            val videoname1 = videoName
            sessionManager!!.setStringValue("video_name", videoname)
            val file: File
            var fileOutputStream: FileOutputStream? = null
            try {
                file = filesDir
                fileOutputStream = openFileOutput(videoName, MODE_PRIVATE) //MODE PRIVATE
                val `in` = c.inputStream
                val buffer = ByteArray(1024)
                var len1 = 0
                while (`in`.read(buffer).also { len1 = it } > 0) {
                    fileOutputStream.write(buffer, 0, len1)
                }
                fileOutputStream.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                try {
                    fileOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            val intent = Intent(this, VideoCreateActivity::class.java)
            intent.putExtra("video_item", video_item)
            startActivity(intent)
            finish()


        } catch (e: IOException) {
            Log.d("Error....", e.toString())
            showProgress(false)
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> if (backpressed) {
                onBackPressed()
                ivvideo!!.stopPlayer()

            } else {
                AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("If you are back your changes will be lost?")
                    .setPositiveButton("Ok") { _, _ -> finish() }
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
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

