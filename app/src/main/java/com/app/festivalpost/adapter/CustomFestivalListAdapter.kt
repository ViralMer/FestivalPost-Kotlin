package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.ChoosePhotoActivity
import com.app.festivalpost.R
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.PostContentItem
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import java.util.*

class CustomFestivalListAdapter(
    var context: Context,
    var originaldata: ArrayList<PostContentItem>?,
    var title: String
) : RecyclerView.Adapter<CustomFestivalListAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_category_festival_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postContentItem = originaldata!![position]
        Glide.with(context).load(postContentItem.post_content)
            .placeholder(R.drawable.placeholder_img).error(
                R.drawable.placeholder_img
            ).into(holder.ivphoto)
        holder.ivphoto.tag = position
        holder.ivphoto.setOnClickListener { v ->
            val index = v.tag as Int
            val f = originaldata!![index]
            val detailact = Intent(context, ChoosePhotoActivity::class.java)
            Global.storePreference(Constant.PREF_PHOTO_INDEX, index)
            Global.storePreference("category_name", title)
            detailact.putExtra("object_post_content", f)
            context.startActivity(detailact)
        }
    }

    override fun getItemCount(): Int {
        return if (originaldata == null) 0 else originaldata!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivphoto: RoundedImageView

        init {
            ivphoto = view.findViewById<View>(R.id.ivphoto) as RoundedImageView
        }
    }
}