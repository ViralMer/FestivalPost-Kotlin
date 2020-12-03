package com.emegamart.lelys.utils

import android.R
import android.content.Context
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView

abstract class HidingScrollListener(h: Context) : RecyclerView.OnScrollListener() {
    private var mToolbarOffset = 0
    private var mControlsVisible = true
    private var mToolbarHeight = 0
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mControlsVisible) {
                if (mToolbarOffset > HIDE_THRESHOLD) {
                    setInvisible()
                } else {
                    setVisible()
                }
            } else {
                if (mToolbarHeight - mToolbarOffset > SHOW_THRESHOLD) {
                    setVisible()
                } else {
                    setInvisible()
                }
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        clipToolbarOffset()
        onMoved(mToolbarOffset)
        if (mToolbarOffset < mToolbarHeight && dy > 0 || mToolbarOffset > 0 && dy < 0) {
            mToolbarOffset += dy
        }
    }

    private fun clipToolbarOffset() {
        if (mToolbarOffset > mToolbarHeight) {
            mToolbarOffset = mToolbarHeight
        } else if (mToolbarOffset < 0) {
            mToolbarOffset = 0
        }
    }

    private fun setVisible() {
        if (mToolbarOffset > 0) {
            onShow()
            mToolbarOffset = 0
        }
        mControlsVisible = true
    }

    private fun setInvisible() {
        if (mToolbarOffset < mToolbarHeight) {
            onHide()
            mToolbarOffset = mToolbarHeight
        }
        mControlsVisible = false
    }

    abstract fun onMoved(distance: Int)
    abstract fun onShow()
    abstract fun onHide()

    companion object {
        private const val HIDE_THRESHOLD = 10f
        private const val SHOW_THRESHOLD = 70f
    }

    init {
        val tv = TypedValue()
        if (h.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
            mToolbarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, h.resources.displayMetrics)
        }
    }
}