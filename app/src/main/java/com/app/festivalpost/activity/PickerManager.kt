package com.app.festivalpost.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.festivalpost.R
import com.app.festivalpost.activity.PickerBuilder.onImageReceivedListener
import com.app.festivalpost.activity.PickerBuilder.onPermissionRefusedListener
import com.yalantis.ucrop.UCrop
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Mickael on 10/10/2016.
 */
abstract class PickerManager(var activity: Activity) {
    protected var mProcessingPhotoUri: Uri? = null
    private var withTimeStamp = true
    private var folder: String? = null
    private var imageName: String
    private var uCrop: UCrop? = null
    protected var imageReceivedListener: onImageReceivedListener? = null
    protected var permissionRefusedListener: onPermissionRefusedListener? = null
    private var cropActivityColor = R.color.colorPrimary
    fun setOnImageReceivedListener(listener: onImageReceivedListener?): PickerManager {
        imageReceivedListener = listener
        return this
    }

    fun setOnPermissionRefusedListener(listener: onPermissionRefusedListener?): PickerManager {
        permissionRefusedListener = listener
        return this
    }

    fun pickPhotoWithPermission() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_IMAGE_PERMISSION
            )
        } else sendToExternalApp()
    }

    fun handlePermissionResult(grantResults: IntArray) {
        if (grantResults.size > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {

            // permission was granted
            sendToExternalApp()
        } else {

            // permission denied
            if (permissionRefusedListener != null) permissionRefusedListener!!.onPermissionRefused()
            activity.finish()
        }
    }

    protected abstract fun sendToExternalApp()

    // long currentTimeMillis = System.currentTimeMillis();
    // String photoName = imageName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date(currentTimeMillis)) + ".jpg";
    val imageFile: Uri
        get() {
            val imagePathStr = Environment.getExternalStorageDirectory().toString() + "/" +
                    if (folder == null) Environment.DIRECTORY_DCIM + "/" + activity.getString(R.string.app_name) else folder
            val path = File(imagePathStr)
            if (!path.exists()) {
                path.mkdir()
            }
            val finalPhotoName = (imageName +
                    (if (withTimeStamp) "_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(
                        Date(System.currentTimeMillis())
                    ) else "")
                    + ".png")

            // long currentTimeMillis = System.currentTimeMillis();
            // String photoName = imageName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date(currentTimeMillis)) + ".jpg";
            val photo = File(path, finalPhotoName)
            return Uri.fromFile(photo)
        }

    open fun setUri(uri: Uri?) {}
    @SuppressLint("ResourceAsColor")
    fun startCropActivity() {
        if (uCrop == null) {
            uCrop = UCrop.of(mProcessingPhotoUri!!, imageFile).withAspectRatio(1f, 1f)
            //            uCrop = uCrop.useSourceImageAspectRatio();
            val options = UCrop.Options()
            options.setFreeStyleCropEnabled(true)
            options.setToolbarColor(cropActivityColor)
            options.setStatusBarColor(cropActivityColor)
            //options.setActiveWidgetColor(cropActivityColor);
            uCrop = uCrop!!.withOptions(options)
        }
        uCrop!!.start(activity)
    }

    fun handleCropResult(data: Intent?) {
        val resultUri = UCrop.getOutput(data!!)
        if (imageReceivedListener != null) imageReceivedListener!!.onImageReceived(resultUri)
        activity.finish()
    }

    fun setActivity(activity: Activity): PickerManager {
        this.activity = activity
        return this
    }

    fun setImageName(imageName: String): PickerManager {
        this.imageName = imageName
        return this
    }

    fun setCropActivityColor(cropActivityColor: Int): PickerManager {
        this.cropActivityColor = cropActivityColor
        return this
    }

    fun withTimeStamp(withTimeStamp: Boolean): PickerManager {
        this.withTimeStamp = withTimeStamp
        return this
    }

    fun setImageFolderName(folder: String?): PickerManager {
        this.folder = folder
        return this
    }

    fun setCustomizedUcrop(customizedUcrop: UCrop?): PickerManager {
        uCrop = customizedUcrop
        return this
    }

    companion object {
        const val REQUEST_CODE_SELECT_IMAGE = 200
        const val REQUEST_CODE_IMAGE_PERMISSION = 201
    }

    init {
        imageName = activity.getString(R.string.app_name)
    }
}