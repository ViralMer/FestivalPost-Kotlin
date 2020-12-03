package com.app.festivalpost.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.OnItemClickListener
import com.app.festivalpost.R
import com.app.festivalpost.models.FramePreview
import com.bumptech.glide.Glide
import java.util.*

class FrameChooseAdapter(var context: Context, var originaldata: ArrayList<FramePreview>) :
    RecyclerView.Adapter<FrameChooseAdapter.ViewHolder>() {
    var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_frame_dynamic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val framePreview = originaldata[position]
        if (position >= 19) {
            Glide.with(context).load(framePreview.dynamic_images)
                .placeholder(R.drawable.placeholder_img).error(
                    R.drawable.placeholder_img
                ).into(holder.layMain)
        } else {
            holder.layMain.setImageResource(framePreview.images!!)
        }
        holder.layMain.tag = position
        if (framePreview.isIs_selected) {
            holder.ivselected.visibility = View.VISIBLE
        } else {
            holder.ivselected.visibility = View.GONE
        }
        holder.layMain.tag = position
        holder.layMain.setOnClickListener { view ->
            val index = view.tag as Int
            val p = originaldata[index]
            onItemClickListener.onItemClicked(p, index)
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


        /*

        holder.layMain.setTag(position);
        holder.layMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = (int) view.getTag();
                FrameItem f = originaldata.get(index);
                onItemClickListener.onItemClicked(f,index);
            }
        });*/
    }

    override fun getItemCount(): Int {
        Log.d("original", "" + originaldata.size)
        return originaldata.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layMain: ImageView = itemView.findViewById<View>(R.id.ivFrameDynamic) as ImageView
        val ivselected: ImageView = itemView.findViewById<View>(R.id.ivselected) as ImageView
        val frameLayout: FrameLayout = itemView.findViewById<View>(R.id.frameLayout) as FrameLayout

    }

    fun getURLForResource(resourceId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + resourceId)
            .toString()
    }

    init {
        onItemClickListener = context as OnItemClickListener
    }
}