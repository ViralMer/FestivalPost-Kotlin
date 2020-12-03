package com.app.festivalpost.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.app.festivalpost.activity.PickerBuilder.onPermissionRefusedListener
import com.app.festivalpost.activity.PickerBuilder.onImageReceivedListener
import com.app.festivalpost.activity.PickerManager
import com.app.festivalpost.activity.GlobalHolder
import com.app.festivalpost.activity.PickerBuilder
import com.yalantis.ucrop.UCrop
import com.app.festivalpost.activity.CameraPickerManager

/**
 * Created by Mickael on 13/10/2016.
 */
class PickerBuilder(private var activity: Activity, type: Int) {
    private val permissionRefusedListener: onPermissionRefusedListener? = null
    protected var imageReceivedListener: onImageReceivedListener? = null
    private val pickerManager: PickerManager?=null

    interface onPermissionRefusedListener {
        fun onPermissionRefused()
    }

    interface onImageReceivedListener {
        fun onImageReceived(imageUri: Uri?)
    }

    fun start() {
        val intent = Intent(activity, ImagePickerActivity::class.java)
        activity.startActivity(intent)
        GlobalHolder.instance.pickerManager=pickerManager
    }

    fun setOnImageReceivedListener(listener: onImageReceivedListener?): PickerBuilder {
        pickerManager!!.setOnImageReceivedListener(listener)
        return this
    }

    fun setOnPermissionRefusedListener(listener: onPermissionRefusedListener?): PickerBuilder {
        pickerManager!!.setOnPermissionRefusedListener(listener)
        return this
    }

    fun setCropScreenColor(cropScreenColor: Int): PickerBuilder {
        pickerManager!!.setCropActivityColor(cropScreenColor)
        return this
    }

    fun setImageName(imageName: String?): PickerBuilder {
        pickerManager!!.setImageName(imageName!!)
        return this
    }

    fun withTimeStamp(withTimeStamp: Boolean): PickerBuilder {
        pickerManager!!.withTimeStamp(withTimeStamp)
        return this
    }

    fun setImageFolderName(folderName: String?): PickerBuilder {
        pickerManager!!.setImageFolderName(folderName)
        return this
    }

    fun setCustomizedUcrop(ucrop: UCrop?): PickerBuilder {
        pickerManager!!.setCustomizedUcrop(ucrop)
        return this
    }

    companion object {
        const val SELECT_FROM_GALLERY = 0
        const val SELECT_FROM_CAMERA = 1
    }

    init {
       /* pickerManager = if (type == SELECT_FROM_GALLERY) {
            PickerManager(activity)
        } else CameraPickerManager(activity)*/
    }
}