package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.models.BusinessCategory
import kotlin.collections.ArrayList

class BusinessCategoryViewAllItemAdapter(var context: Context, var originaldata: ArrayList<BusinessCategory?>) :
    RecyclerView.Adapter<BusinessCategoryViewAllItemAdapter.ViewHolder>() {
    var searchCount = 0
    var onItemClickListener: OnItemClickListener = context as OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View? =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_business_category_item, null,false)
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val businessCategoryItem = originaldata[position]

        holder.tvTitle.text=businessCategoryItem!!.category_name


        if (businessCategoryItem.is_selected!!) {
            holder.ivSelected.visibility = View.VISIBLE
        } else {
            holder.ivSelected.visibility = View.GONE
        }


        holder.itemView.tag = position
        holder.itemView.setOnClickListener { view ->
            val index = view.tag as Int
            val p = originaldata[index]
            onItemClickListener.onItemClicked(p, index)
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