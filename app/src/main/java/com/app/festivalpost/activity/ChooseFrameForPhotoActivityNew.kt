package com.app.festivalpost.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.PickerBuilder.onImageReceivedListener
import com.app.festivalpost.activity.PickerBuilder.onPermissionRefusedListener
import com.app.festivalpost.adapter.FontTypeAdapter
import com.app.festivalpost.adapter.FrameChooseAdapter
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.app.festivalpost.photoeditor.OnPhotoEditorListener
import com.app.festivalpost.photoeditor.PhotoEditor
import com.app.festivalpost.photoeditor.PhotoEditorView
import com.app.festivalpost.photoeditor.ViewType
import com.app.festivalpost.utility.MultiTouchListenerNewNotRotate
import com.app.festivalpost.utility.MultiTouchListenerNotMoveble
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import java.io.IOException
import java.util.*

class ChooseFrameForPhotoActivityNew : AppBaseActivity(), OnItemClickListener,FontOnItemClickListener,ColorPickerDialogListener {
    private var photoEditorView: PhotoEditorView? = null
    private var linearAddText: LinearLayout? = null
    private var linearTextcolor: LinearLayout? = null
    private var linearfonttype: LinearLayout? = null
    private var linearbackgroundcolor: LinearLayout? = null


    var framePreview: FramePreview? = null


    var layroot: LinearLayout? = null
    var views: MutableList<ViewData> = ArrayList()
    var mPhotoEditor: PhotoEditor? = null
    var tempEnteredText: String? = null
    var selectedPosition = 0
    var index1 = 0
    var plus = 0
    var width = 0
    var height = 0
    var selected_color = Color.BLACK
    var rootTextView: View? = null
    var selectedFontTypeface: Typeface? = null
    var llframe: LinearLayout? = null
    var lay_frames: LinearLayout? = null
    var llwatermark: LinearLayout? = null
    var ivbackground: ImageView? = null
    private var photo_path: String? = ""
    private var image_type: Int? = 0
    private var storeValue: String? = ""
    var i = 0
    var backpressed = true
    var background = false
    var ivcall1: ImageView? = null
    var tvframephone1: TextView? = null
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
        setContentView(R.layout.activity_choose_frame_for_photo)
        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            Log.d("AppCrash", "" + thread.toString())
            Log.d("AppCrash1", "" + e.message.toString())
        }
        sessionManager=SessionManager(this)
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("photo_path")) {
                photo_path = bundle["photo_path"] as String?
            }
            if (bundle.containsKey("image_type")) {
                image_type = bundle["image_type"] as Int?
            }
        }


        setActionbar()
        selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")



        phoneTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        emailTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        websiteTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        addressTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        allfonttypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        nametypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        recyclerView = findViewById(R.id.rvdata)
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        if (photo_path != null && !photo_path.equals("", ignoreCase = true)) {
            Glide.with(this@ChooseFrameForPhotoActivityNew).load(photo_path).into(ivbackground!!)
        }

        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        llframe = findViewById<View>(R.id.llframe) as LinearLayout
        llwatermark = findViewById<View>(R.id.llwatermark) as LinearLayout
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels



        photoEditorView = findViewById<View>(R.id.photoEditorView) as PhotoEditorView
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView!!)
            .setPinchTextScalable(true)
            .build()
            linearAddText = findViewById<View>(R.id.linearAddText) as LinearLayout
            linearTextcolor = findViewById<View>(R.id.lineartextcolor) as LinearLayout
            linearfonttype = findViewById<View>(R.id.linearFonttype) as LinearLayout
            linearbackgroundcolor = findViewById<View>(R.id.linearbackgroundcolor) as LinearLayout

        layroot!!.setOnClickListener {
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
            background =false
            websiteValue = false
            addressValue = false
            textallSelected = true
            nameValue = false
        }
        ivphoneselect = findViewById(R.id.ivMobileSelected)
        ivemailselect = findViewById(R.id.ivEmailSelected)
        ivaddressselect = findViewById(R.id.ivAddressSelected)
        ivwebsiteselect = findViewById(R.id.ivWebsiteSelected)
        ivlogoselect = findViewById(R.id.ivLogoSelected)
        ivnameSelect = findViewById(R.id.ivNameSelected)




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
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_33, "frame_33.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_34, "frame_34.png"))
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
            /*framePreviewArrayList.add(FramePreview(R.layout.custom_frame_9, "frame_01.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_11, "frame_03.png"))*/
            /*framePreviewArrayList.add(FramePreview(R.layout.custom_frame_12, "frame_04.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_13, "frame_05.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_14, "frame_06.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_15, "frame_07.png"))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_16, "frame_08.png"))*/

        } catch (e: OutOfMemoryError) {


        } catch (e: Exception) {
        }









        val frameChooseAdapter = FrameChooseAdapter(this, framePreviewArrayList)
        val horizontalLayoutManagaer = LinearLayoutManager(
            this@ChooseFrameForPhotoActivityNew,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView!!.layoutManager = horizontalLayoutManagaer
        recyclerView!!.adapter = frameChooseAdapter
        if (getCustomFrameList(this).isNotEmpty()) {
            val photoItem=framePreviewArrayList[0]
            setFrameNEW(framePreviewArrayList[0])
            if (photoItem.dynamic_images != null && !photoItem.dynamic_images.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                //setFrameNEW(photoItem);
                Glide.with(this@ChooseFrameForPhotoActivityNew).load(photoItem.dynamic_images)
                    .into(
                        ivframebg!!
                    )
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
            }

            override fun onStopViewChangeListener(
                viewType: ViewType?,
                numberOfAddedViews: Int,
                view: View?
            ) {
            }
        })
        /*findViewById<View>(R.id.linearbackgroundcolor).setOnClickListener {
            val dialog = Dialog(
                this@ChooseFrameForPhotoActivityNew,
                R.style.DialogAnimation
            )
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            dialog.setContentView(R.layout.custom_color_picker_dialog)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            val colorPickerView = dialog.findViewById<View>(R.id.colorPickerView) as ColorPickerView
            val viewColor = dialog.findViewById<View>(R.id.viewColor)
            colorPickerView.setInitialColor(Color.RED)
            colorPickerView.setEnabledBrightness(true)
            colorPickerView.setEnabledAlpha(true)
            viewColor.setBackgroundColor(Color.RED)
            colorPickerView.subscribe { color, fromUser, shouldPropagate ->
                selected_color = color
                viewColor.setBackgroundColor(color)
            }
            val btndone = dialog.findViewById<View>(R.id.btndone) as TextView
            val btncancel = dialog.findViewById<View>(R.id.btncancel) as TextView
            btndone.setOnClickListener {
                layroot!!.setBackgroundColor(colorPickerView.color)
                dialog.dismiss()
            }
            btncancel.setOnClickListener {
                selected_color = R.color.colorBlack
                dialog.dismiss()
            }
            dialog.show()
        }*/

        linearAddText!!.setOnClickListener {
            rootTextView = null
            showAddTextDialog("", selected_color)
        }
        linearTextcolor!!.setOnClickListener {
            ColorPickerDialog.newBuilder().show(this)
        }
        linearbackgroundcolor!!.setOnClickListener {
            background=true;
            ColorPickerDialog.newBuilder().show(this)
        }
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
                    phoneLine!!.visibility = View.GONE
                    websiteLine!!.visibility=View.GONE
                    ivEmail!!.visibility = View.VISIBLE
                } else if (index1 == 1 + plus) {
                    phoneLine!!.visibility = View.GONE;
                    websiteLine!!.visibility=View.GONE
                    ivEmail!!.visibility = View.VISIBLE
                }
                else if (index1 == 2 + plus) {
                    ivEmail!!.visibility = View.GONE
                    phoneLine!!.visibility = View.GONE
                } else if (index1 == 3 + plus) {
                    phoneLine!!.setVisibility(View.VISIBLE);
                    ivEmail!!.visibility = View.VISIBLE

                } else if (index1 == 4 + plus) {
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
                    ivWebsite!!.visibility = View.VISIBLE
                    websiteLine!!.hide()
                } else if (index1 == 1 + plus) {
                    websiteLine!!.setVisibility(View.GONE);
                    ivWebsite!!.visibility = View.GONE
                    linearWebsite!!.visibility = View.GONE
                }else if (index1 == 2 + plus) {

                    websiteLine!!.setVisibility(View.GONE);
                    ivWebsite!!.visibility = View.GONE
                } else if (index1 == 3 + plus) {
                    websiteLine!!.visibility = View.GONE
                    ivWebsite!!.visibility = View.GONE
                } else if (index1 == 4 + plus) {
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
                if (index1 == 2 + plus) {
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


    override fun onResume() {
        super.onResume()
        llwatermark!!.visibility = View.GONE
    }

    @SuppressLint("ResourceAsColor")
    fun showAddTextDialog(text: String?, color: Int) {
        val dialog = Dialog(
            this@ChooseFrameForPhotoActivityNew,
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
            Global.hideSoftKeyboard(this@ChooseFrameForPhotoActivityNew, edtext)
            emailValue = false
            addressValue = false
            phoneValue = false
            background =false
            websiteValue = false
            nameValue = false
            background =false
            textallSelected = false
            dialog.dismiss()
        }
        btncancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    fun startCamera() {
        PickerBuilder(this@ChooseFrameForPhotoActivityNew, PickerBuilder.SELECT_FROM_CAMERA)
            .setOnImageReceivedListener(object : onImageReceivedListener {
                override fun onImageReceived(imageUri: Uri?) {
                    Toast.makeText(
                        this@ChooseFrameForPhotoActivityNew,
                        "Got image - $imageUri",
                        Toast.LENGTH_LONG
                    ).show()
                    addImage(imageUri)
                }
            })
            .setImageName("testImage")
            .setImageFolderName(resources.getString(R.string.app_name))
            .withTimeStamp(false)
            .setCropScreenColor(resources.getColor(R.color.colorPrimary))
            .start()
    }

    fun startGallery() {
        PickerBuilder(this@ChooseFrameForPhotoActivityNew, PickerBuilder.SELECT_FROM_GALLERY)
            .setOnImageReceivedListener(object : onImageReceivedListener {
                override fun onImageReceived(imageUri: Uri?) {
                    Toast.makeText(
                        this@ChooseFrameForPhotoActivityNew,
                        "Got image - $imageUri",
                        Toast.LENGTH_LONG
                    ).show()
                    addImage(imageUri)
                }
            })
            .setImageName("test")
            .setImageFolderName(resources.getString(R.string.app_name))
            .setCropScreenColor(resources.getColor(R.color.colorPrimary))
            .setOnPermissionRefusedListener(object : onPermissionRefusedListener {
                override fun onPermissionRefused() {}
            })
            .start()
    }

    fun addImage(uri: Uri?) {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (mPhotoEditor != null && bitmap != null) {
            mPhotoEditor!!.addImage(bitmap)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }



    var tvaction: TextView? = null
    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvaction = toolbar.findViewById<View>(R.id.btn_next) as TextView
        tvtitle.text = resources.getString(R.string.txt_select_frame)
        tvaction!!.text = resources.getString(R.string.txt_next)
        tvaction!!.setOnClickListener {
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
            mPhotoEditor!!.clearHelperBox()
            showProgress(true)
            if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!) {
                if (image_type!! == 0) {
                    showProgress(true)
                    llwatermark!!.visibility = View.GONE
                    val handler = Handler()
                    handler.postDelayed({
                        Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)

                        val params = ivframebg!!.layoutParams
                        val params1 = layroot!!.layoutParams
                        params.width = width
                        params1.width = width
                        params.height = width
                        params1.height = width
                        layroot!!.isDrawingCacheEnabled = true

                        layroot!!.buildDrawingCache(true)
                        val savedBmp = Bitmap.createBitmap(
                            layroot!!.drawingCache
                        )
                        layroot!!.isDrawingCacheEnabled = false
                        val newsaveBmp = scaleBitmap(savedBmp, 1080, 1080)
                        try {
                            //Write file
                            val filename = "bitmap.png"
                            val stream = openFileOutput(filename, MODE_PRIVATE)
                            newsaveBmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)

                            //Cleanup
                            stream.close()
                            newsaveBmp.recycle()

                            //Pop intent
                            val in1 = Intent(
                                this@ChooseFrameForPhotoActivityNew,
                                SaveAndShareActivity::class.java
                            )
                            in1.putExtra("image", filename)
                            in1.putExtra("image_type", image_type.toString())
                            startActivity(in1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, 1500)
                    showProgress(false)
                }else{
                    showProgress(true)
                    llwatermark!!.show()
                    val handler = Handler()
                    handler.postDelayed({
                        //Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)
                        val params = ivframebg!!.layoutParams
                        val params1 = layroot!!.layoutParams
                        params.width = width
                        params1.width = width
                        params.height = width
                        params1.height = width
                        layroot!!.isDrawingCacheEnabled = true
                        layroot!!.buildDrawingCache(true)
                        val savedBmp = Bitmap.createBitmap(
                            layroot!!.drawingCache
                        )
                        layroot!!.isDrawingCacheEnabled = false
                        val newsaveBmp = scaleBitmap(savedBmp, 1080, 1080)
                        try {
                            //Write file
                            val filename = "bitmap.png"
                            val stream = openFileOutput(filename, MODE_PRIVATE)
                            newsaveBmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)

                            //Cleanup
                            stream.close()
                            newsaveBmp.recycle()

                            //Pop intent
                            val in1 = Intent(
                                this@ChooseFrameForPhotoActivityNew,
                                SaveAndShareActivity::class.java
                            )
                            in1.putExtra("image", filename)
                            in1.putExtra("image_type", image_type.toString())
                            startActivity(in1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, 1500)

                }
                showProgress(false)
            } else {
                showProgress(true)
                llwatermark!!.visibility = View.GONE
                val handler = Handler()
                handler.postDelayed({
                    //Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)
                    val params = ivframebg!!.layoutParams

                    params.width = width
                    params.height = width

                    layroot!!.isDrawingCacheEnabled = true
                    layroot!!.buildDrawingCache(true)
                    val savedBmp = Bitmap.createBitmap(
                        layroot!!.drawingCache
                    )
                    layroot!!.isDrawingCacheEnabled = false
                    val newsaveBmp = scaleBitmap(savedBmp, 1080, 1080)
                    try {
                        //Write file
                        val filename = "bitmap.png"
                        val stream = openFileOutput(filename, MODE_PRIVATE)
                        newsaveBmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)

                        //Cleanup
                        stream.close()
                        newsaveBmp.recycle()

                        //Pop intent
                        val in1 = Intent(
                            this@ChooseFrameForPhotoActivityNew,
                            SaveAndShareActivity::class.java
                        )
                        in1.putExtra("image", filename)
                        in1.putExtra("image_type", image_type.toString())
                        startActivity(in1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, 1500)
                showProgress(false)
            }


        }
        //animateButton()
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


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this@ChooseFrameForPhotoActivityNew)
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


    fun setFrameNEW(localFrameItem: FramePreview?) {
        Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)
        if (llframe!!.childCount > 0) llframe!!.removeAllViews()
        framePreview = localFrameItem
        textallSelected = true
        val frame_view = LayoutInflater.from(this@ChooseFrameForPhotoActivityNew).inflate(
            localFrameItem!!.layout_id,
            llframe, false
        )

        if (index1==0+plus)
        {
            ivcall1 = frame_view.findViewById(R.id.ivPhone1)
            tvframephone1 = frame_view.findViewById(R.id.tvframephone1)
        }

        if (index1==1+plus)
        {
            ivcall1 = frame_view.findViewById(R.id.ivPhone1)
            tvframephone1 = frame_view.findViewById(R.id.tvframephone1)
        }

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
        frameLogo!!.visibility = View.GONE
        ivphotoclose!!.visibility = View.GONE
        frameEmail!!.visibility = View.GONE
        frameEmail!!.visibility = View.GONE
        frameWebsite!!.visibility = View.GONE
        frameAddress!!.visibility = View.GONE
        framePhone!!.visibility = View.GONE
        frameName!!.visibility = View.GONE
        linearPhone!!.setBackgroundResource(0)
        linearEmail!!.setBackgroundResource(0)
        linearAddress!!.setBackgroundResource(0)
        linearWebsite!!.setBackgroundResource(0)
        linearLogo!!.setBackgroundResource(0)
        linearName!!.setBackgroundResource(0)


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

        val businessItem = get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS, this)

        imagemultiTouchListenerNew.setOnGestureControl(object :
            MultiTouchListenerNewNotRotate.OnGestureControl {
            override fun onClick() {
                linearPhone!!.setBackgroundResource(0)
                linearEmail!!.setBackgroundResource(0)
                linearAddress!!.setBackgroundResource(0)
                linearWebsite!!.setBackgroundResource(0)
                linearName!!.setBackgroundResource(0)
                linearLogo!!.setBackgroundResource(R.drawable.contact_white_bg_image)
                frameLogo!!.visibility = View.VISIBLE
                ivphotoclose!!.visibility = View.VISIBLE
                ivphoneclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                emailValue = false
                phoneValue = false
                background = false
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
                frameLogo!!.visibility = View.VISIBLE
                ivphotoclose!!.visibility = View.VISIBLE
                ivphoneclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                emailValue = false
                background = false
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
                framePhone!!.visibility = View.VISIBLE
                ivphoneclose!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                emailValue = false
                phoneValue = true
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = false
                background = false
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
                framePhone!!.visibility = View.VISIBLE
                ivphoneclose!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                emailValue = false
                phoneValue = true
                websiteValue = false
                addressValue = false
                textallSelected = false
                background = false
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
                frameEmail!!.visibility = View.VISIBLE
                ivemailclose!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                emailValue = true
                phoneValue = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                background = false
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
                frameEmail!!.visibility = View.VISIBLE
                ivemailclose!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                emailValue = true
                phoneValue = false
                background = false
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
                frameWebsite!!.visibility = View.VISIBLE
                ivwebsiteclose!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                emailValue = false
                phoneValue = false
                websiteValue = true
                addressValue = false
                background = false
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
                frameWebsite!!.visibility = View.VISIBLE
                ivwebsiteclose!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                emailValue = false
                phoneValue = false
                websiteValue = true
                background = false
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
                background = false
                linearLogo!!.setBackgroundResource(0)
                frameAddress!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.VISIBLE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
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
                background = false
                nameValue = false
                frameAddress!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.VISIBLE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
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
                background = false
                websiteValue = false
                addressValue = false
                textallSelected = false
                nameValue = true
                frameName!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.VISIBLE
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
                background = false
                nameValue = true
                frameName!!.visibility = View.VISIBLE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.VISIBLE
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
                    linearWebsite!!.hide()
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

                } else if (index1 == 2 + plus) {
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
                } else if (index1 == 4+ plus) {
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
                        tvframeweb!!.text = businessItem.busi_address
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                } else if (index1 == 5+plus) {
                    linearAddress!!.hide()
                    linearWebsite!!.hide()

                    if (businessItem.busi_logo != "") {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this@ChooseFrameForPhotoActivityNew).load(businessItem.busi_logo)
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
                else if (index1 == 6 + plus) {
                    linearWebsite!!.hide()
                    linearAddress!!.hide()
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
                    if (businessItem.busi_logo != "" && businessItem.busi_logo != null) {
                        linearLogo!!.visibility = View.VISIBLE
                        ivframelogo!!.visibility = View.VISIBLE
                        frameLogo!!.visibility = View.VISIBLE
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this@ChooseFrameForPhotoActivityNew).load(businessItem.busi_logo)
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
            ivphoneclose!!.setOnClickListener(View.OnClickListener {
                framePhone!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivphoneselect!!.setImageResource(R.drawable.mobile_select)
            })
            ivphotoclose!!.setOnClickListener(View.OnClickListener {
                frameLogo!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivlogoselect!!.setImageResource(R.drawable.logo_select)
            })
            ivemailclose!!.setOnClickListener(View.OnClickListener {
                frameEmail!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivemailselect!!.setImageResource(R.drawable.email_select)
            })
            ivwebsiteclose!!.setOnClickListener(View.OnClickListener {
                frameWebsite!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivwebsiteselect!!.setImageResource(R.drawable.website_select)
            })
            ivaddressclose!!.setOnClickListener(View.OnClickListener {
                frameAddress!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivaddressselect!!.setImageResource(R.drawable.location_select)
            })
            ivnameClose!!.setOnClickListener(View.OnClickListener {
                frameName!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
                ivnameSelect!!.setImageResource(R.drawable.name_select)
            })
        }
        llframe!!.addView(frame_view)
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
                    Glide.with(this@ChooseFrameForPhotoActivityNew).load(photoItem.dynamic_images).into(
                        ivframebg!!
                    )
                }
            }
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
            if (index1 == 0 + plus)
            {
                tvframephone1!!.typeface=allfonttypeface
            }
            if(index1== 1+plus)
            {
                tvframephone1!!.typeface=allfonttypeface

            }
        } else if (phoneValue) {
            phoneTypeface = Typeface.createFromAsset(assets, path)
            if (index1 == 0 + plus)
            {
                tvframephone1!!.typeface=phoneTypeface
            }
            if(index1== 1+plus)
            {
                tvframephone1!!.typeface=phoneTypeface

            }
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
        namecolor=color
        if (nameValue) {
            namecolor = color
            tvframename!!.setTextColor(namecolor)
        } else if (textallSelected) {
            if (background) {
                layroot!!.setBackgroundColor(color)
            } else {
                allselectedcolor = color
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ivcall!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                    ivEmail!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                    ivWebsite!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                    ivLocation!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                    if (index1 == 0 + plus)
                    {
                        ivcall1!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)
                    }
                    if(index1== 1 + plus)
                    {
                        ivcall1!!.backgroundTintList = ColorStateList.valueOf(allselectedcolor)

                    }
                }
                tvframephone!!.setTextColor(allselectedcolor)
                tvframeemail!!.setTextColor(allselectedcolor)
                tvframeweb!!.setTextColor(allselectedcolor)
                tvframelocation!!.setTextColor(allselectedcolor)
                tvframename!!.setTextColor(allselectedcolor)
                if (index1 == 0 + plus)
                {
                    tvframephone1!!.setTextColor(allselectedcolor)
                }
                if(index1== 1+plus)
                {
                    tvframephone1!!.setTextColor(allselectedcolor)

                }
            }
        } else if (phoneValue) {
            phoneselected_color = color
            if (index1 == 0 + plus)
            {
                tvframephone1!!.setTextColor(phoneselected_color)
            }
            if(index1== 1+plus)
            {
                tvframephone1!!.setTextColor(phoneselected_color)

            }
            tvframephone!!.setTextColor(phoneselected_color)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ivcall!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)
                if (index1 == 0 + plus)
                {
                    ivcall1!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)
                }
                if(index1== 1+plus)
                {
                    ivcall1!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)

                }
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
        Log.d("ColorID", "Dialog id :$dialogId ColorId:$color")
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
        return Bitmap.createBitmap(bm, 0, 0, newWidth, newHeight, matrix, false)
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