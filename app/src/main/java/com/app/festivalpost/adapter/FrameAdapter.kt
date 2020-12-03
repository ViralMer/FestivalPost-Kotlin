package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.app.festivalpost.R
import com.app.festivalpost.models.FrameItem
import com.bumptech.glide.Glide
import java.util.*

class FrameAdapter(var context: Context, var frameItemArrayList: ArrayList<FrameItem>) :
    PagerAdapter() {
    override fun getCount(): Int {
        return frameItemArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(context).inflate(R.layout.custom_frame_item, container, false)
        val frameItem = frameItemArrayList[position]
        val ivframe = view.findViewById<ImageView>(R.id.ivframe)
        val tvusethis = view.findViewById<TextView>(R.id.tvusethis)
        if (!frameItem.img_url.equals("", ignoreCase = true)) {
            Glide.with(context).load(frameItem.img_url).into(ivframe)
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}