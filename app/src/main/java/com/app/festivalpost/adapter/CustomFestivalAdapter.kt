package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.ChoosePhotoActivity
import com.app.festivalpost.R
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.IncidentsItem
import java.util.*

class CustomFestivalAdapter(var context: Context, var originaldata: ArrayList<IncidentsItem>?) :
    RecyclerView.Adapter<CustomFestivalAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.custom_view_design, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postContentItem = originaldata!![position]
        holder.tvTitle.text = postContentItem.title
        if (postContentItem.postContentArrayList.size > 3) {
            holder.imageView.visibility = View.VISIBLE
        } else {
            holder.imageView.visibility = View.GONE
        }
        val customFestivalListAdapter = CustomFestivalListAdapter(
            context, postContentItem.postContentArrayList, postContentItem.title
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
            val detailact = Intent(context, ChoosePhotoActivity::class.java)
            Global.storePreference(Constant.PREF_PHOTO_INDEX, index)
            Global.storePreference("category_name", f.title)
            detailact.putExtra("object_post", f)
            context.startActivity(detailact)
        }
    }

    override fun getItemCount(): Int {
        return if (originaldata == null) 0 else originaldata!!.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivphoto: RecyclerView
        val tvTitle: TextView
        val tvviewall: TextView
        val imageView: ImageView

        init {
            ivphoto = view.findViewById<View>(R.id.rvdatanew) as RecyclerView
            tvTitle = view.findViewById<View>(R.id.tvTitle) as TextView
            tvviewall = view.findViewById<View>(R.id.tvviewall) as TextView
            imageView = view.findViewById<View>(R.id.ivNext) as ImageView
        }
    }
}