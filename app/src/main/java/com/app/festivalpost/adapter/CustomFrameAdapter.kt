package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.app.festivalpost.CustomFrameActivity
import com.app.festivalpost.R
import com.app.festivalpost.models.FrameContentListItem
import java.util.*

class CustomFrameAdapter(var context: Context, var originaldata: ArrayList<FrameContentListItem>?) :
    RecyclerView.Adapter<CustomFrameAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.custom_view_design, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val frameContentItem = originaldata!![position]
        Log.d("imageNameimageName", "" + frameContentItem.name + "Size" + originaldata!!.size)
        holder.tvTitle.text = frameContentItem.name
        if (frameContentItem.contentItemArrayList!!.size > 3) {
            holder.imageView.visibility = View.VISIBLE
        } else {
            holder.imageView.visibility = View.GONE
        }
        val customFestivalListAdapter = CustomFrameListAdapter(
            context,
            frameContentItem.contentItemArrayList,
            frameContentItem.name!!
        )
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.ivphoto.layoutManager = linearLayoutManager
        holder.ivphoto.adapter = customFestivalListAdapter
        holder.imageView.setOnClickListener { holder.ivphoto.smoothScrollBy(300, 0) }
        holder.tvviewall.tag = position
        holder.tvviewall.setOnClickListener { view ->
            val index = view.tag as Int
            val f = originaldata!![index]
            val detailact = Intent(context, CustomFrameActivity::class.java)
            detailact.putExtra("custom_category_name", f.name)
            detailact.putExtra("object_post", f)
            detailact.putExtra("index", 0)
            context.startActivity(detailact)
        }
    }

    override fun getItemCount(): Int {
        return if (originaldata == null) 0 else originaldata!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivphoto: RecyclerView = view.findViewById<View>(R.id.rvdatanew) as RecyclerView
        val tvTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView
        val tvviewall: TextView = view.findViewById<View>(R.id.tvviewall) as TextView
        val imageView: ImageView = view.findViewById<View>(R.id.ivNext) as ImageView

    }
}