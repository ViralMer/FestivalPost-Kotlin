package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.R
import com.app.festivalpost.models.FrameItem
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import java.util.*

class ChooseFrameAdapter(var context: Context, var originaldata: ArrayList<FrameItem>) :
    RecyclerView.Adapter<ChooseFrameAdapter.ViewHolder>() {
    var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_choose_frame_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val frameItem = originaldata[position]
        if (frameItem.img_url != "") {
            Glide.with(context).load(frameItem.img_url).placeholder(R.drawable.placeholder_img)
                .error(
                    R.drawable.placeholder_img
                ).into(holder.ivphoto)
        }
        holder.layMain.tag = position
        holder.layMain.setOnClickListener { view ->
            val index = view.tag as Int
            val f = originaldata[index]
            onItemClickListener.onItemClicked(f, index)
        }
    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layMain: LinearLayout
        val ivphoto: RoundedImageView

        init {
            layMain = itemView.findViewById<View>(R.id.lay_main) as LinearLayout
            ivphoto = itemView.findViewById<View>(R.id.ivphoto) as RoundedImageView
        }
    }

    init {
        onItemClickListener = context as OnItemClickListener
    }
}