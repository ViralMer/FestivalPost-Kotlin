package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.R
import com.app.festivalpost.models.VideoLanguageItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.show
import com.makeramen.roundedimageview.RoundedImageView
import kotlin.collections.ArrayList

class ChooseVideoAdapter(var context: Context, var originaldata: ArrayList<VideoLanguageItem?>) :
    RecyclerView.Adapter<ChooseVideoAdapter.ViewHolder>() {
    var searchCount = 0
    var onItemClickListener: OnItemClickListener = context as OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.custom_choose_photo_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoItem = originaldata[position]
        if (photoItem!!.image != null && !photoItem.image.equals("", ignoreCase = true)) {
            Glide.with(context).load(photoItem.image).placeholder(R.drawable.placeholder_img)
                .error(
                    R.drawable.placeholder_img
                ).into(holder.ivphoto)
        }

        if (photoItem!!.type=="0")
        {
            holder.tvplan.text=context.getString(R.string.free)
            holder.tvplan.setBackgroundResource(R.drawable.bg_gradient)
            holder.tvplan.show()
        }
        else{
            holder.tvplan.text=context.getString(R.string.preimum)
            holder.tvplan.setBackgroundResource(R.drawable.premium_bg)
            holder.tvplan.hide()
        }

       if (photoItem.isIs_selected) {
            holder.viewselected.visibility = View.VISIBLE
        } else {
            holder.viewselected.visibility = View.GONE
        }
        holder.layMain.tag = position
        holder.layMain.setOnClickListener { view ->
            val index = view.tag as Int
            val p = originaldata[index]
            onItemClickListener.onItemClicked(p, 0)
            for (i in originaldata.indices) {
                val pp = originaldata[i]
                pp!!.isIs_selected = i == index
                originaldata[i] = pp
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layMain: LinearLayout
        val ivphoto: RoundedImageView
        val viewselected: View
        val tvplan: TextView = itemView.findViewById(R.id.tvPlan) as TextView


        init {
            layMain = itemView.findViewById<View>(R.id.lay_main) as LinearLayout
            ivphoto = itemView.findViewById<View>(R.id.ivphoto) as RoundedImageView
            viewselected = itemView.findViewById(R.id.viewselected) as View

        }
    }

}