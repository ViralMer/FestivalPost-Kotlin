package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.OnItemClickListener
import com.app.festivalpost.R
import com.app.festivalpost.models.VideoListItem
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import java.util.*

class ChooseVideoAdapter(var context: Context, var originaldata: ArrayList<VideoListItem>) :
    RecyclerView.Adapter<ChooseVideoAdapter.ViewHolder>() {
    var searchCount = 0
    var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.custom_choose_photo_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoItem = originaldata[position]
        if (photoItem.thumbnail != null && !photoItem.thumbnail.equals("", ignoreCase = true)) {
            Glide.with(context).load(photoItem.thumbnail).placeholder(R.drawable.placeholder_img)
                .error(
                    R.drawable.placeholder_img
                ).into(holder.ivphoto)
        }

        //holder.textView.setText(photoItem.getDate());
        /*if (position==0) {
            photoItem.setIs_selected(true);
        }
            else{
                photoItem.setIs_selected(false);
            }*/if (photoItem.isIs_selected) {
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
                if (i != index) {
                    pp.setIs_selected(false)
                } else {
                    pp.setIs_selected(true)
                }
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
        val textView: TextView

        init {
            layMain = itemView.findViewById<View>(R.id.lay_main) as LinearLayout
            ivphoto = itemView.findViewById<View>(R.id.ivphoto) as RoundedImageView
            viewselected = itemView.findViewById(R.id.viewselected) as View
            textView = itemView.findViewById<View>(R.id.tvname) as TextView
        }
    }

    init {
        onItemClickListener = context as OnItemClickListener
    }
}