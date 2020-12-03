package com.app.festivalpost.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.PickerBuilder.onImageReceivedListener
import com.app.festivalpost.activity.PickerBuilder.onPermissionRefusedListener
import com.app.festivalpost.photoeditor.PhotoEditor
import com.app.festivalpost.photoeditor.PhotoEditorView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException

class ImageTextEditorActivity : AppCompatActivity() {
    var mPhotoEditor: PhotoEditor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_text_editor)
        val photoEditorView = findViewById<PhotoEditorView>(R.id.photoEditorView)
        mPhotoEditor = PhotoEditor.Builder(this, photoEditorView)
            .setPinchTextScalable(true)
            .build()
    }

    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        MenuInflater inflater = getMenuInflater();
    //        inflater.inflate(R.menu.menu_home, menu);
    //        return true;
    //    }
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        switch (item.getItemId()) {
    //            case R.id.menu_addtext:
    //                addTextOnImage();
    //                return true;
    //            case R.id.menu_image:
    //                addImageOnImage();
    //                return true;
    //            default:
    //                return super.onOptionsItemSelected(item);
    //        }
    //    }
    fun addTextOnImage() {
        val detailact = Intent(this@ImageTextEditorActivity, AddTextActivity::class.java)
        startActivityForResult(detailact, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1000) {
            val text = data!!.getStringExtra("text")
            val color = data.getIntExtra("color", 0)
            if (mPhotoEditor != null) {
                mPhotoEditor!!.addText(text, color)
            }
        }
    }

    fun addImageOnImage() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        val builder = AlertDialog.Builder(this@ImageTextEditorActivity)
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

    fun startCamera() {
        PickerBuilder(this@ImageTextEditorActivity, PickerBuilder.SELECT_FROM_CAMERA)
            .setOnImageReceivedListener(object : onImageReceivedListener {
                override fun onImageReceived(imageUri: Uri?) {
                    Toast.makeText(
                        this@ImageTextEditorActivity,
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
        PickerBuilder(this@ImageTextEditorActivity, PickerBuilder.SELECT_FROM_GALLERY)
            .setOnImageReceivedListener(object : onImageReceivedListener {
                override fun onImageReceived(imageUri: Uri?) {
                    Toast.makeText(
                        this@ImageTextEditorActivity,
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
}