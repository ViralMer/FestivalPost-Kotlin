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
import com.app.festivalpost.models.HomePageItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.loadImageFromUrl
import com.emegamart.lelys.utils.extensions.onClick
import com.makeramen.roundedimageview.RoundedImageView
import kotlin.collections.ArrayList

class CategoryItemAdapter(var context: Context, var originaldata: ArrayList<HomePageItem?>) :
    RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View? =
            LayoutInflater.from(parent.context).inflate(R.layout.item_custom_category, null,false)
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homePageItem = originaldata[position]


        holder.imageView.loadImageFromUrl(homePageItem!!.fest_image!!)
        holder.tvTitle.text=homePageItem.fest_name

        holder.tvNew.hide()

        holder.itemView.onClick {
            context.launchActivity<ChoosePhotoActivity> {
                putExtra("category_id",homePageItem.fest_id)
                putExtra("category_name",homePageItem.fest_name)
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