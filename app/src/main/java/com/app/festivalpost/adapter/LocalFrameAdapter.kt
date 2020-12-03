package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.app.festivalpost.R
import com.app.festivalpost.models.BusinessItem
import com.app.festivalpost.models.LocalFrameItem
import com.bumptech.glide.Glide
import java.util.*

class LocalFrameAdapter(
    var context: Context,
    var frameItemArrayList: ArrayList<LocalFrameItem>,
    var businessItem: BusinessItem?
) : PagerAdapter() {
    override fun getCount(): Int {
        return frameItemArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val frameItem = frameItemArrayList[position]
        val view = LayoutInflater.from(context).inflate(frameItem.layout_id, container, false)
        val ivframelogo = view.findViewById<ImageView>(R.id.ivframelogo)
        val tvframephone = view.findViewById<TextView>(R.id.tvframephone)
        val tvframeemail = view.findViewById<TextView>(R.id.tvframeemail)
        val tvframeweb = view.findViewById<TextView>(R.id.tvframeweb)
        if (businessItem != null) {
            if (businessItem!!.busiLogo != "") {
                Glide.with(context).load(businessItem!!.busiLogo)
                    .placeholder(R.drawable.placeholder_img).error(
                        R.drawable.placeholder_img
                    ).into(ivframelogo)
            }
            tvframephone.text = businessItem!!.busiMobile
            tvframeemail.text = businessItem!!.busiEmail
            tvframeweb.text = businessItem!!.busiWebsite
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}