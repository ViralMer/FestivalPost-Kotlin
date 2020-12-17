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
import com.emegamart.lelys.utils.extensions.*
import com.makeramen.roundedimageview.RoundedImageView
import kotlin.collections.ArrayList

class CustomFestivalVideoItemAdapter(var context: Context, var originaldata: ArrayList<VideoPageItem?>) :
    RecyclerView.Adapter<CustomFestivalVideoItemAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_category, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoPageItem = originaldata!![position]

        Glide.with(context).load(videoPageItem!!.video_image).error(R.drawable.placeholder_img).placeholder(R.drawable.placeholder_img).into(holder.imageView)
        holder.tvTitle.text=videoPageItem.video_image;

        holder.tvNew.show()
        holder.tvNew.text=videoPageItem.video_date!!

        holder.itemView.onClick {
            context.launchActivity<VideoDetailActivity> {
                putExtra("video_id",videoPageItem.id)
                putExtra("video_name",videoPageItem.video_name)
                putExtra("video_date",videoPageItem.video_date)
            }
        }



    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView
        val tvNew: TextView
        val imageView: ImageView

        init {
            tvTitle = view.findViewById<View>(R.id.tvTitle) as TextView
            tvNew = view.findViewById<View>(R.id.tvDate) as TextView
            imageView = view.findViewById<View>(R.id.ivphoto) as RoundedImageView
        }
    }
}