package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.ChoosePhotoActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.VideoDetailActivity
import com.app.festivalpost.models.HomePageItem
import com.app.festivalpost.models.VideoPageItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.loadImageFromUrl
import com.emegamart.lelys.utils.extensions.onClick
import com.makeramen.roundedimageview.RoundedImageView
import kotlin.collections.ArrayList

class VideoCategoryItemAdapter(var context: Context, var originaldata: ArrayList<VideoPageItem?>) :
    RecyclerView.Adapter<VideoCategoryItemAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View? =
            LayoutInflater.from(parent.context).inflate(R.layout.item_custom_category, null,false)
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoPageItem = originaldata[position]


        Glide.with(context).load(videoPageItem!!.video_image).error(R.drawable.placeholder_img).placeholder(R.drawable.placeholder_img).into(holder.imageView)
        holder.tvTitle.text=videoPageItem.video_name

        holder.tvNew.hide()

        holder.itemView.onClick {
            context.launchActivity<VideoDetailActivity> {
                putExtra("category_id",videoPageItem.id)
                putExtra("category_name",videoPageItem.video_name)
            }
        }



    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView
        val tvNew: TextView = view.findViewById<View>(R.id.tvDate) as TextView
        val imageView:RoundedImageView= view.findViewById<View>(R.id.ivphoto) as RoundedImageView




    }
}