package com.app.festivalpost.activity

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.adapter.ChooseFrameAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.FrameItem
import com.app.festivalpost.models.ViewData
import com.app.festivalpost.photoeditor.OnPhotoEditorListener
import com.app.festivalpost.photoeditor.PhotoEditor
import com.app.festivalpost.photoeditor.PhotoEditorView
import com.app.festivalpost.photoeditor.ViewType
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import org.json.JSONObject
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.io.IOException
import java.util.*

class ChooseFrameBkpActivity() : AppCompatActivity(), ApiResponseListener,OnItemClickListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var dataArrayList: ArrayList<FrameItem>? = null
    var selectedPosition = 0
    var rvdata: RecyclerView? = null
    private var photoEditorView: PhotoEditorView? = null
    private var frameaddtext: FrameLayout? = null
    private var btnaddtextdone: TextView? = null
    private var tvaddtext: TextView? = null
    private var tvaddimage: TextView? = null
    private var tvtextcolor: TextView? = null
    private var tvfonttype: TextView? = null
    var layroot: LinearLayout? = null
    var views: List<ViewData> = ArrayList()
    var mPhotoEditor: PhotoEditor? = null
    private val edtext: EditText
        private get() = findViewById<View>(R.id.edtext) as EditText
    var selected_color = Color.BLACK
    var rootTextView: View? = null
    var selectedFontTypeface: Typeface? = null
    var ivframe: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_choose_frame_bkp)
        apiManager = ApiManager(this@ChooseFrameBkpActivity)
        setActionbar()
        selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/aileron_light.otf")
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        ivframe = findViewById<View>(R.id.ivframe) as ImageView
        photoEditorView = findViewById<View>(R.id.photoEditorView) as PhotoEditorView
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView!!)
            .setPinchTextScalable(true)
            .build()
        mPhotoEditor!!.setOnPhotoEditorListener(object : OnPhotoEditorListener {
            override fun onEditTextChangeListener(
                rootView: View?,
                text: String?,
                colorCode: Int,
                viewList: Int
            ) {
                rootTextView = rootView
                selected_color = colorCode
                edtext.setText(text)
                edtext.setTextColor(colorCode)
                frameaddtext!!.visibility = View.VISIBLE
            }

            override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {}
            override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {}
            override fun onStartViewChangeListener(
                viewType: ViewType?,
                numberOfAddedViews: Int,
                view: View?
            ) {
            }

            override fun onStopViewChangeListener(
                viewType: ViewType?,
                numberOfAddedViews: Int,
                view: View?
            ) {
            }
        })
        frameaddtext = findViewById<View>(R.id.frameaddtext) as FrameLayout
        btnaddtextdone = findViewById<View>(R.id.btnaddtextdone) as TextView
        tvaddtext = findViewById<View>(R.id.tvaddtext) as TextView
        tvaddimage = findViewById<View>(R.id.tvaddimage) as TextView
        tvtextcolor = findViewById<View>(R.id.tvtextcolor) as TextView
        tvfonttype = findViewById<View>(R.id.tvfonttype) as TextView
        findViewById<View>(R.id.btnchangebackgroundcolor).setOnClickListener(
            View.OnClickListener { view ->
                ColorPickerPopup.Builder(this@ChooseFrameBkpActivity)
                    .initialColor(Color.RED) // Set initial color
                    .enableBrightness(false) // Enable brightness slider or not
                    .enableAlpha(false) // Enable alpha slider or not
                    .okTitle("Choose")
                    .cancelTitle("Cancel")
                    .showValue(false)
                    .showIndicator(true)
                    .build()
                    .show(view, object : ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            layroot!!.setBackgroundColor(color)
                        }
                    })
            })
        btnaddtextdone!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val tempSelectedColor = selected_color
                val tempEnteredText = edtext.text.toString()
                if (mPhotoEditor != null && !tempEnteredText.equals("", ignoreCase = true)) {
                    if (rootTextView == null) {
                        rootTextView = mPhotoEditor!!.addText(
                            selectedFontTypeface,
                            tempEnteredText,
                            tempSelectedColor
                        )
                    } else {
                        mPhotoEditor!!.editText(
                            rootTextView!!,
                            selectedFontTypeface,
                            tempEnteredText,
                            tempSelectedColor
                        )
                    }
                }
                rootTextView = null
                selected_color = Color.BLACK
                edtext.setText("")
                frameaddtext!!.visibility = View.GONE
                Global.hideSoftKeyboard(this@ChooseFrameBkpActivity, edtext)
            }
        })
        tvaddimage!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                Dexter.withActivity(this@ChooseFrameBkpActivity)
                    .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                            if (report.areAllPermissionsGranted()) {
                                val builder = AlertDialog.Builder(this@ChooseFrameBkpActivity)
                                builder.setTitle("Choose")
                                val animals = arrayOf("Camera", "Gallery")
                                builder.setItems(animals, object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface, which: Int) {
                                        when (which) {
                                            0 -> startCamera()
                                            1 -> startGallery()
                                        }
                                    }
                                })
                                val dialog = builder.create()
                                dialog.show()
                            } else {
                                // TODO - handle permission denied case
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
        })
        tvaddtext!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                edtext.setText("")
                selected_color = Color.BLACK
                edtext.setTextColor(selected_color)
                frameaddtext!!.visibility = View.VISIBLE
            }
        })
        tvtextcolor!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                ColorPickerPopup.Builder(this@ChooseFrameBkpActivity)
                    .initialColor(Color.RED) // Set initial color
                    .enableBrightness(false) // Enable brightness slider or not
                    .enableAlpha(false) // Enable alpha slider or not
                    .okTitle("Choose")
                    .cancelTitle("Cancel")
                    .showValue(false)
                    .showIndicator(true)
                    .build()
                    .show(view, object : ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            selected_color = color
                            edtext.setTextColor(color)
                        }
                    })
            }
        })
        tvfonttype!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                var defaultText = edtext.text.toString()
                if (defaultText.equals("", ignoreCase = true)) {
                    defaultText = "Custom Text"
                }
                //shotFonttype(defaultText)
            }
        })
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        val horizontalLayoutManagaer =
            LinearLayoutManager(this@ChooseFrameBkpActivity, LinearLayoutManager.HORIZONTAL, false)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        //        rvdata.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(12), true));
        rvdata!!.itemAnimator = DefaultItemAnimator()
        Global.showProgressDialog(this@ChooseFrameBkpActivity)

    }

    fun startCamera() {
        PickerBuilder(this@ChooseFrameBkpActivity, PickerBuilder.SELECT_FROM_CAMERA)
            .setOnImageReceivedListener(object : PickerBuilder.onImageReceivedListener {
                override fun onImageReceived(imageUri: Uri?) {
                    Toast.makeText(
                        this@ChooseFrameBkpActivity,
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
        PickerBuilder(this@ChooseFrameBkpActivity, PickerBuilder.SELECT_FROM_GALLERY)
            .setOnImageReceivedListener(object : PickerBuilder.onImageReceivedListener {
                override fun onImageReceived(imageUri: Uri?) {
                    Toast.makeText(
                        this@ChooseFrameBkpActivity,
                        "Got image - $imageUri",
                        Toast.LENGTH_LONG
                    ).show()
                    addImage(imageUri)
                }
            })
            .setImageName("test")
            .setImageFolderName(resources.getString(R.string.app_name))
            .setCropScreenColor(resources.getColor(R.color.colorPrimary))
            .setOnPermissionRefusedListener(object : PickerBuilder.onPermissionRefusedListener {
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

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_frame)
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction.text = resources.getString(R.string.txt_next)
        tvaction.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                mPhotoEditor!!.clearHelperBox()
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
                    val in1 = Intent(this@ChooseFrameBkpActivity, SaveAndShareActivity::class.java)
                    in1.putExtra("image", filename)
                    startActivity(in1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                Global.dismissProgressDialog(this@ChooseFrameBkpActivity)
                if (!isConnected) {
                    Global.noInternetConnectionDialog(this@ChooseFrameBkpActivity)
                }
            }
        })
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                Global.dismissProgressDialog(this@ChooseFrameBkpActivity)
                if (requestService.equals(ApiEndpoints.getTemplates, ignoreCase = true)) {
                    try {
                        Log.d("response", (responseString)!!)
                        processFrameResponse(responseString)
                        if (status) {
                            filldata()
                        } else {
                            Toast.makeText(this@ChooseFrameBkpActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }

    private fun filldata() {
        if (dataArrayList!!.size > 0) {
            val adapter = ChooseFrameAdapter(this@ChooseFrameBkpActivity, (dataArrayList)!!)
            rvdata!!.adapter = adapter
        } else {
            Toast.makeText(this@ChooseFrameBkpActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post(object : Runnable {
            override fun run() {
                Global.dismissProgressDialog(this@ChooseFrameBkpActivity)
                Global.showFailDialog(this@ChooseFrameBkpActivity, responseString)
            }
        })
    }

    override fun onItemClicked(`object`: Any?, index: Int) {
        val frameItem = `object` as FrameItem
        if (frameItem.img_url != "") {
            Glide.with(this@ChooseFrameBkpActivity).load(frameItem.img_url)
                .placeholder(R.drawable.placeholder_img).error(
                    R.drawable.placeholder_img
                ).into(
                    (ivframe)!!
                )
        }
    }



    fun processFrameResponse(responseString: String?) {
        dataArrayList = ArrayList()
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
            if (jsonObject.has("data")) {
                val jsonArray = jsonObject.getJSONArray("data")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val url = jsonArray.getString(i)
                        val f = FrameItem()
                        f.img_url = url
                        dataArrayList!!.add(f)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }





}