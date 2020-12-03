package com.app.festivalpost.activity

import android.os.Build
import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Created by Qs on 16/5/30.
 */
class ExpandingViewPagerTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        var position = position
        //position = if (position < -1) else position
        //position = if (position > 1)  else position
        val tempScale = if (position < 0) 1 + position else 1 - position
        val slope = (MAX_SCALE - MIN_SCALE) / 1
        val scaleValue = MIN_SCALE + tempScale * slope
        page.scaleX = scaleValue
        page.scaleY = scaleValue
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.parent.requestLayout()
        }
    }

    companion object {
        const val MAX_SCALE = 1.0f
        const val MIN_SCALE = 0.8f
    }
}