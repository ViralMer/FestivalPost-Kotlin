package com.app.festivalpost.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.app.festivalpost.activity.PickerManager
import java.util.*

class ImagePickerManager(activity: Activity?) : PickerManager(activity!!) {
    override fun sendToExternalApp() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        Objects.requireNonNull(activity).startActivityForResult(
            Intent.createChooser(intent, "Select avatar..."),
            REQUEST_CODE_SELECT_IMAGE
        )
    }

    override fun setUri(uri: Uri?) {
        mProcessingPhotoUri = uri
    }
}