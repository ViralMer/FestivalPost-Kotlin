package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.festivalpost.models.FrameContentItem
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.app.festivalpost.activity.CustomFrameActivity
import com.app.festivalpost.R
import com.makeramen.roundedimageview.RoundedImageView
import java.util.ArrayList

class CustomFrameListAdapter(
    var context: Context,
    var originaldata: ArrayList<FrameContentItem>?,
    var title: String
) : RecyclerView.Adapter<CustomFrameListAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_category_festival_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postContentItem = originaldata!![position]
        Glide.with(context).load(postContentItem.banner_image)
            .placeholder(R.drawable.placeholder_img).error(
                R.drawable.placeholder_img
            ).into(holder.ivphoto)
        holder.ivphoto.tag = position
        holder.ivphoto.setOnClickListener { v ->
            val index = v.tag as Int
            val f = originaldata!![index]
            val detailact = Intent(context, CustomFrameActivity::class.java)
            detailact.putExtra("index", index)
            detailact.putExtra("custom_category_name", title)
            detailact.putExtra("object_post_content", f)
            context.startActivity(detailact)
        }
    }

    override fun getItemCount(): Int {
        return if (originaldata == null) 0 else originaldata!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivphoto: RoundedImageView = view.findViewById<View>(R.id.ivphoto) as RoundedImageView

    }
}