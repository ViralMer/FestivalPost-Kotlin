package com.app.festivalpost.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.app.festivalpost.R
import com.app.festivalpost.models.FestivalItem
import java.util.*

class SampleActivity : AppCompatActivity() {
    var dayItemArrayList: ArrayList<FestivalItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        dayItemArrayList = ArrayList()
        for (i in 0..9) {
            dayItemArrayList!!.add(FestivalItem())
        }
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        viewPager.clipChildren = false
        viewPager.pageMargin =
            resources.getDimensionPixelOffset(R.dimen.spacing_control_half)
        viewPager.offscreenPageLimit = dayItemArrayList!!.size
        viewPager.setPageTransformer(false, CarouselEffectTransformer(this@SampleActivity))
        viewPager.adapter = PagerAdapter()
    }

    internal inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        override fun getCount(): Int {
            return dayItemArrayList!!.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(this@SampleActivity)
                .inflate(R.layout.sample_custom_item, container, false)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }
}