package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.activity.*
import com.app.festivalpost.api.RestClient
import com.app.festivalpost.models.BusinessCategory
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.HomePageItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.models.BaseResponse
import com.emegamart.lelys.utils.extensions.*
import com.makeramen.roundedimageview.RoundedImageView
import retrofit2.Call
import retrofit2.Response
import java.io.Serializable
import java.util.*
import retrofit2.Callback
import kotlin.collections.ArrayList

class BusinessItemAdapter(var context: Context, var originaldata: ArrayList<CurrentBusinessItem?>) : RecyclerView.Adapter<BusinessItemAdapter.BusinessItemAdapterViewHolder>() {

    var onItemClickListener: OnItemClickListener = context as OnItemClickListener
    var onFontItemClickListener: FontOnItemClickListener = context as FontOnItemClickListener

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
            holder.tvDelete.visibility=View.GONE
            holder.tvDelete.visibility=View.GONE
            holder.tvupgrade.background=ContextCompat.getDrawable(context,R.drawable.premium_bg)
        }
        else{
            holder.tvupgrade.text="Upgrade"
            holder.tvDelete.visibility=View.VISIBLE
            holder.tvupgrade.background=ContextCompat.getDrawable(context,R.drawable.bg_gradient)
            holder.tvupgrade.setOnClickListener {
                context.launchActivity<PremiumActivity> {
                    putExtra("business_id",businessCategoryItem.busi_id!!.toString())
                }

            }
        }

        holder.tvDelete.tag = position
        holder.tvDelete.setOnClickListener { view ->

            val index = view.tag as Int
            val p = originaldata[index]
            onFontItemClickListener.onFontItemClicked(p, index)
            /*val materialAlertDialogBuilder = AlertDialog.Builder(context)
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.custom_add_busines_dialog, null)
            val tvTitle: TextView
            val tvMessage: TextView
            val btnOk: Button
            val btnCancel: Button
            tvTitle = view.findViewById(R.id.tvTitle)
            tvMessage = view.findViewById(R.id.tvMessage)
            btnOk = view.findViewById(R.id.btnOk)
            btnCancel = view.findViewById(R.id.btnCancel)
            btnOk.text = "Ok"
            tvTitle.text = "Warning"
            tvMessage.text = "Are you sure want to delete Business?"
            materialAlertDialogBuilder.setView(view).setCancelable(true)
            val b = materialAlertDialogBuilder.create()
            btnCancel.setOnClickListener { b.dismiss() }
            btnOk.setOnClickListener {
                b.show()
            }*/
        }


        if (businessCategoryItem.is_current_business==1) {
            holder.ivcurrentbusiness.visibility = View.VISIBLE
        } else {
            holder.ivcurrentbusiness.visibility = View.GONE
        }

        holder.profileEdit.setOnClickListener {
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
        val tvDelete: TextView = view.findViewById<View>(R.id.tvDelete) as TextView
        val tvdescription: TextView = view.findViewById<View>(R.id.tvdescription) as TextView
        val ivSelected :AppCompatImageView = view.findViewById<View>(R.id.ivlogo) as AppCompatImageView

        val profileEdit :AppCompatImageView = view.findViewById<View>(R.id.profileEdit) as AppCompatImageView
        val ivcurrentbusiness :AppCompatImageView = view.findViewById<View>(R.id.ivcurrentbusiness) as AppCompatImageView



    }


}