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
import com.emegamart.lelys.utils.extensions.*
import com.makeramen.roundedimageview.RoundedImageView
import kotlin.collections.ArrayList

class CustomFestivalItemAdapter(var context: Context, var originaldata: ArrayList<HomePageItem?>) :
    RecyclerView.Adapter<CustomFestivalItemAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_category, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homePageItem = originaldata!![position]

        Glide.with(context).load(homePageItem!!.fest_image).error(R.drawable.placeholder_img).placeholder(R.drawable.placeholder_img).into(holder.imageView)
        //holder.imageView.loadImageFromUrl(homePageItem!!.fest_image!!)
        holder.tvTitle.text=homePageItem.fest_name;

        holder.tvNew.show()
        holder.tvNew.text=homePageItem.fest_date!!

        holder.itemView.onClick {
            context.launchActivity<ChoosePhotoActivity> {
                putExtra("category_id",homePageItem.fest_id)
                putExtra("category_name",homePageItem.fest_name)
                putExtra("category_date",homePageItem.fest_date)
            }
        }



    }

    override fun getItemCount(): Int {
        return if (originaldata == null) 0 else originaldata!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView
        val tvNew: TextView = view.findViewById<View>(R.id.tvDate) as TextView
        val imageView: RoundedImageView = view.findViewById<View>(R.id.ivphoto) as RoundedImageView

    }
}