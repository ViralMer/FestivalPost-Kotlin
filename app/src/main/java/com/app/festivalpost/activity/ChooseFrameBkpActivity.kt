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
        selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
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
                shotFonttype(defaultText)
            }
        })
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        val horizontalLayoutManagaer =
            LinearLayoutManager(this@ChooseFrameBkpActivity, LinearLayoutManager.HORIZONTAL, false)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        //        rvdata.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(12), true));
        rvdata!!.itemAnimator = DefaultItemAnimator()
        Global.showProgressDialog(this@ChooseFrameBkpActivity)
        apiManager!!.getTemplates(
            ApiEndpoints.getTemplates,
            Global.getPreference(Constant.PREF_TOKEN, "")
        )
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

    fun shotFonttype(defaultText: String?) {
        val dialog = Dialog(
            this@ChooseFrameBkpActivity,
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
        tvnamefont1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                edtext.typeface = selectedFontTypeface
                dialog.dismiss()
            }
        })
        tvnamefont2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/anton_regular.ttf")
                edtext.typeface = selectedFontTypeface
                dialog.dismiss()
            }
        })
        tvnamefont3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/museomoderno_regular.ttf")
                edtext.typeface = selectedFontTypeface
                dialog.dismiss()
            }
        })
        tvnamefont4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/notable_regular.ttf")
                edtext.typeface = selectedFontTypeface
                dialog.dismiss()
            }
        })
        tvnamefont5.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                edtext.typeface = selectedFontTypeface
                dialog.dismiss()
            }
        })
        tvnamefont6.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/yellowtail_regular.ttf")
                edtext.typeface = selectedFontTypeface
                dialog.dismiss()
            }
        })
        tvnamefont7.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/aaargh.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont8.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/alice_wiker.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont9.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/armopb.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont10.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
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
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont11.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
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
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont12.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/chinesetakeaway.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont13.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/jedi.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont14.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                Typeface.createFromAsset(assets, "fonts/itromatic_bold.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont15.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
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
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont16.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
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
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont17.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
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
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont18.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/piedra_regular.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont19.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/squitcher.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont20.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/unicorn.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont21.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                selectedFontTypeface = Typeface.createFromAsset(assets, "fonts/xo.ttf")
                for (p in views.indices) {
                    if (p == selectedPosition) {
                        rootTextView = views[p].view
                        if (rootTextView != null) {
                            mPhotoEditor!!.editText(
                                rootTextView!!,
                                selectedFontTypeface,
                                views[p].text,
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        tvnamefont22.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
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
                                selected_color
                            )
                        }
                    }
                }
                dialog.dismiss()
            }
        })
        val dialogButton = dialog.findViewById<View>(R.id.btndone) as TextView
        dialogButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                selectedFontTypeface =
                    Typeface.createFromAsset(assets, "fonts/open_sans_regular.ttf")
                edtext.typeface = selectedFontTypeface
                dialog.dismiss()
            }
        })
        dialog.show()
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