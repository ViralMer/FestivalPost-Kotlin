package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.app.festivalpost.activity.CustomFrameActivity
import com.app.festivalpost.R
import com.app.festivalpost.models.CustomCategoryItem
import com.app.festivalpost.models.FrameContentListItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.onClick
import com.makeramen.roundedimageview.RoundedImageView
import java.util.*

class CustomFrameAdapter(var context: Context, var originaldata: ArrayList<CustomCategoryItem>?) :
    RecyclerView.Adapter<CustomFrameAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.custom_view_design, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val frameContentItem = originaldata!![position]
        holder.tvTitle.text = frameContentItem.name
        Glide.with(context).load(frameContentItem.custom_image).placeholder(R.drawable.placeholder_img).error(R.drawable.placeholder_img).into(holder.ivphoto)

        holder.itemView.onClick {
            context.launchActivity<CustomFrameActivity> {
                putExtra("custom_category_id",frameContentItem.custom_cateogry_id)
            }
        }


    }

    override fun getItemCount(): Int {
        return if (originaldata == null) 0 else originaldata!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivphoto: RoundedImageView = view.findViewById<View>(R.id.ivphoto) as RoundedImageView
        val tvTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView


    }
}