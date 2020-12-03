package com.app.festivalpost.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.app.festivalpost.activity.GlobalHolder.Companion.instance

class ImagePickerActivity : AppCompatActivity() {
    var pickerManager: PickerManager? = null
    var REQUEST_CROP:Int?=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerManager = instance.pickerManager
        pickerManager!!.setActivity(this@ImagePickerActivity)
        pickerManager!!.pickPhotoWithPermission()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            finish()
            return
        }
        when (requestCode) {
            PickerManager.REQUEST_CODE_SELECT_IMAGE -> {
                val uri: Uri?
                uri = if (data != null) data.data else pickerManager!!.imageFile
                pickerManager!!.setUri(uri)
                pickerManager!!.startCropActivity()
            }
            REQUEST_CROP -> if (data != null) {
                pickerManager!!.handleCropResult(data)
            } else finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == PickerManager.REQUEST_CODE_IMAGE_PERMISSION) pickerManager!!.handlePermissionResult(
            grantResults
        ) else finish()
    }
}