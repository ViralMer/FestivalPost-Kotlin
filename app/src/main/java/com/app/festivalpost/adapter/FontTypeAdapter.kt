package com.app.festivalpost.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.ChoosePhotoActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.ChooseFrameForPhotoActivityNew
import com.app.festivalpost.activity.FontOnItemClickListener
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.models.BusinessCategory
import com.app.festivalpost.models.FontTypeList
import com.app.festivalpost.models.HomePageItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.launchActivity
import com.emegamart.lelys.utils.extensions.onClick
import com.emegamart.lelys.utils.extensions.show
import com.makeramen.roundedimageview.RoundedImageView
import kotlin.collections.ArrayList

class FontTypeAdapter(var context: Context, var originaldata: ArrayList<FontTypeList?>,var text:String) :
    RecyclerView.Adapter<FontTypeAdapter.ViewHolder>() {
    var searchCount = 0
    var onItemClickListener: FontOnItemClickListener = context as FontOnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View? =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_business_category_item, null,false)
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val businessCategoryItem = originaldata[position]

        holder.tvTitle.text=text
        val typeface = Typeface.createFromAsset(context.assets, businessCategoryItem!!.name)
        holder.tvTitle.typeface = typeface
        if (businessCategoryItem.is_selected!!) {
            holder.ivSelected.visibility = View.VISIBLE
        } else {
            holder.ivSelected.visibility = View.GONE
        }


        holder.itemView.tag = position
        holder.itemView.setOnClickListener { view ->
            val index = view.tag as Int
            val p = originaldata[index]
            onItemClickListener.onFontItemClicked(p, index)
            for (i in originaldata.indices) {
                val pp = originaldata[i]
                pp!!.is_selected = i == index
                originaldata[i] = pp
            }
            notifyDataSetChanged()
        }




    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById<View>(R.id.tv_category_name) as TextView
        val ivSelected :AppCompatImageView = view.findViewById<View>(R.id.iv_clicked) as AppCompatImageView





    }

}