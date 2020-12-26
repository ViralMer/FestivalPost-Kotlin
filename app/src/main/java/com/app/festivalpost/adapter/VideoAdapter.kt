package com.app.festivalpost.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.FileListItem
import com.app.festivalpost.models.PostItem
import com.app.festivalpost.utils.Constants.SharedPref.USER_NAME
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.emegamart.lelys.utils.extensions.getSharedPrefInstance
import java.util.*
import java.util.function.Consumer

class VideoAdapter(var context: Context, var originaldata: ArrayList<FileListItem>) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    var searchCount = 0
    private var isLoaderVisible = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.custom_post_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val festivalItem = originaldata[position]
        Log.d("image_url", "" + originaldata.size)
        holder.tvname.text = getSharedPrefInstance().getStringValue(USER_NAME)
        val path: String =
            Environment.getExternalStorageDirectory().toString().toString() + "/FestivalPost"
        val bitmap = BitmapFactory.decodeFile(path + "/" + festivalItem.path)
        holder.ivphoto.setImageBitmap(bitmap)
        holder.layMain.tag = festivalItem
        holder.layMain.setOnClickListener { view ->
            showFullScreenImage(path)
        }
    }

    fun showFullScreenImage(imageUrl: String?) {
        val dialog = Dialog(
            context,
            R.style.DialogAnimation
        )
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        dialog.setContentView(R.layout.custom_image_dialog)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val ivimage = dialog.findViewById<View>(R.id.ivimage) as ImageView
        if (imageUrl != null && !imageUrl.equals("", ignoreCase = true)) {
            Glide.with(context).load(imageUrl).placeholder(R.drawable.placeholder_img)
                .error(R.drawable.placeholder_img).into(ivimage)
        }
        val button = dialog.findViewById<Button>(R.id.btnshare)
        button.setOnClickListener {
            try {
                if (imageUrl != null && !imageUrl.equals("", ignoreCase = true)) {
                    Glide.with(context)
                        .asBitmap()
                        .load(imageUrl)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(object : SimpleTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val path = MediaStore.Images.Media.insertImage(
                                    context.contentResolver, resource, "Festival Post", null
                                )
                                val screenshotUri = Uri.parse(path)
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/*"
                                share.putExtra(Intent.EXTRA_STREAM, screenshotUri)
                                share.putExtra(
                                    Intent.EXTRA_TEXT,
                                    Global.getPreference(Constant.PREF_SHARE_MESSAGE, "")
                                )
                                context.startActivity(Intent.createChooser(share, "Share Design!"))
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                super.onLoadFailed(errorDrawable)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                super.onLoadCleared(placeholder)
                            }
                        })
                }
            } catch (e: Exception) {
                Log.d("MEssage", "" + e.message)
            }
        }
        val dialogButton = dialog.findViewById<View>(R.id.btndone) as TextView
        dialogButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layMain: LinearLayout = view.findViewById<View>(R.id.lay_main) as LinearLayout
        val tvname: TextView = view.findViewById<View>(R.id.tvname) as TextView
        val laypost: LinearLayout = view.findViewById<View>(R.id.laypost) as LinearLayout
        val ivphoto: ImageView = view.findViewById<View>(R.id.ivphoto) as ImageView

    }

    fun addMoreItems(postItems: ArrayList<PostItem?>?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            originaldata.forEach(Consumer { postItem ->
                originaldata.add(postItem)
                notifyDataSetChanged()
                notifyItemInserted(originaldata.size - 1)
                notifyItemRemoved(originaldata.size - 1)
            })
        }
    }



    fun clear() {
        originaldata.clear()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): FileListItem {
        return originaldata[position]
    }
}