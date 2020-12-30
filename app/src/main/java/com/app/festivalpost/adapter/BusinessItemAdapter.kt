package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.ChoosePhotoActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.AddBusinessActivity
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.activity.PremiumActivity
import com.app.festivalpost.models.BusinessCategory
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.HomePageItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.makeramen.roundedimageview.RoundedImageView
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class BusinessItemAdapter(var context: Context, var originaldata: ArrayList<CurrentBusinessItem?>) : RecyclerView.Adapter<BusinessItemAdapter.BusinessItemAdapterViewHolder>() {

    var onItemClickListener: OnItemClickListener = context as OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessItemAdapter.BusinessItemAdapterViewHolder {
        val view: View? =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_business_item, null,false)
        return BusinessItemAdapterViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: BusinessItemAdapter.BusinessItemAdapterViewHolder, position: Int) {
        val businessCategoryItem = originaldata[position]

        holder.tvheading.text=businessCategoryItem!!.busi_name
        holder.tvdescription.text=businessCategoryItem.purc_end_date

        holder.ivSelected.loadImageFromUrl(businessCategoryItem.busi_logo!!)

        if (businessCategoryItem.plan_name=="Premium")
        {
            holder.tvupgrade.text=businessCategoryItem.plan_name
            holder.tvupgrade.background=ContextCompat.getDrawable(context,R.drawable.premium_bg)
        }
        else{
            holder.tvupgrade.text="Upgrade"
            holder.tvupgrade.background=ContextCompat.getDrawable(context,R.drawable.bg_gradient)
            holder.tvupgrade.onClick {
                context.launchActivity<PremiumActivity> {
                    putExtra("business_id",businessCategoryItem.busi_id!!.toString())
                }

            }
        }


        if (businessCategoryItem.is_current_business==1) {
            holder.ivcurrentbusiness.visibility = View.VISIBLE
        } else {
            holder.ivcurrentbusiness.visibility = View.GONE
        }

        holder.profileEdit.onClick {
            context.launchActivity<AddBusinessActivity> {
                putExtra("object",originaldata[position])
            }

        }






        holder.itemView.tag = position
        holder.itemView.setOnClickListener { view ->
            val index = view.tag as Int
            val p = originaldata[index]
            onItemClickListener.onItemClicked(p, index)
            for (i in originaldata.indices) {
                val pp = originaldata[i]
                originaldata[i] = pp
            }
            notifyDataSetChanged()
        }



    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class BusinessItemAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvheading: TextView = view.findViewById<View>(R.id.tvheading) as TextView
        val tvupgrade: TextView = view.findViewById<View>(R.id.tvupgrade) as TextView
        val tvdescription: TextView = view.findViewById<View>(R.id.tvdescription) as TextView
        val ivSelected :AppCompatImageView = view.findViewById<View>(R.id.ivlogo) as AppCompatImageView

        val profileEdit :AppCompatImageView = view.findViewById<View>(R.id.profileEdit) as AppCompatImageView
        val ivcurrentbusiness :AppCompatImageView = view.findViewById<View>(R.id.ivcurrentbusiness) as AppCompatImageView



    }


}