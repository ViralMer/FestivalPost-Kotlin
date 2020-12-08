package com.app.festivalpost.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.activity.PickerBuilder.onImageReceivedListener
import com.app.festivalpost.activity.PickerBuilder.onPermissionRefusedListener
import com.app.festivalpost.adapter.FrameChooseAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.FrameListItem
import com.app.festivalpost.models.FramePreview
import com.app.festivalpost.models.LocalFrameItemNew
import com.app.festivalpost.models.ViewData
import com.app.festivalpost.photoeditor.OnPhotoEditorListener
import com.app.festivalpost.photoeditor.PhotoEditor
import com.app.festivalpost.activity.OnItemClickListener

import com.app.festivalpost.photoeditor.PhotoEditorView
import com.app.festivalpost.photoeditor.ViewType
import com.app.festivalpost.utility.MultiTouchListenerNewNotRotate
import com.app.festivalpost.utility.MultiTouchListenerNotMoveble
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.json.JSONObject
import top.defaults.colorpicker.ColorPickerView
import java.io.IOException
import java.util.*

class ChooseFrameForPhotoActivityNew : AppCompatActivity(), ApiResponseListener,OnItemClickListener
    {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    private var photoEditorView: PhotoEditorView? = null
    private var tvaddtext: TextView? = null
    private var tvaddimage: TextView? = null
    private var tvtextcolor: TextView? = null
    var localFrameItemNew: LocalFrameItemNew? = null
    var framePreview: FramePreview? = null
    private var tvfonttype: TextView? = null
    var layroot: LinearLayout? = null
    var views: MutableList<ViewData> = ArrayList()
    var mPhotoEditor: PhotoEditor? = null
    var tempEnteredText: String? = null
    var selectedPosition = 0
    var index1 = 0
    var selected_color = Color.BLACK
    var rootTextView: View? = null
    var selectedFontTypeface: Typeface? = null
    var llframe: LinearLayout? = null
    var lay_frames: LinearLayout? = null
    var llwatermark: LinearLayout? = null
    var ivbackground: ImageView? = null
    var photo_path: String? = ""
    var i = 0
    var backpressed = true
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
    var ivSocialIcons: ImageView? = null
    var tvframephone: TextView? = null
    var tvframeemail: TextView? = null
    var tvframeweb: TextView? = null
    var tvframelocation: TextView? = null
    var tvframename: TextView? = null
    var linearPhone: LinearLayout? = null
    var linearEmail: LinearLayout? = null
    var     linearWebsite: LinearLayout? = null
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
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("photo_path")) {
                photo_path = bundle["photo_path"] as String?
            }
        }

        apiManager =
            ApiManager(this@ChooseFrameForPhotoActivityNew)
        setActionbar()
        selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        phoneTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        emailTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        websiteTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        addressTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        allfonttypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        nametypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        recyclerView = findViewById(R.id.rvdata)
        ivbackground = findViewById<View>(R.id.ivbackground) as ImageView
        if (photo_path != null && !photo_path.equals("", ignoreCase = true)) {
            Glide.with(this@ChooseFrameForPhotoActivityNew).load(photo_path)
                .placeholder(R.drawable.placeholder_img).error(
                    R.drawable.placeholder_img
                ).into(
                    ivbackground!!
                )
        }
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        llframe = findViewById<View>(R.id.llframe) as LinearLayout
        llwatermark = findViewById<View>(R.id.llwatermark) as LinearLayout
        photoEditorView = findViewById<View>(R.id.photoEditorView) as PhotoEditorView
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView!!)
            .setPinchTextScalable(true)
            .build()
        tvaddtext = findViewById<View>(R.id.tvaddtext) as TextView
        tvaddimage = findViewById<View>(R.id.tvaddimage) as TextView
        tvtextcolor = findViewById<View>(R.id.tvtextcolor) as TextView
        tvfonttype = findViewById<View>(R.id.tvfonttype) as TextView
        tvtextcolor!!.isEnabled = true
        tvfonttype!!.isEnabled = true
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
        try {
            framePreviewArrayList = Global.newFrames
        } catch (e: OutOfMemoryError) {
        } catch (e: Exception) {
        }
        var frameListItems1 = ArrayList<FrameListItem>()
        val gson = Gson()
        val json = Global.getPreference(Constant.PREF_FRAME_LIST, "")
        frameListItems1 = if (json!!.isEmpty()) {
            ArrayList()
        } else {
            val type = object : TypeToken<ArrayList<FrameListItem?>?>() {}.type
            gson.fromJson(json, type)
        }
        Log.d("FrameListSize1", "" + frameListItems1.size)
        for (i in frameListItems1.indices) {
            framePreviewArrayList.add(
                FramePreview(
                    R.layout.custom_frame_layout_dynamic,
                    frameListItems1[i].frame_url
                )
            )
        }
        /*
        for (int i=0;i<frameListItems1.size();i++)
        {
            LayoutInflater mInflater1 = LayoutInflater.from(ChooseFrameForPhotoActivityNew.this);
            View child1 = mInflater1.inflate(R.layout.custom_frame_dynamic, null);
            FrameLayout frameLayout1 = child1.findViewById(R.id.frameLayout);
            try {
                FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(325, 325);
                params1.width=dpToPx(130);
                params1.height=dpToPx(130);
                frameLayout1.setLayoutParams(params1);
            }
            catch (Exception e)
            {

            }
            ImageView imageView1_1=child1.findViewById(R.id.ivselected);
            ImageView imageView1_2=child1.findViewById(R.id.ivFrameDynamic);
            imageView1_1.setImageResource(R.drawable.frame_selection_bg);
            Glide.with(ChooseFrameForPhotoActivityNew.this).load(frameListItems1.get(i).getFrame_url()).into(imageView1_2);


            LayoutInflater mInflater5 = LayoutInflater.from(ChooseFrameForPhotoActivityNew.this);
            View layoutDynamic1 = mInflater5.inflate(R.layout.custom_frame_layout_dynamic, null);

            ImageView ivframebg1 = layoutDynamic1.findViewById(R.id.ivframebg);
            ImageView ivframelogo1 = layoutDynamic1.findViewById(R.id.ivframelogo);
            Glide.with(ChooseFrameForPhotoActivityNew.this).load(frameListItems1.get(i).getFrame_url()).into(ivframebg1);
            dynamicFrameArrayList.add(new LocalFrameItemNew(layoutDynamic1,child1));
        }
*/


        /* if (((LinearLayout) lay_frames).getChildCount() > 0) {
            ((LinearLayout) lay_frames).removeAllViews();
        }
        final ArrayList<ImageView> imageViewArrayList = new ArrayList<>();

        */
        /*if (localFrameItemArrayList.size() > 0) {
            for (int i = 0; i < localFrameItemArrayList.size(); i++) {
                LocalFrameItem localFrameItem = localFrameItemArrayList.get(i);

                View frame_view = LayoutInflater.from(ChooseFrameForPhotoActivityNew.this).inflate(localFrameItem.getPreview_id(),
                        lay_frames, false);
                ImageView ivselected = frame_view.findViewById(R.id.ivselected);
                imageViewArrayList.add(ivselected);

                frame_view.setTag(i);
                frame_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        LocalFrameItem lfi = localFrameItemArrayList.get(index);
                        setFrame(lfi);
                        for (int imagesIndex = 0; imagesIndex < imageViewArrayList.size(); imagesIndex++) {
                            ImageView img = imageViewArrayList.get(imagesIndex);
                            if(imagesIndex == index){
                                img.setVisibility(View.VISIBLE);
                            }else{
                                img.setVisibility(View.GONE);
                            }
                        }

                    }
                });

                lay_frames.addView(frame_view);
            }

            setFrame(localFrameItemArrayList.get(0));
            for (int imagesIndex = 0; imagesIndex < imageViewArrayList.size(); imagesIndex++) {
                ImageView img = imageViewArrayList.get(imagesIndex);
                if(imagesIndex == 0){
                    img.setVisibility(View.VISIBLE);
                }else{
                    img.setVisibility(View.GONE);
                }
            }
        }*/
        /*

        if (dynamicFrameArrayList.size() > 0) {

            for (int i = 0; i < dynamicFrameArrayList.size(); i++) {
                LocalFrameItemNew localFrameItem = dynamicFrameArrayList.get(i);



                View frame_view = localFrameItem.getFrameView();
                ImageView ivselected = frame_view.findViewById(R.id.ivselected);
                imageViewArrayList.add(ivselected);

                frame_view.setTag(i);
                frame_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        index1=index;
                        LocalFrameItemNew lfi = dynamicFrameArrayList.get(index);
                        setFrameNEW(lfi);
                        Log.d("ImageIndex",""+index);
                        for (int imagesIndex = 0; imagesIndex < imageViewArrayList.size(); imagesIndex++) {
                            ImageView img = imageViewArrayList.get(imagesIndex);

                            try {
                                if (imagesIndex == index) {
                                    img.setVisibility(View.VISIBLE);
                                } else {
                                    img.setVisibility(View.GONE);
                                }
                            }
                            catch (Exception e)
                            {
                                Log.d("Errror",""+e.getMessage());
                            }

                        }

                    }
                });

                lay_frames.addView(frame_view);
                //
            }

            setFrameNEW(dynamicFrameArrayList.get(0));

            for (int imagesIndex = 0; imagesIndex < imageViewArrayList.size(); imagesIndex++) {
                try {
                    ImageView img = imageViewArrayList.get(imagesIndex);
                    if (imagesIndex == 0) {
                        img.setVisibility(View.VISIBLE);
                    } else {
                        img.setVisibility(View.GONE);
                    }
                }
                catch (Exception e)
                {

                }

            }
        }
*/
        val frameChooseAdapter = FrameChooseAdapter(this, framePreviewArrayList)
        val horizontalLayoutManagaer = LinearLayoutManager(
            this@ChooseFrameForPhotoActivityNew,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView!!.setLayoutManager(horizontalLayoutManagaer)
        recyclerView!!.setAdapter(frameChooseAdapter)
        setFrameNEW(framePreviewArrayList[0])


        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // do something after 2s = 2000 miliseconds
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);

                    }
                },2000);
                //horizontalScrollView.fullScroll(HorizontalScrollView.FO);

            }
        }, 10);

*/mPhotoEditor!!.setOnPhotoEditorListener(object : OnPhotoEditorListener {
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
                    tvtextcolor!!.isEnabled = true
                    tvfonttype!!.isEnabled = true
                    tvtextcolor!!.setBackgroundResource(R.drawable.small_button_bg)
                    tvfonttype!!.setBackgroundResource(R.drawable.small_button_bg)
                    backpressed = true
                } else {
                    i -= 1
                    views.removeAt(selectedPosition)
                    selectedPosition = i - 1
                    tvtextcolor!!.isEnabled = true
                    tvfonttype!!.isEnabled = true
                    tvtextcolor!!.setBackgroundResource(R.drawable.small_button_bg)
                    tvfonttype!!.setBackgroundResource(R.drawable.small_button_bg)
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
        findViewById<View>(R.id.btnchangebackgroundcolor).setOnClickListener {
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
        }
        tvaddimage!!.setOnClickListener {
            Dexter.withActivity(this@ChooseFrameForPhotoActivityNew)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            val builder = AlertDialog.Builder(this@ChooseFrameForPhotoActivityNew)
                            builder.setTitle("Choose")
                            val animals = arrayOf("Camera", "Gallery")
                            builder.setItems(animals) { dialog, which ->
                                when (which) {
                                    0 -> startCamera()
                                    1 -> startGallery()
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
        tvaddtext!!.setOnClickListener {
            rootTextView = null
            showAddTextDialog("", selected_color)
        }
        tvtextcolor!!.setOnClickListener { showColorPickerDialog() }
        tvfonttype!!.setOnClickListener {
            var defaultText = ""
            if (defaultText.equals("", ignoreCase = true)) {
                defaultText = "Custom Text"
            }
            showFonttypeDialog(defaultText)
        }
        ivlogoselect!!.setOnClickListener(View.OnClickListener {
            if (ivlogoselect!!.getDrawable().constantState === resources.getDrawable(R.drawable.logo_select).constantState) {
                ivlogoselect!!.setImageResource(R.drawable.logo_light)
                setPrefernces("false", "1")
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
                setPrefernces("true", "1")
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
            if (ivemailselect!!.getDrawable().constantState === resources.getDrawable(R.drawable.email_select).constantState) {
                ivemailselect!!.setImageResource(R.drawable.email_light)
                setPrefernces("false", "3")
                linearEmail!!.setBackgroundResource(0)
                linearEmail!!.visibility = View.GONE
                ivEmail!!.visibility = View.GONE
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
                setPrefernces("true", "3")
                linearEmail!!.setBackgroundResource(0)
                linearEmail!!.visibility = View.VISIBLE
                ivEmail!!.visibility = View.VISIBLE
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
            if (ivphoneselect!!.getDrawable().constantState === resources.getDrawable(R.drawable.mobile_select).constantState) {
                ivphoneselect!!.setImageResource(R.drawable.mobile_light)
                setPrefernces("false", "2")
                linearPhone!!.visibility = View.GONE
                linearPhone!!.setBackgroundResource(0)
                ivcall!!.visibility = View.GONE
                tvframephone!!.visibility = View.GONE
                framePhone!!.visibility = View.GONE
                phoneLine!!.visibility = View.GONE
                ivphotoclose!!.visibility = View.GONE
                ivaddressclose!!.visibility = View.GONE
                ivphoneclose!!.visibility = View.GONE
                ivwebsiteclose!!.visibility = View.GONE
                ivemailclose!!.visibility = View.GONE
                ivnameClose!!.visibility = View.GONE
            } else {
                ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                setPrefernces("true", "2")
                linearPhone!!.visibility = View.VISIBLE
                linearPhone!!.setBackgroundResource(0)
                ivcall!!.visibility = View.VISIBLE
                phoneLine!!.visibility = View.VISIBLE
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
            if (ivwebsiteselect!!.getDrawable().constantState === resources.getDrawable(R.drawable.website_select).constantState) {
                ivwebsiteselect!!.setImageResource(R.drawable.website_light)
                setPrefernces("false", "5")
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
                setPrefernces("true", "5")
                linearWebsite!!.visibility = View.VISIBLE
                linearWebsite!!.setBackgroundResource(0)
                websiteLine!!.visibility = View.VISIBLE
                ivWebsite!!.visibility = View.VISIBLE
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
            if (ivaddressselect!!.getDrawable().constantState === resources.getDrawable(R.drawable.address_select).constantState) {
                ivaddressselect!!.setImageResource(R.drawable.address_light)
                setPrefernces("false", "4")
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
                ivaddressselect!!.setImageResource(R.drawable.address_select)
                setPrefernces("true", "4")
                linearAddress!!.setBackgroundResource(0)
                linearAddress!!.visibility = View.VISIBLE
                ivLocation!!.visibility = View.VISIBLE
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
            if (ivnameSelect!!.getDrawable().constantState === resources.getDrawable(R.drawable.businessname_select).constantState) {
                ivnameSelect!!.setImageResource(R.drawable.businessname_light)
                setPrefernces("false", "6")
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
                ivnameSelect!!.setImageResource(R.drawable.businessname_select)
                setPrefernces("true", "6")
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

    fun setPrefernces(isDelete: String?, prefernceValue: String?) {
        Global.showProgressDialog(this@ChooseFrameForPhotoActivityNew)
        apiManager!!.setPrefernces(
            ApiEndpoints.setpreference,
            Global.getPreference(Constant.PREF_TOKEN, ""),
            isDelete,
            prefernceValue
        )
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
            if (tempEnteredText == "") {
                tvtextcolor!!.setBackgroundResource(R.drawable.small_button_bg)
                tvfonttype!!.setBackgroundResource(R.drawable.small_button_bg)
                tvtextcolor!!.isEnabled = true
                tvfonttype!!.isEnabled = true
            } else {
                tvtextcolor!!.isEnabled = true
                tvfonttype!!.isEnabled = true
                tvtextcolor!!.setBackgroundResource(R.drawable.small_button_bg)
                tvfonttype!!.setBackgroundResource(R.drawable.small_button_bg)
            }
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
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_frame)
        tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
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
            if (!Global.getPreference(Constant.PREF_PREMIUM, false)) {
                llwatermark!!.visibility = View.VISIBLE
            } else {
                llwatermark!!.visibility = View.GONE
            }
            Global.showProgressDialog(this@ChooseFrameForPhotoActivityNew)
            val handler = Handler()
            handler.postDelayed({
                Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)
                layroot!!.isDrawingCacheEnabled = true
                layroot!!.buildDrawingCache(true)
                val savedBmp = Bitmap.createBitmap(
                    layroot!!.drawingCache
                )
                layroot!!.isDrawingCacheEnabled = false
                try {
                    //Write file
                    val filename = "bitmap.png"
                    val stream = openFileOutput(filename, MODE_PRIVATE)
                    savedBmp.compress(Bitmap.CompressFormat.PNG, 100, stream)

                    //Cleanup
                    stream.close()
                    savedBmp.recycle()

                    //Pop intent
                    val in1 = Intent(
                        this@ChooseFrameForPhotoActivityNew,
                        SaveAndShareActivity::class.java
                    )
                    in1.putExtra("image", filename)
                    startActivity(in1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 1500)
        }
        animateButton()
    }

    fun animateButton() {
        val myAnim =
            AnimationUtils.loadAnimation(this@ChooseFrameForPhotoActivityNew, R.anim.bounce)
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        tvaction!!.startAnimation(myAnim)
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

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@ChooseFrameForPhotoActivityNew)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)
            if (requestService.equals(ApiEndpoints.setpreference, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        setFrameNEW(framePreview)
                    } else {
                        Global.showFailDialog(this@ChooseFrameForPhotoActivityNew, message)
                    }
                } catch (e: Exception) {
                }
            }
        }
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

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@ChooseFrameForPhotoActivityNew)
            Global.showFailDialog(this@ChooseFrameForPhotoActivityNew, responseString)
        }
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
        }*/imagemultiTouchListenerNew.setOnGestureControl(object :
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
                ivphotoclose!!.setVisibility(View.GONE)
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
        val businessItem = Global.currentBusiness
        if (businessItem != null) {
            try {
                if (index1 == 0) {
                    if (businessItem.busiName != "" && businessItem.busiName != null) {
                        linearName!!.setVisibility(View.VISIBLE)
                        frameName!!.setVisibility(View.VISIBLE)
                        tvframename!!.setVisibility(View.VISIBLE)
                        tvframename!!.setText(businessItem.busiName)
                        ivnameSelect!!.setImageResource(R.drawable.businessname_select)
                    }
                    if (businessItem.busiLogo != "" && businessItem.busiLogo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this@ChooseFrameForPhotoActivityNew).load(businessItem.busiLogo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busiMobile != null && businessItem.busiMobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busiMobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                } else if (index1 == 2) {
                    if (businessItem.busiName != "" && businessItem.busiName != null) {
                        linearName!!.setVisibility(View.VISIBLE)
                        frameName!!.setVisibility(View.VISIBLE)
                        tvframename!!.setVisibility(View.VISIBLE)
                        tvframename!!.setText(businessItem.busiName)
                        ivnameSelect!!.setImageResource(R.drawable.businessname_select)
                    }
                    if (businessItem.busiLogo != "" && businessItem.busiLogo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this@ChooseFrameForPhotoActivityNew).load(businessItem.busiLogo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busiMobile != null && businessItem.busiMobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busiMobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busiAddress != null && businessItem.busiAddress != "") {
                        linearAddress!!.setVisibility(View.VISIBLE)
                        ivLocation!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setVisibility(View.VISIBLE)
                        frameAddress!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setText(businessItem.busiAddress)
                        ivaddressselect!!.setImageResource(R.drawable.address_select)
                    }
                    if (businessItem.busiEmail != null && businessItem.busiEmail != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busiEmail)
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busiWebsite != null && businessItem.busiWebsite != "") {
                        linearWebsite!!.setVisibility(View.VISIBLE)
                        ivWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setVisibility(View.VISIBLE)
                        frameWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setText(businessItem.busiWebsite)
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                } else if (index1 == 3) {
                    if (businessItem.busiLogo != "" && businessItem.busiLogo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this@ChooseFrameForPhotoActivityNew).load(businessItem.busiLogo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busiMobile != null && businessItem.busiMobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busiMobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busiEmail != null && businessItem.busiEmail != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busiEmail)
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                } else if (index1 == 4) {
                    if (businessItem.busiName != "" && businessItem.busiName != null) {
                        linearName!!.setVisibility(View.VISIBLE)
                        frameName!!.setVisibility(View.VISIBLE)
                        tvframename!!.setVisibility(View.VISIBLE)
                        tvframename!!.setText(businessItem.busiName)
                        ivnameSelect!!.setImageResource(R.drawable.businessname_select)
                    }
                    if (businessItem.busiLogo != "" && businessItem.busiLogo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this@ChooseFrameForPhotoActivityNew).load(businessItem.busiLogo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busiMobile != null && businessItem.busiMobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busiMobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busiEmail != null && businessItem.busiEmail != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busiEmail)
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
                    if (businessItem.busiLogo != "" && businessItem.busiLogo != null) {
                        linearLogo!!.setVisibility(View.VISIBLE)
                        ivframelogo!!.setVisibility(View.VISIBLE)
                        frameLogo!!.setVisibility(View.VISIBLE)
                        ivlogoselect!!.setImageResource(R.drawable.logo_select)
                        Glide.with(this@ChooseFrameForPhotoActivityNew).load(businessItem.busiLogo)
                            .into(ivframelogo!!)
                    }
                    if (businessItem.busiMobile != null && businessItem.busiMobile != "") {
                        framePhone!!.setVisibility(View.VISIBLE)
                        linearPhone!!.setVisibility(View.VISIBLE)
                        ivcall!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setVisibility(View.VISIBLE)
                        tvframephone!!.setText(businessItem.busiMobile)
                        ivphoneselect!!.setImageResource(R.drawable.mobile_select)
                    }
                    if (businessItem.busiAddress != null && businessItem.busiAddress != "") {
                        linearAddress!!.setVisibility(View.VISIBLE)
                        ivLocation!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setVisibility(View.VISIBLE)
                        frameAddress!!.setVisibility(View.VISIBLE)
                        tvframelocation!!.setText(businessItem.busiAddress)
                        ivaddressselect!!.setImageResource(R.drawable.address_select)
                    }
                    if (businessItem.busiEmail != null && businessItem.busiEmail != "") {
                        linearEmail!!.setVisibility(View.VISIBLE)
                        ivEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setVisibility(View.VISIBLE)
                        frameEmail!!.setVisibility(View.VISIBLE)
                        tvframeemail!!.setText(businessItem.busiEmail)
                        ivemailselect!!.setImageResource(R.drawable.email_select)
                    }
                    if (businessItem.busiWebsite != null && businessItem.busiWebsite != "") {
                        linearWebsite!!.setVisibility(View.VISIBLE)
                        ivWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setVisibility(View.VISIBLE)
                        frameWebsite!!.setVisibility(View.VISIBLE)
                        tvframeweb!!.setText(businessItem.busiWebsite)
                        ivwebsiteselect!!.setImageResource(R.drawable.website_select)
                    }
                }
            } catch (e: Exception) {
            }
            ivphoneclose!!.setOnClickListener(View.OnClickListener {
                framePhone!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivphoneselect!!.setImageResource(R.drawable.mobile_light)
            })
            ivphotoclose!!.setOnClickListener(View.OnClickListener {
                frameLogo!!.setVisibility(View.GONE)
                ivphotoclose!!.setVisibility(View.GONE)
                ivlogoselect!!.setImageResource(R.drawable.logo_light)
            })
            ivemailclose!!.setOnClickListener(View.OnClickListener {
                frameEmail!!.setVisibility(View.GONE)
                ivemailclose!!.setVisibility(View.GONE)
                ivemailselect!!.setImageResource(R.drawable.email_light)
            })
            ivwebsiteclose!!.setOnClickListener(View.OnClickListener {
                frameWebsite!!.setVisibility(View.GONE)
                ivwebsiteclose!!.setVisibility(View.GONE)
                ivwebsiteselect!!.setImageResource(R.drawable.website_light)
            })
            ivaddressclose!!.setOnClickListener(View.OnClickListener {
                frameAddress!!.setVisibility(View.GONE)
                ivaddressclose!!.setVisibility(View.GONE)
                ivaddressselect!!.setImageResource(R.drawable.address_light)
            })
            ivnameClose!!.setOnClickListener(View.OnClickListener {
                frameName!!.setVisibility(View.GONE)
                ivnameClose!!.setVisibility(View.GONE)
                ivnameSelect!!.setImageResource(R.drawable.businessname_light)
            })
        }
        llframe!!.addView(frame_view)
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
            if (jsonObject.has("current_business")) {
                val businessJsonObject = jsonObject.getJSONObject("current_business")
                Global.storeCurrentBusiness(businessJsonObject.toString())
            } else {
                Global.storeCurrentBusiness("")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showColorPickerDialog() {
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
            if (nameValue) {
                namecolor = colorPickerView.color
                tvframename!!.setTextColor(namecolor)
            } else if (textallSelected) {
                allselectedcolor = colorPickerView.color
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
                phoneselected_color = colorPickerView.color
                tvframephone!!.setTextColor(phoneselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ivcall!!.backgroundTintList = ColorStateList.valueOf(phoneselected_color)
                }
            } else if (emailValue) {
                emailselected_color = colorPickerView.color
                tvframeemail!!.setTextColor(emailselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ivEmail!!.backgroundTintList = ColorStateList.valueOf(emailselected_color)
                }
            } else if (websiteValue) {
                websiteselected_color = colorPickerView.color
                tvframeweb!!.setTextColor(websiteselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ivWebsite!!.backgroundTintList = ColorStateList.valueOf(websiteselected_color)
                }
            } else if (addressValue) {
                addressselected_color = colorPickerView.color
                tvframelocation!!.setTextColor(addressselected_color)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ivLocation!!.backgroundTintList = ColorStateList.valueOf(addressselected_color)
                }
            } else {
                selected_color = colorPickerView.color
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
            //rootTextView=null;
            dialog.dismiss()
        }
        btncancel.setOnClickListener {
            selected_color = R.color.colorBlack
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showFonttypeDialog(defaultText: String?) {
        val dialog = Dialog(
            this@ChooseFrameForPhotoActivityNew,
            R.style.DialogAnimation
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.setContentView(R.layout.custom_choose_font_dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val tvnamefont1 = dialog.findViewById<View>(R.id.tvnamefont1) as TextView
        val tvnamefont2 = dialog.findViewById<View>(R.id.tvnamefont2) as TextView
        val tvnamefont3 = dialog.findViewById<View>(R.id.tvnamefont3) as TextView
        val tvnamefont4 = dialog.findViewById<View>(R.id.tvnamefont4) as TextView
        val tvnamefont5 = dialog.findViewById<View>(R.id.tvnamefont5) as TextView
        val tvnamefont6 = dialog.findViewById<View>(R.id.tvnamefont6) as TextView
        val tvnamefont7 = dialog.findViewById<View>(R.id.tvnamefont7) as TextView
        val tvnamefont8 = dialog.findViewById<View>(R.id.tvnamefont8) as TextView
        val tvnamefont9 = dialog.findViewById<View>(R.id.tvnamefont9) as TextView
        val tvnamefont10 = dialog.findViewById<View>(R.id.tvnamefont10) as TextView
        val tvnamefont11 = dialog.findViewById<View>(R.id.tvnamefont11) as TextView
        val tvnamefont12 = dialog.findViewById<View>(R.id.tvnamefont12) as TextView
        val tvnamefont13 = dialog.findViewById<View>(R.id.tvnamefont13) as TextView
        val tvnamefont14 = dialog.findViewById<View>(R.id.tvnamefont14) as TextView
        val tvnamefont15 = dialog.findViewById<View>(R.id.tvnamefont15) as TextView
        val tvnamefont16 = dialog.findViewById<View>(R.id.tvnamefont16) as TextView
        val tvnamefont17 = dialog.findViewById<View>(R.id.tvnamefont17) as TextView
        val tvnamefont18 = dialog.findViewById<View>(R.id.tvnamefont18) as TextView
        val tvnamefont19 = dialog.findViewById<View>(R.id.tvnamefont19) as TextView
        val tvnamefont20 = dialog.findViewById<View>(R.id.tvnamefont20) as TextView
        val tvnamefont21 = dialog.findViewById<View>(R.id.tvnamefont21) as TextView
        val tvnamefont22 = dialog.findViewById<View>(R.id.tvnamefont22) as TextView
        tvnamefont1.text = defaultText
        tvnamefont2.text = defaultText
        tvnamefont3.text = defaultText
        tvnamefont4.text = defaultText
        tvnamefont5.text = defaultText
        tvnamefont6.text = defaultText
        tvnamefont7.text = defaultText
        tvnamefont8.text = defaultText
        tvnamefont9.text = defaultText
        tvnamefont10.text = defaultText
        tvnamefont11.text = defaultText
        tvnamefont12.text = defaultText
        tvnamefont13.text = defaultText
        tvnamefont14.text = defaultText
        tvnamefont15.text = defaultText
        tvnamefont16.text = defaultText
        tvnamefont17.text = defaultText
        tvnamefont18.text = defaultText
        tvnamefont19.text = defaultText
        tvnamefont20.text = defaultText
        tvnamefont21.text = defaultText
        tvnamefont22.text = defaultText
        tvnamefont1.typeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
        tvnamefont2.typeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
        tvnamefont3.typeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
        tvnamefont4.typeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
        tvnamefont5.typeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
        tvnamefont6.typeface = Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
        tvnamefont7.typeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
        tvnamefont8.typeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
        tvnamefont9.typeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
        tvnamefont10.typeface = Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
        tvnamefont11.typeface = Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
        tvnamefont12.typeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
        tvnamefont13.typeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
        tvnamefont14.typeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
        tvnamefont15.typeface = Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
        tvnamefont16.typeface = Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
        tvnamefont17.typeface = Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
        tvnamefont18.typeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
        tvnamefont19.typeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
        tvnamefont20.typeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
        tvnamefont21.typeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
        tvnamefont22.typeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
        tvnamefont1.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
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
            dialog.dismiss()
        }
        tvnamefont2.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
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
            dialog.dismiss()
        }
        tvnamefont3.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                if (rootTextView != null) for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        Log.d("SelectedColor111", "" + views[p].color)
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
            dialog.dismiss()
        }
        tvnamefont4.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                if (rootTextView != null) for (p in views.indices) {
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
            dialog.dismiss()
        }
        tvnamefont5.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
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
            dialog.dismiss()
        }
        tvnamefont6.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
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
            dialog.dismiss()
        }
        tvnamefont7.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
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
            dialog.dismiss()
        }
        tvnamefont8.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
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
            dialog.dismiss()
        }
        tvnamefont9.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
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
            dialog.dismiss()
        }
        tvnamefont10.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/babelsans_oblique.ttf")
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
            dialog.dismiss()
        }
        tvnamefont11.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/brushstrike_trial.ttf")
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
            dialog.dismiss()
        }
        tvnamefont12.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
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
            dialog.dismiss()
        }
        tvnamefont13.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
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
            dialog.dismiss()
        }
        tvnamefont14.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
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
            dialog.dismiss()
        }
        tvnamefont15.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/minecraftia_regular.ttf")
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
            dialog.dismiss()
        }
        tvnamefont16.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/something_strange.ttf")
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
            dialog.dismiss()
        }
        tvnamefont17.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/sf_foxboro_script.ttf")
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
            dialog.dismiss()
        }
        tvnamefont18.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
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
            dialog.dismiss()
        }
        tvnamefont19.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
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
            dialog.dismiss()
        }
        tvnamefont20.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
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
            dialog.dismiss()
        }
        tvnamefont21.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
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
            dialog.dismiss()
        }
        tvnamefont22.setOnClickListener {
            if (nameValue) {
                nametypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframename!!.typeface = nametypeface
            } else if (textallSelected) {
                allfonttypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframephone!!.typeface = allfonttypeface
                tvframeemail!!.typeface = allfonttypeface
                tvframeweb!!.typeface = allfonttypeface
                tvframelocation!!.typeface = allfonttypeface
                tvframename!!.typeface = allfonttypeface
            } else if (phoneValue) {
                phoneTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframephone!!.typeface = phoneTypeface
            } else if (emailValue) {
                emailTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframeemail!!.typeface = emailTypeface
            } else if (websiteValue) {
                websiteTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframeweb!!.typeface = websiteTypeface
            } else if (addressValue) {
                addressTypeface = Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                tvframelocation!!.typeface = addressTypeface
            } else {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
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
            dialog.dismiss()
        }
        val dialogButton = dialog.findViewById<View>(R.id.btndone) as TextView
        dialogButton.setOnClickListener { /*selectedFontTypeface = Typeface.createFromAsset(getAssets(), "fonts/open_sans_regular.ttf");
                    for (int p=0;p<views.size();p++) {

                        if (p == selectedPosition) {
                            rootTextView = views.get(p).getView();
                            if (rootTextView != null) {
                                mPhotoEditor.editText(rootTextView, selectedFontTypeface, views.get(p).getText(), selected_color);
                            }
                        }
                    }*/
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onItemClicked(`object`: Any?, index: Int) {
        Log.d("index123", "" + index)
        val photoItem = `object` as FramePreview
        index1 = index
        setFrameNEW(photoItem)
        if (index >= 19) {
            if (photoItem.dynamic_images != null && !photoItem.dynamic_images.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                //setFrameNEW(photoItem);
                Glide.with(this@ChooseFrameForPhotoActivityNew).load(framePreview!!.dynamic_images)
                    .placeholder(
                        R.drawable.placeholder_img
                    ).error(R.drawable.placeholder_img).into(
                        ivframebg!!
                    )
            }
        }
    }
}