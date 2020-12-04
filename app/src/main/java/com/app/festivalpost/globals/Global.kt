package com.app.festivalpost.globals

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.festivalpost.R
import com.app.festivalpost.models.LocalFrameItem
import com.app.festivalpost.models.FramePreview
import com.app.festivalpost.models.LocalFrameItemNew
import com.app.festivalpost.activity.MyApplication
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.models.BusinessItem
import com.google.gson.Gson
import com.app.festivalpost.models.PreferenceItem
import com.app.festivalpost.models.FrameListItem
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest
import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Pattern
import kotlin.experimental.and

object Global {
    val isEmulator: Boolean
        get() {
            var inEmulator = false
            val brand = Build.BRAND
            Log.d("Brand", brand)
            if (brand.compareTo("generic") == 0) {
                inEmulator = true
            }
            return inEmulator
        }

    val allFrames: ArrayList<LocalFrameItem>
        get() {
            val localFrameItemArrayList = ArrayList<LocalFrameItem>()
            //        localFrameItemArrayList.add(new LocalFrameItem(R.layout.custom_frame_2, R.layout.custom_frame_preview_2));
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_layout_dynamic,
                    R.layout.custom_frame_dynamic
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_18,
                    R.layout.custom_frame_preview_18
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_19,
                    R.layout.custom_frame_preview_19
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_20,
                    R.layout.custom_frame_preview_20
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_21,
                    R.layout.custom_frame_preview_21
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_1,
                    R.layout.custom_frame_preview_1
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_4,
                    R.layout.custom_frame_preview_4
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_5,
                    R.layout.custom_frame_preview_5
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_6,
                    R.layout.custom_frame_preview_6
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_7,
                    R.layout.custom_frame_preview_7
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_8,
                    R.layout.custom_frame_preview_8
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_9,
                    R.layout.custom_frame_preview_1
                )
            )
            //        localFrameItemArrayList.add(new LocalFrameItem(R.layout.custom_frame_10, R.layout.custom_frame_preview_2));
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_11,
                    R.layout.custom_frame_preview_3
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_12,
                    R.layout.custom_frame_preview_4
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_13,
                    R.layout.custom_frame_preview_5
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_14,
                    R.layout.custom_frame_preview_6
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_15,
                    R.layout.custom_frame_preview_7
                )
            )
            localFrameItemArrayList.add(
                LocalFrameItem(
                    R.layout.custom_frame_16,
                    R.layout.custom_frame_preview_8
                )
            )
            return localFrameItemArrayList
        }

    /*framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_22,R.drawable.frame_22));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_23,R.drawable.frame_23));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_24,R.drawable.frame_24));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_25,R.drawable.frame_25));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_26,R.drawable.frame_26));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_27,R.drawable.frame_27));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_28,R.drawable.frame_28));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_29,R.drawable.frame_29));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_30,R.drawable.frame_30));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_31,R.drawable.frame_31));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_32,R.drawable.frame_32));*/
    val newFrames: ArrayList<FramePreview>
        get() {
            val framePreviewArrayList = ArrayList<FramePreview>()
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_17, R.drawable.frame_17))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_18, R.drawable.frame_18))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_19, R.drawable.frame_19))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_20, R.drawable.frame_20))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_21, R.drawable.frame_21))
            /*framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_22,R.drawable.frame_22));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_23,R.drawable.frame_23));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_24,R.drawable.frame_24));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_25,R.drawable.frame_25));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_26,R.drawable.frame_26));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_27,R.drawable.frame_27));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_28,R.drawable.frame_28));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_29,R.drawable.frame_29));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_30,R.drawable.frame_30));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_31,R.drawable.frame_31));
        framePreviewArrayList.add(new FramePreview(R.layout.custom_frame_32,R.drawable.frame_32));*/framePreviewArrayList.add(
                FramePreview(
                    R.layout.custom_frame_1, R.drawable.frame_01
                )
            )
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_3, R.drawable.frame_03))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_4, R.drawable.frame_04))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_5, R.drawable.frame_05))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_6, R.drawable.frame_06))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_7, R.drawable.frame_07))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_8, R.drawable.frame_08))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_9, R.drawable.frame_01))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_11, R.drawable.frame_03))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_12, R.drawable.frame_04))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_13, R.drawable.frame_05))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_14, R.drawable.frame_06))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_15, R.drawable.frame_07))
            framePreviewArrayList.add(FramePreview(R.layout.custom_frame_16, R.drawable.frame_08))
            return framePreviewArrayList
        }
    val allFramesNew: ArrayList<LocalFrameItemNew>
        get() {
            val localFrameItemArrayList = ArrayList<LocalFrameItemNew>()
            val mInflater1 = LayoutInflater.from(MyApplication.appContext)
            val mInflater3 = LayoutInflater.from(MyApplication.appContext)
            val mInflater4 = LayoutInflater.from(MyApplication.appContext)
            val mInflater5 = LayoutInflater.from(MyApplication.appContext)
            val mInflater6 = LayoutInflater.from(MyApplication.appContext)
            val mInflater7 = LayoutInflater.from(MyApplication.appContext)
            val mInflater8 = LayoutInflater.from(MyApplication.appContext)
            val mInflater9 = LayoutInflater.from(MyApplication.appContext)
            val mInflater11 = LayoutInflater.from(MyApplication.appContext)
            val mInflater12 = LayoutInflater.from(MyApplication.appContext)
            val mInflater13 = LayoutInflater.from(MyApplication.appContext)
            val mInflater14 = LayoutInflater.from(MyApplication.appContext)
            val mInflater15 = LayoutInflater.from(MyApplication.appContext)
            val mInflater16 = LayoutInflater.from(MyApplication.appContext)
            val mInflater17 = LayoutInflater.from(MyApplication.appContext)
            val mInflater18 = LayoutInflater.from(MyApplication.appContext)
            val mInflater19 = LayoutInflater.from(MyApplication.appContext)
            val mInflater20 = LayoutInflater.from(MyApplication.appContext)
            val mInflater21 = LayoutInflater.from(MyApplication.appContext)
            val mInflater22 = LayoutInflater.from(MyApplication.appContext)
            val mInflater23 = LayoutInflater.from(MyApplication.appContext)
            val mInflater24 = LayoutInflater.from(MyApplication.appContext)
            val mInflater25 = LayoutInflater.from(MyApplication.appContext)
            val mInflater26 = LayoutInflater.from(MyApplication.appContext)
            val mInflater27 = LayoutInflater.from(MyApplication.appContext)
            val mInflater28 = LayoutInflater.from(MyApplication.appContext)
            val mInflater29 = LayoutInflater.from(MyApplication.appContext)
            val mInflater30 = LayoutInflater.from(MyApplication.appContext)
            val mInflater31 = LayoutInflater.from(MyApplication.appContext)
            val mInflater32 = LayoutInflater.from(MyApplication.appContext)
            val child1 = mInflater1.inflate(R.layout.custom_frame_preview_1, null)
            val child3 = mInflater3.inflate(R.layout.custom_frame_preview_3, null)
            val child4 = mInflater4.inflate(R.layout.custom_frame_preview_4, null)
            val child5 = mInflater5.inflate(R.layout.custom_frame_preview_5, null)
            val child6 = mInflater6.inflate(R.layout.custom_frame_preview_6, null)
            val child7 = mInflater7.inflate(R.layout.custom_frame_preview_7, null)
            val child8 = mInflater8.inflate(R.layout.custom_frame_preview_8, null)
            val child9 = mInflater9.inflate(R.layout.custom_frame_preview_1, null)
            val child11 = mInflater11.inflate(R.layout.custom_frame_preview_3, null)
            val child12 = mInflater12.inflate(R.layout.custom_frame_preview_4, null)
            val child13 = mInflater13.inflate(R.layout.custom_frame_preview_5, null)
            val child14 = mInflater14.inflate(R.layout.custom_frame_preview_6, null)
            val child15 = mInflater15.inflate(R.layout.custom_frame_preview_7, null)
            val child16 = mInflater16.inflate(R.layout.custom_frame_preview_8, null)
            val child17 = mInflater17.inflate(R.layout.custom_frame_preview_17, null)
            val child18 = mInflater18.inflate(R.layout.custom_frame_preview_18, null)
            val child19 = mInflater19.inflate(R.layout.custom_frame_preview_19, null)
            val child20 = mInflater20.inflate(R.layout.custom_frame_preview_20, null)
            val child21 = mInflater21.inflate(R.layout.custom_frame_preview_21, null)
            val child22 = mInflater22.inflate(R.layout.custom_frame_preview_22, null)
            val child23 = mInflater23.inflate(R.layout.custom_frame_preview_23, null)
            val child24 = mInflater24.inflate(R.layout.custom_frame_preview_24, null)
            val child25 = mInflater25.inflate(R.layout.custom_frame_preview_25, null)
            val child26 = mInflater26.inflate(R.layout.custom_frame_preview_26, null)
            val child27 = mInflater27.inflate(R.layout.custom_frame_preview_27, null)
            val child28 = mInflater28.inflate(R.layout.custom_frame_preview_28, null)
            val child29 = mInflater29.inflate(R.layout.custom_frame_preview_29, null)
            val child30 = mInflater30.inflate(R.layout.custom_frame_preview_30, null)
            val child31 = mInflater31.inflate(R.layout.custom_frame_preview_31, null)
            val child32 = mInflater32.inflate(R.layout.custom_frame_preview_32, null)
            val frameLayout1 = child1.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout3 = child3.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout4 = child4.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout5 = child5.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout6 = child6.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout7 = child7.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout8 = child8.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout9 = child9.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout11 = child11.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout12 = child12.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout13 = child13.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout14 = child14.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout15 = child15.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout16 = child16.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout17 = child17.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout18 = child18.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout19 = child19.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout20 = child20.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout21 = child21.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout22 = child22.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout23 = child23.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout24 = child24.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout25 = child25.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout26 = child26.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout27 = child27.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout28 = child28.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout29 = child29.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout30 = child30.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout31 = child31.findViewById<FrameLayout>(R.id.frameLayout)
            val frameLayout32 = child32.findViewById<FrameLayout>(R.id.frameLayout)
            try {
                val params1 = FrameLayout.LayoutParams(325, 325)
                val params3 = FrameLayout.LayoutParams(325, 325)
                val params4 = FrameLayout.LayoutParams(325, 325)
                val params5 = FrameLayout.LayoutParams(325, 325)
                val params6 = FrameLayout.LayoutParams(325, 325)
                val params7 = FrameLayout.LayoutParams(325, 325)
                val params8 = FrameLayout.LayoutParams(325, 325)
                val params9 = FrameLayout.LayoutParams(325, 325)
                val params11 = FrameLayout.LayoutParams(325, 325)
                val params12 = FrameLayout.LayoutParams(325, 325)
                val params13 = FrameLayout.LayoutParams(325, 325)
                val params14 = FrameLayout.LayoutParams(325, 325)
                val params15 = FrameLayout.LayoutParams(325, 325)
                val params16 = FrameLayout.LayoutParams(325, 325)
                val params17 = FrameLayout.LayoutParams(325, 325)
                val params18 = FrameLayout.LayoutParams(325, 325)
                val params19 = FrameLayout.LayoutParams(325, 325)
                val params20 = FrameLayout.LayoutParams(325, 325)
                val params21 = FrameLayout.LayoutParams(325, 325)
                val params22 = FrameLayout.LayoutParams(325, 325)
                val params23 = FrameLayout.LayoutParams(325, 325)
                val params24 = FrameLayout.LayoutParams(325, 325)
                val params25 = FrameLayout.LayoutParams(325, 325)
                val params26 = FrameLayout.LayoutParams(325, 325)
                val params27 = FrameLayout.LayoutParams(325, 325)
                val params28 = FrameLayout.LayoutParams(325, 325)
                val params29 = FrameLayout.LayoutParams(325, 325)
                val params30 = FrameLayout.LayoutParams(325, 325)
                val params31 = FrameLayout.LayoutParams(325, 325)
                val params32 = FrameLayout.LayoutParams(325, 325)
                params1.width = dpToPx(130, MyApplication.appContext!!)
                params3.width = dpToPx(130, MyApplication.appContext!!)
                params4.width = dpToPx(130, MyApplication.appContext!!)
                params5.width = dpToPx(130, MyApplication.appContext!!)
                params6.width = dpToPx(130, MyApplication.appContext!!)
                params7.width = dpToPx(130, MyApplication.appContext!!)
                params8.width = dpToPx(130, MyApplication.appContext!!)
                params9.width = dpToPx(130, MyApplication.appContext!!)
                params11.width = dpToPx(130, MyApplication.appContext!!)
                params12.width = dpToPx(130, MyApplication.appContext!!)
                params13.width = dpToPx(130, MyApplication.appContext!!)
                params14.width = dpToPx(130, MyApplication.appContext!!)
                params15.width = dpToPx(130, MyApplication.appContext!!)
                params16.width = dpToPx(130, MyApplication.appContext!!)
                params17.width = dpToPx(130, MyApplication.appContext!!)
                params18.width = dpToPx(130, MyApplication.appContext!!)
                params19.width = dpToPx(130, MyApplication.appContext!!)
                params20.width = dpToPx(130, MyApplication.appContext!!)
                params21.width = dpToPx(130, MyApplication.appContext!!)
                params22.width = dpToPx(130, MyApplication.appContext!!)
                params23.width = dpToPx(130, MyApplication.appContext!!)
                params24.width = dpToPx(130, MyApplication.appContext!!)
                params25.width = dpToPx(130, MyApplication.appContext!!)
                params26.width = dpToPx(130, MyApplication.appContext!!)
                params27.width = dpToPx(130, MyApplication.appContext!!)
                params28.width = dpToPx(130, MyApplication.appContext!!)
                params29.width = dpToPx(130, MyApplication.appContext!!)
                params30.width = dpToPx(130, MyApplication.appContext!!)
                params31.width = dpToPx(130, MyApplication.appContext!!)
                params32.width = dpToPx(130, MyApplication.appContext!!)
                params1.height = dpToPx(130, MyApplication.appContext!!)
                params3.height = dpToPx(130, MyApplication.appContext!!)
                params4.height = dpToPx(130, MyApplication.appContext!!)
                params5.height = dpToPx(130, MyApplication.appContext!!)
                params6.height = dpToPx(130, MyApplication.appContext!!)
                params7.height = dpToPx(130, MyApplication.appContext!!)
                params8.height = dpToPx(130, MyApplication.appContext!!)
                params9.height = dpToPx(130, MyApplication.appContext!!)
                params11.height = dpToPx(130, MyApplication.appContext!!)
                params12.height = dpToPx(130, MyApplication.appContext!!)
                params13.height = dpToPx(130, MyApplication.appContext!!)
                params14.height = dpToPx(130, MyApplication.appContext!!)
                params15.height = dpToPx(130, MyApplication.appContext!!)
                params16.height = dpToPx(130, MyApplication.appContext!!)
                params17.height = dpToPx(130, MyApplication.appContext!!)
                params18.height = dpToPx(130, MyApplication.appContext!!)
                params19.height = dpToPx(130, MyApplication.appContext!!)
                params20.height = dpToPx(130, MyApplication.appContext!!)
                params21.height = dpToPx(130, MyApplication.appContext!!)
                params22.height = dpToPx(130, MyApplication.appContext!!)
                params23.height = dpToPx(130, MyApplication.appContext!!)
                params24.height = dpToPx(130, MyApplication.appContext!!)
                params25.height = dpToPx(130, MyApplication.appContext!!)
                params26.height = dpToPx(130, MyApplication.appContext!!)
                params27.height = dpToPx(130, MyApplication.appContext!!)
                params28.height = dpToPx(130, MyApplication.appContext!!)
                params29.height = dpToPx(130, MyApplication.appContext!!)
                params30.height = dpToPx(130, MyApplication.appContext!!)
                params31.height = dpToPx(130, MyApplication.appContext!!)
                params32.height = dpToPx(130, MyApplication.appContext!!)
                frameLayout1.layoutParams = params1
                frameLayout3.layoutParams = params3
                frameLayout4.layoutParams = params4
                frameLayout5.layoutParams = params5
                frameLayout6.layoutParams = params6
                frameLayout7.layoutParams = params7
                frameLayout8.layoutParams = params8
                frameLayout9.layoutParams = params9
                frameLayout11.layoutParams = params11
                frameLayout12.layoutParams = params12
                frameLayout13.layoutParams = params13
                frameLayout14.layoutParams = params14
                frameLayout15.layoutParams = params15
                frameLayout16.layoutParams = params16
                frameLayout17.layoutParams = params17
                frameLayout18.layoutParams = params18
                frameLayout19.layoutParams = params19
                frameLayout20.layoutParams = params20
                frameLayout21.layoutParams = params21
                frameLayout22.layoutParams = params22
                frameLayout23.layoutParams = params23
                frameLayout24.layoutParams = params24
                frameLayout25.layoutParams = params25
                frameLayout26.layoutParams = params26
                frameLayout27.layoutParams = params27
                frameLayout28.layoutParams = params28
                frameLayout29.layoutParams = params29
                frameLayout30.layoutParams = params30
                frameLayout31.layoutParams = params31
                frameLayout32.layoutParams = params32
            } catch (e: Exception) {
            }
            val newLayout1 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout3 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout4 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout5 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout6 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout7 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout8 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout9 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout11 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout12 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout13 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout14 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout15 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout16 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout17 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout18 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout19 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout20 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout21 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout22 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout23 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout24 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout25 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout26 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout27 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout28 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout29 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout30 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout31 = LayoutInflater.from(MyApplication.appContext!!)
            val newLayout32 = LayoutInflater.from(MyApplication.appContext!!)
            val layoutDynamic1 = newLayout1.inflate(R.layout.custom_frame_1, null)
            val layoutDynamic3 = newLayout3.inflate(R.layout.custom_frame_3, null)
            val layoutDynamic4 = newLayout4.inflate(R.layout.custom_frame_4, null)
            val layoutDynamic5 = newLayout5.inflate(R.layout.custom_frame_5, null)
            val layoutDynamic6 = newLayout6.inflate(R.layout.custom_frame_6, null)
            val layoutDynamic7 = newLayout7.inflate(R.layout.custom_frame_7, null)
            val layoutDynamic8 = newLayout8.inflate(R.layout.custom_frame_8, null)
            val layoutDynamic9 = newLayout9.inflate(R.layout.custom_frame_9, null)
            val layoutDynamic11 = newLayout11.inflate(R.layout.custom_frame_11, null)
            val layoutDynamic12 = newLayout12.inflate(R.layout.custom_frame_12, null)
            val layoutDynamic13 = newLayout13.inflate(R.layout.custom_frame_13, null)
            val layoutDynamic14 = newLayout14.inflate(R.layout.custom_frame_14, null)
            val layoutDynamic15 = newLayout15.inflate(R.layout.custom_frame_15, null)
            val layoutDynamic16 = newLayout16.inflate(R.layout.custom_frame_16, null)
            val layoutDynamic17 = newLayout17.inflate(R.layout.custom_frame_17, null)
            val layoutDynamic18 = newLayout18.inflate(R.layout.custom_frame_18, null)
            val layoutDynamic19 = newLayout19.inflate(R.layout.custom_frame_19, null)
            val layoutDynamic20 = newLayout20.inflate(R.layout.custom_frame_20, null)
            val layoutDynamic21 = newLayout21.inflate(R.layout.custom_frame_21, null)
            val layoutDynamic22 = newLayout22.inflate(R.layout.custom_frame_22, null)
            val layoutDynamic23 = newLayout23.inflate(R.layout.custom_frame_23, null)
            val layoutDynamic24 = newLayout24.inflate(R.layout.custom_frame_24, null)
            val layoutDynamic25 = newLayout25.inflate(R.layout.custom_frame_25, null)
            val layoutDynamic26 = newLayout26.inflate(R.layout.custom_frame_26, null)
            val layoutDynamic27 = newLayout27.inflate(R.layout.custom_frame_27, null)
            val layoutDynamic28 = newLayout28.inflate(R.layout.custom_frame_28, null)
            val layoutDynamic29 = newLayout29.inflate(R.layout.custom_frame_29, null)
            val layoutDynamic30 = newLayout30.inflate(R.layout.custom_frame_30, null)
            val layoutDynamic31 = newLayout31.inflate(R.layout.custom_frame_31, null)
            val layoutDynamic32 = newLayout32.inflate(R.layout.custom_frame_32, null)
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic17, child17))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic18, child18))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic19, child19))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic20, child20))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic21, child21))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic22, child22))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic23, child23))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic24, child24))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic25, child25))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic26, child26))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic27, child27))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic28, child28))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic29, child29))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic30, child30))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic31, child31))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic32, child32))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic3, child3))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic4, child4))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic5, child5))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic6, child6))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic7, child7))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic8, child8))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic9, child9))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic11, child11))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic12, child12))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic13, child13))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic14, child14))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic15, child15))
            localFrameItemArrayList.add(LocalFrameItemNew(layoutDynamic16, child16))
            return localFrameItemArrayList
        }

    fun spToPx(sp: Int, context: Context): Int {
        val r = context.resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp.toFloat(),
                r.displayMetrics
            )
        )
    }

    fun dpToPx(sp: Int, context: Context): Int {
        val r = context.resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                sp.toFloat(),
                r.displayMetrics
            )
        )
    }

    fun noInternetConnectionDialog(context: Context) {
        val dialog = Dialog(
            context,
            R.style.DialogAnimation
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.setContentView(R.layout.view_no_internet)
        dialog.setCancelable(false)
        val button = dialog.findViewById<Button>(R.id.btnTryAgain)
        button.setOnClickListener {
            context.startActivity(Intent(context, HomeActivity::class.java))
            (context as AppCompatActivity).finish()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showSuccessDialog(context: Context?, message: String?) {
        Handler(Looper.getMainLooper()).post {
            if (context != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showFailDialog(context: Context?, message: String?) {
        Handler(Looper.getMainLooper()).post {
            if (context != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showToast(context: Context?, message: String?) {
        Handler(Looper.getMainLooper()).post {
            if (context != null) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showInfoDialog(context: Context?, message: String?) {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(context!!)
        builder.setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, which -> dialog.dismiss() }
            .show()
    }

    var pd: ProgressDialog? = null
    fun showProgressDialog(context: Context?) {

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
        if (context != null) {
            pd = ProgressDialog(context)
            pd!!.setCancelable(false)
            pd!!.setMessage(context.resources.getString(R.string.txt_please_wait))
            if (pd!!.isShowing) {
                pd!!.dismiss()
            }
            pd!!.show()
        }
        //            }
//        });
    }

    fun dismissProgressDialog(context: Context?) {
        Handler(Looper.getMainLooper()).post {
            if (context != null) {
                if (pd != null && pd!!.isShowing) {
                    pd!!.dismiss()
                }
            }
        }
    }
    /*    public static synchronized boolean isNetworkAvailable(Context context) {
        boolean flag = false;
        if (context != null) {
            ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mgr != null) {
                boolean mobileNetwork = false;
                boolean wifiNetwork = false;

                NetworkInfo mobileInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiInfo = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mobileInfo != null)
                    mobileNetwork = mobileInfo.isAvailable();
                if (wifiInfo != null)
                    wifiNetwork = wifiInfo.isAvailable();
                flag = (mobileNetwork || wifiNetwork);
            }
        }

        return flag;
    }*/
    /**
     * Checking device has camera hardware or not
     */
    fun isDeviceSupportCamera(context: Context): Boolean {
        return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            true
        } else {
            // no camera on this device
            false
        }
    }

    fun storePreference(parameters: HashMap<String?, String?>) {
        val editor = MyApplication.sharedPref!!.edit()
        val it: Iterator<Map.Entry<String?, String?>> = parameters.entries.iterator()
        while (it.hasNext()) {
            val pairs = it.next()
            editor.putString(pairs.key, pairs.value)
        }
        editor.commit()
    }

    fun storePreference(key: String?, value: String?) {
        val editor = MyApplication.sharedPref!!.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun storePreference(key: String?, value: Int) {
        val editor = MyApplication.sharedPref!!.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun storePreference(key: String?, value: Long) {
        val editor = MyApplication.sharedPref!!.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    fun removePreference(key: String?) {
        val editor = MyApplication.sharedPref!!.edit()
        editor.remove(key)
        editor.commit()
    }

    fun removePreferences(keys: Array<String?>) {
        val editor = MyApplication.sharedPref!!.edit()
        for (key in keys) {
            editor.remove(key)
        }
        editor.commit()
    }

    fun clearPrefernces() {
        val editor = MyApplication.sharedPref!!.edit()
        editor.clear()
        editor.commit()
    }

    fun storePreference(key: String?, value: Boolean?) {
        val editor = MyApplication.sharedPref!!.edit()
        editor.putBoolean(key, value!!)
        editor.commit()
    }

    fun getPreference(keys: Array<String>): HashMap<String, String?> {
        val parameters = HashMap<String, String?>()
        for (key in keys) {
            parameters[key] = MyApplication.sharedPref!!.getString(key, null)
        }
        return parameters
    }

    fun getPreference(key: String?, defValue: String?): String? {
        return MyApplication.sharedPref!!.getString(key, defValue)
    }

    fun getPreference(key: String?, defValue: Long): Long {
        return MyApplication.sharedPref!!.getLong(key, defValue)
    }

    fun getPreference(key: String?, defValue: Int): Int {
        return MyApplication.sharedPref!!.getInt(key, defValue)
    }

    fun getPreference(key: String?, defValue: Boolean?): Boolean {
        return MyApplication.sharedPref!!.getBoolean(key, defValue!!)
    }

    fun fileLog(filename: String, output: String?) {
        try {
            val myFile = File(
                Environment.getExternalStorageDirectory().toString() + File.separator + filename
            )
            myFile.createNewFile()
            val fOut = FileOutputStream(myFile)
            val myOutWriter = OutputStreamWriter(fOut)
            myOutWriter.write(output)
            myOutWriter.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    fun checkEmail(email: String?): Boolean {
        val EMAIL_ADDRESS_PATTERN =
            Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    fun hideSoftKeyboard(context: Context, editText: EditText) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun storeCurrentBusiness(json: String?) {
        storePreference(Constant.PREF_CURRENT_BUSINESS, json)
    }

    fun storeCurrentBusinessNew(json: String?) {
        storePreference(Constant.PREF_CURRENT_BUSINESS_NEW, json)
    }

    val currentBusiness: BusinessItem
        get() {
            val json = getPreference(Constant.PREF_CURRENT_BUSINESS, "")
            return if (!json.equals("", ignoreCase = true)) {
                Gson().fromJson(json, BusinessItem::class.java)
            } else {
                BusinessItem()
            }
        }
    val currentBusinessNEW: BusinessItem
        get() {
            val json = getPreference(Constant.PREF_CURRENT_BUSINESS_NEW, "")
            return if (!json.equals("", ignoreCase = true)) {
                Gson().fromJson(json, BusinessItem::class.java)
            } else {
                BusinessItem()
            }
        }
    val preferenceItem: PreferenceItem
        get() {
            val json = getPreference(Constant.PREF_PREF_LIST, "")
            return if (!json.equals("", ignoreCase = true)) {
                Gson().fromJson(json, PreferenceItem::class.java)
            } else {
                PreferenceItem()
            }
        }

    fun storeFrameList(json: String?) {
        storePreference(Constant.PREF_FRAME_LIST, json)
    }

    val frameList: FrameListItem
        get() {
            val json = getPreference(Constant.PREF_FRAME_LIST, "")
            return if (!json.equals("", ignoreCase = true)) {
                Gson().fromJson(json, FrameListItem::class.java)
            } else {
                FrameListItem()
            }
        }

    fun getAlertDialog(context: Context, title: String?, message: String?) {
        val materialAlertDialogBuilder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_error_dialog, null)
        val tvTitle: TextView
        val tvMessage: TextView
        val btnOk: Button
        tvTitle = view.findViewById(R.id.tvTitle)
        tvMessage = view.findViewById(R.id.tvMessage)
        btnOk = view.findViewById(R.id.btnOk)
        tvTitle.text = title
        tvMessage.text = message
        materialAlertDialogBuilder.setView(view).setCancelable(true)
        val b = materialAlertDialogBuilder.create()
        btnOk.setOnClickListener { b.dismiss() }
        b.show()
    }

    fun pxtoSdp(context: Context, value: Int): Int {
        var valueInPixels = 0
        for (i in 0..299) {
            if (value == 1) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._1sdp)
                return valueInPixels
            } else if (value == 2) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._2sdp)
                return valueInPixels
            } else if (value == 3) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._3sdp)
                return valueInPixels
            } else if (value == 4) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._4sdp)
                return valueInPixels
            } else if (value == 5) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._5sdp)
                return valueInPixels
            } else if (value == 6) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._6sdp)
                return valueInPixels
            } else if (value == 7) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._7sdp)
                return valueInPixels
            } else if (value == 8) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._8sdp)
                return valueInPixels
            } else if (value == 9) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._9sdp)
                return valueInPixels
            } else if (value == 10) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._10sdp)
                return valueInPixels
            } else if (value == 11) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._11sdp)
                return valueInPixels
            } else if (value == 12) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._12sdp)
                return valueInPixels
            } else if (value == 13) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._13sdp)
                return valueInPixels
            } else if (value == 14) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._14sdp)
                return valueInPixels
            } else if (value == 15) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._15sdp)
                return valueInPixels
            } else if (value == 16) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._16sdp)
                return valueInPixels
            } else if (value == 17) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._17sdp)
                return valueInPixels
            } else if (value == 18) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._18sdp)
                return valueInPixels
            } else if (value == 19) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._19sdp)
                return valueInPixels
            } else if (value == 20) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._20sdp)
                return valueInPixels
            } else if (value == 21) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._21sdp)
                return valueInPixels
            } else if (value == 22) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._22sdp)
                return valueInPixels
            } else if (value == 23) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._23sdp)
                return valueInPixels
            } else if (value == 24) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._24sdp)
                return valueInPixels
            } else if (value == 25) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._25sdp)
                return valueInPixels
            } else if (value == 26) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._26sdp)
                return valueInPixels
            } else if (value == 27) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._27sdp)
                return valueInPixels
            } else if (value == 28) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._28sdp)
                return valueInPixels
            } else if (value == 29) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._29sdp)
                return valueInPixels
            } else if (value == 30) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._30sdp)
                return valueInPixels
            } else if (value == 31) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._31sdp)
                return valueInPixels
            } else if (value == 32) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._32sdp)
                return valueInPixels
            } else if (value == 33) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._33sdp)
                return valueInPixels
            } else if (value == 34) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._34sdp)
                return valueInPixels
            } else if (value == 35) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._35sdp)
                return valueInPixels
            } else if (value == 36) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._36sdp)
                return valueInPixels
            } else if (value == 37) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._37sdp)
                return valueInPixels
            } else if (value == 38) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._38sdp)
                return valueInPixels
            } else if (value == 39) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._39sdp)
                return valueInPixels
            } else if (value == 40) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._40sdp)
                return valueInPixels
            } else if (value == 41) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._41sdp)
                return valueInPixels
            } else if (value == 42) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._42sdp)
                return valueInPixels
            } else if (value == 43) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._43sdp)
                return valueInPixels
            } else if (value == 44) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._44sdp)
                return valueInPixels
            } else if (value == 45) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._45sdp)
                return valueInPixels
            } else if (value == 46) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._46sdp)
                return valueInPixels
            } else if (value == 47) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._47sdp)
                return valueInPixels
            } else if (value == 48) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._48sdp)
                return valueInPixels
            } else if (value == 49) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._49sdp)
                return valueInPixels
            } else if (value == 50) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._50sdp)
                return valueInPixels
            } else if (value == 51) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._51sdp)
                return valueInPixels
            } else if (value == 52) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._52sdp)
                return valueInPixels
            } else if (value == 53) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._53sdp)
                return valueInPixels
            } else if (value == 54) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._54sdp)
                return valueInPixels
            } else if (value == 55) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._55sdp)
                return valueInPixels
            } else if (value == 56) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._56sdp)
                return valueInPixels
            } else if (value == 57) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._57sdp)
                return valueInPixels
            } else if (value == 58) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._58sdp)
                return valueInPixels
            } else if (value == 59) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._59sdp)
                return valueInPixels
            } else if (value == 60) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._60sdp)
                return valueInPixels
            } else if (value == 61) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._61sdp)
                return valueInPixels
            } else if (value == 62) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._62sdp)
                return valueInPixels
            } else if (value == 63) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._63sdp)
                return valueInPixels
            } else if (value == 64) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._64sdp)
                return valueInPixels
            } else if (value == 65) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._65sdp)
                return valueInPixels
            } else if (value == 66) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._66sdp)
                return valueInPixels
            } else if (value == 67) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._67sdp)
                return valueInPixels
            } else if (value == 68) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._68sdp)
                return valueInPixels
            } else if (value == 69) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._69sdp)
                return valueInPixels
            } else if (value == 70) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._70sdp)
                return valueInPixels
            } else if (value == 71) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._71sdp)
                return valueInPixels
            } else if (value == 72) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._72sdp)
                return valueInPixels
            } else if (value == 73) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._73sdp)
                return valueInPixels
            } else if (value == 74) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._74sdp)
                return valueInPixels
            } else if (value == 75) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._75sdp)
                return valueInPixels
            } else if (value == 76) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._76sdp)
                return valueInPixels
            } else if (value == 77) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._77sdp)
                return valueInPixels
            } else if (value == 78) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._78sdp)
                return valueInPixels
            } else if (value == 79) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._79sdp)
                return valueInPixels
            } else if (value == 80) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._80sdp)
                return valueInPixels
            } else if (value == 81) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._81sdp)
                return valueInPixels
            } else if (value == 82) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._82sdp)
                return valueInPixels
            } else if (value == 83) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._83sdp)
                return valueInPixels
            } else if (value == 84) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._84sdp)
                return valueInPixels
            } else if (value == 85) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._85sdp)
                return valueInPixels
            } else if (value == 86) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._86sdp)
                return valueInPixels
            } else if (value == 87) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._87sdp)
                return valueInPixels
            } else if (value == 88) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._88sdp)
                return valueInPixels
            } else if (value == 89) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._89sdp)
                return valueInPixels
            } else if (value == 90) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._90sdp)
                return valueInPixels
            } else if (value == 91) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._91sdp)
                return valueInPixels
            } else if (value == 92) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._92sdp)
                return valueInPixels
            } else if (value == 93) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._93sdp)
                return valueInPixels
            } else if (value == 94) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._94sdp)
                return valueInPixels
            } else if (value == 95) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._95sdp)
                return valueInPixels
            } else if (value == 96) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._96sdp)
                return valueInPixels
            } else if (value == 97) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._97sdp)
                return valueInPixels
            } else if (value == 98) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._98sdp)
                return valueInPixels
            } else if (value == 99) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._99sdp)
                return valueInPixels
            } else if (value == 100) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._100sdp)
                return valueInPixels
            }
            if (value == 101) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._101sdp)
                return valueInPixels
            } else if (value == 102) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._102sdp)
                return valueInPixels
            } else if (value == 103) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._103sdp)
                return valueInPixels
            } else if (value == 104) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._104sdp)
                return valueInPixels
            } else if (value == 105) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._105sdp)
                return valueInPixels
            } else if (value == 106) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._106sdp)
                return valueInPixels
            } else if (value == 107) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._107sdp)
                return valueInPixels
            } else if (value == 108) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._108sdp)
                return valueInPixels
            } else if (value == 109) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._109sdp)
                return valueInPixels
            } else if (value == 110) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._110sdp)
                return valueInPixels
            } else if (value == 111) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._111sdp)
                return valueInPixels
            } else if (value == 112) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._112sdp)
                return valueInPixels
            } else if (value == 113) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._113sdp)
                return valueInPixels
            } else if (value == 114) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._114sdp)
                return valueInPixels
            } else if (value == 115) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._115sdp)
                return valueInPixels
            } else if (value == 116) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._116sdp)
                return valueInPixels
            } else if (value == 117) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._117sdp)
                return valueInPixels
            } else if (value == 118) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._118sdp)
                return valueInPixels
            } else if (value == 119) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._119sdp)
                return valueInPixels
            } else if (value == 120) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._120sdp)
                return valueInPixels
            } else if (value == 121) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._121sdp)
                return valueInPixels
            } else if (value == 122) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._122sdp)
                return valueInPixels
            } else if (value == 123) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._123sdp)
                return valueInPixels
            } else if (value == 124) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._124sdp)
                return valueInPixels
            } else if (value == 125) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._125sdp)
                return valueInPixels
            } else if (value == 126) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._126sdp)
                return valueInPixels
            } else if (value == 127) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._127sdp)
                return valueInPixels
            } else if (value == 128) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._128sdp)
                return valueInPixels
            } else if (value == 129) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._129sdp)
                return valueInPixels
            } else if (value == 130) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._130sdp)
                return valueInPixels
            } else if (value == 131) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._131sdp)
                return valueInPixels
            } else if (value == 132) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._132sdp)
                return valueInPixels
            } else if (value == 133) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._133sdp)
                return valueInPixels
            } else if (value == 134) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._134sdp)
                return valueInPixels
            } else if (value == 135) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._135sdp)
                return valueInPixels
            } else if (value == 136) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._136sdp)
                return valueInPixels
            } else if (value == 137) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._137sdp)
                return valueInPixels
            } else if (value == 138) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._138sdp)
                return valueInPixels
            } else if (value == 139) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._139sdp)
                return valueInPixels
            } else if (value == 140) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._140sdp)
                return valueInPixels
            } else if (value == 141) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._141sdp)
                return valueInPixels
            } else if (value == 142) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._142sdp)
                return valueInPixels
            } else if (value == 143) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._143sdp)
                return valueInPixels
            } else if (value == 144) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._144sdp)
                return valueInPixels
            } else if (value == 145) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._145sdp)
                return valueInPixels
            } else if (value == 146) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._146sdp)
                return valueInPixels
            } else if (value == 147) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._147sdp)
                return valueInPixels
            } else if (value == 148) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._148sdp)
                return valueInPixels
            } else if (value == 149) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._149sdp)
                return valueInPixels
            } else if (value == 150) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._150sdp)
                return valueInPixels
            } else if (value == 151) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._151sdp)
                return valueInPixels
            } else if (value == 152) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._152sdp)
                return valueInPixels
            } else if (value == 153) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._153sdp)
                return valueInPixels
            } else if (value == 154) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._154sdp)
                return valueInPixels
            } else if (value == 155) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._155sdp)
                return valueInPixels
            } else if (value == 156) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._156sdp)
                return valueInPixels
            } else if (value == 157) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._157sdp)
                return valueInPixels
            } else if (value == 158) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._158sdp)
                return valueInPixels
            } else if (value == 159) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._159sdp)
                return valueInPixels
            } else if (value == 160) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._160sdp)
                return valueInPixels
            } else if (value == 161) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._161sdp)
                return valueInPixels
            } else if (value == 162) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._162sdp)
                return valueInPixels
            } else if (value == 163) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._163sdp)
                return valueInPixels
            } else if (value == 164) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._164sdp)
                return valueInPixels
            } else if (value == 165) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._165sdp)
                return valueInPixels
            } else if (value == 166) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._166sdp)
                return valueInPixels
            } else if (value == 167) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._167sdp)
                return valueInPixels
            } else if (value == 168) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._168sdp)
                return valueInPixels
            } else if (value == 169) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._169sdp)
                return valueInPixels
            } else if (value == 170) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._170sdp)
                return valueInPixels
            } else if (value == 171) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._171sdp)
                return valueInPixels
            } else if (value == 172) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._172sdp)
                return valueInPixels
            } else if (value == 173) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._173sdp)
                return valueInPixels
            } else if (value == 174) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._174sdp)
                return valueInPixels
            } else if (value == 175) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._175sdp)
                return valueInPixels
            } else if (value == 176) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._176sdp)
                return valueInPixels
            } else if (value == 177) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._177sdp)
                return valueInPixels
            } else if (value == 178) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._178sdp)
                return valueInPixels
            } else if (value == 179) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._179sdp)
                return valueInPixels
            } else if (value == 180) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._180sdp)
                return valueInPixels
            } else if (value == 181) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._181sdp)
                return valueInPixels
            } else if (value == 182) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._182sdp)
                return valueInPixels
            } else if (value == 183) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._183sdp)
                return valueInPixels
            } else if (value == 184) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._184sdp)
                return valueInPixels
            } else if (value == 185) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._185sdp)
                return valueInPixels
            } else if (value == 186) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._186sdp)
                return valueInPixels
            } else if (value == 187) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._187sdp)
                return valueInPixels
            } else if (value == 188) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._188sdp)
                return valueInPixels
            } else if (value == 189) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._189sdp)
                return valueInPixels
            } else if (value == 190) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._190sdp)
                return valueInPixels
            } else if (value == 191) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._191sdp)
                return valueInPixels
            } else if (value == 192) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._192sdp)
                return valueInPixels
            } else if (value == 193) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._193sdp)
                return valueInPixels
            } else if (value == 194) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._194sdp)
                return valueInPixels
            } else if (value == 195) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._195sdp)
                return valueInPixels
            } else if (value == 196) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._196sdp)
                return valueInPixels
            } else if (value == 197) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._197sdp)
                return valueInPixels
            } else if (value == 198) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._198sdp)
                return valueInPixels
            } else if (value == 199) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._199sdp)
                return valueInPixels
            } else if (value == 200) {
                valueInPixels = context.resources.getDimensionPixelOffset(R.dimen._200sdp)
                return valueInPixels
            }
            return if (value == 201) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._201sdp)
                valueInPixels
            } else if (value == 202) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._202sdp)
                valueInPixels
            } else if (value == 203) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._203sdp)
                valueInPixels
            } else if (value == 204) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._204sdp)
                valueInPixels
            } else if (value == 205) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._205sdp)
                valueInPixels
            } else if (value == 206) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._206sdp)
                valueInPixels
            } else if (value == 207) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._207sdp)
                valueInPixels
            } else if (value == 208) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._208sdp)
                valueInPixels
            } else if (value == 209) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._209sdp)
                valueInPixels
            } else if (value == 210) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._210sdp)
                valueInPixels
            } else if (value == 211) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._211sdp)
                valueInPixels
            } else if (value == 212) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._212sdp)
                valueInPixels
            } else if (value == 213) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._213sdp)
                valueInPixels
            } else if (value == 214) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._214sdp)
                valueInPixels
            } else if (value == 215) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._215sdp)
                valueInPixels
            } else if (value == 216) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._216sdp)
                valueInPixels
            } else if (value == 217) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._217sdp)
                valueInPixels
            } else if (value == 218) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._218sdp)
                valueInPixels
            } else if (value == 219) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._219sdp)
                valueInPixels
            } else if (value == 220) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._220sdp)
                valueInPixels
            } else if (value == 221) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._221sdp)
                valueInPixels
            } else if (value == 222) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._222sdp)
                valueInPixels
            } else if (value == 223) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._223sdp)
                valueInPixels
            } else if (value == 224) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._224sdp)
                valueInPixels
            } else if (value == 225) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._225sdp)
                valueInPixels
            } else if (value == 226) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._226sdp)
                valueInPixels
            } else if (value == 227) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._227sdp)
                valueInPixels
            } else if (value == 228) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._228sdp)
                valueInPixels
            } else if (value == 229) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._229sdp)
                valueInPixels
            } else if (value == 230) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._230sdp)
                valueInPixels
            } else if (value == 231) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._231sdp)
                valueInPixels
            } else if (value == 232) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._232sdp)
                valueInPixels
            } else if (value == 233) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._233sdp)
                valueInPixels
            } else if (value == 234) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._234sdp)
                valueInPixels
            } else if (value == 235) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._235sdp)
                valueInPixels
            } else if (value == 236) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._236sdp)
                valueInPixels
            } else if (value == 237) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._237sdp)
                valueInPixels
            } else if (value == 238) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._238sdp)
                valueInPixels
            } else if (value == 239) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._239sdp)
                valueInPixels
            } else if (value == 240) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._240sdp)
                valueInPixels
            } else if (value == 241) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._241sdp)
                valueInPixels
            } else if (value == 242) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._242sdp)
                valueInPixels
            } else if (value == 243) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._243sdp)
                valueInPixels
            } else if (value == 244) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._244sdp)
                valueInPixels
            } else if (value == 245) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._245sdp)
                valueInPixels
            } else if (value == 246) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._246sdp)
                valueInPixels
            } else if (value == 247) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._247sdp)
                valueInPixels
            } else if (value == 248) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._248sdp)
                valueInPixels
            } else if (value == 249) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._249sdp)
                valueInPixels
            } else if (value == 250) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._250sdp)
                valueInPixels
            } else if (value == 251) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._251sdp)
                valueInPixels
            } else if (value == 252) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._252sdp)
                valueInPixels
            } else if (value == 253) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._253sdp)
                valueInPixels
            } else if (value == 254) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._254sdp)
                valueInPixels
            } else if (value == 255) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._255sdp)
                valueInPixels
            } else if (value == 256) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._256sdp)
                valueInPixels
            } else if (value == 257) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._257sdp)
                valueInPixels
            } else if (value == 258) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._258sdp)
                valueInPixels
            } else if (value == 259) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._259sdp)
                valueInPixels
            } else if (value == 260) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._260sdp)
                valueInPixels
            } else if (value == 261) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._261sdp)
                valueInPixels
            } else if (value == 262) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._262sdp)
                valueInPixels
            } else if (value == 263) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._263sdp)
                valueInPixels
            } else if (value == 264) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._264sdp)
                valueInPixels
            } else if (value == 265) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._265sdp)
                valueInPixels
            } else if (value == 266) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._266sdp)
                valueInPixels
            } else if (value == 267) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._267sdp)
                valueInPixels
            } else if (value == 268) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._268sdp)
                valueInPixels
            } else if (value == 269) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._269sdp)
                valueInPixels
            } else if (value == 270) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._270sdp)
                valueInPixels
            } else if (value == 271) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._271sdp)
                valueInPixels
            } else if (value == 272) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._272sdp)
                valueInPixels
            } else if (value == 273) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._273sdp)
                valueInPixels
            } else if (value == 274) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._274sdp)
                valueInPixels
            } else if (value == 275) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._275sdp)
                valueInPixels
            } else if (value == 276) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._276sdp)
                valueInPixels
            } else if (value == 277) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._277sdp)
                valueInPixels
            } else if (value == 278) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._278sdp)
                valueInPixels
            } else if (value == 279) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._279sdp)
                valueInPixels
            } else if (value == 280) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._280sdp)
                valueInPixels
            } else if (value == 281) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._281sdp)
                valueInPixels
            } else if (value == 282) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._282sdp)
                valueInPixels
            } else if (value == 283) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._283sdp)
                valueInPixels
            } else if (value == 284) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._284sdp)
                valueInPixels
            } else if (value == 285) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._285sdp)
                valueInPixels
            } else if (value == 286) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._286sdp)
                valueInPixels
            } else if (value == 287) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._287sdp)
                valueInPixels
            } else if (value == 288) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._288sdp)
                valueInPixels
            } else if (value == 289) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._289sdp)
                valueInPixels
            } else if (value == 290) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._290sdp)
                valueInPixels
            } else if (value == 291) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._291sdp)
                valueInPixels
            } else if (value == 292) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._292sdp)
                valueInPixels
            } else if (value == 293) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._293sdp)
                valueInPixels
            } else if (value == 294) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._294sdp)
                valueInPixels
            } else if (value == 295) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._295sdp)
                valueInPixels
            } else if (value == 296) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._296sdp)
                valueInPixels
            } else if (value == 297) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._297sdp)
                valueInPixels
            } else if (value == 298) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._298sdp)
                valueInPixels
            } else if (value == 299) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._299sdp)
                valueInPixels
            } else if (value == 300) {
                valueInPixels =
                    context.resources.getDimensionPixelOffset(R.dimen._300sdp)
                valueInPixels
            } else {
                valueInPixels = dpToPx(value, context)
                valueInPixels
            }
        }
        Log.d("valueInPixles", "" + valueInPixels)
        return valueInPixels
    }
}