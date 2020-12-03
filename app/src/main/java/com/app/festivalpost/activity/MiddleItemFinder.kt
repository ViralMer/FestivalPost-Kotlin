package com.app.festivalpost.activity

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.festivalpost.activity.MiddleItemFinder.MiddleItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.MiddleItemFinder

class MiddleItemFinder(
    private val context: Context,
    private val layoutManager: LinearLayoutManager,
    private val callback: MiddleItemCallback,
    private val controlState: Int
) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (controlState == ALL_STATES || newState == controlState) {
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val itemsCount = lastVisible - firstVisible + 1
            val screenCenter = context.resources.displayMetrics.widthPixels / 2
            var minCenterOffset = Int.MAX_VALUE
            var middleItemIndex = 0
            for (index in 0 until itemsCount) {
                val listItem = layoutManager.getChildAt(index) ?: return
                val leftOffset = listItem.left
                val rightOffset = listItem.right
                val centerOffset =
                    Math.abs(leftOffset - screenCenter) + Math.abs(rightOffset - screenCenter)
                if (minCenterOffset > centerOffset) {
                    minCenterOffset = centerOffset
                    middleItemIndex = index + firstVisible
                }
            }
            callback.scrollFinished(middleItemIndex)
        }
    }

    interface MiddleItemCallback {
        fun scrollFinished(middleElement: Int)
    }

    companion object {
        const val ALL_STATES = 10
    }
}