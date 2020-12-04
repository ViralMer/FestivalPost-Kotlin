package com.app.festivalpost.utility

import android.graphics.Rect
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created on 18/01/2017.
 *
 * @author [Burhanuddin Rashid](https://github.com/burhanrashid52)
 *
 *
 */
class MultiTouchListenerNewNotRotate : View.OnTouchListener {
    private val mGestureListener: GestureDetector
    private val isRotateEnabled = true
    private val isTranslateEnabled = true
    private val isScaleEnabled = true
    private val minimumScale = 0.5f
    private val maximumScale = 10.0f
    private var mActivePointerId = INVALID_POINTER_ID
    private var mPrevX = 0f
    private var mPrevY = 0f
    private var mPrevRawX = 0f
    private var mPrevRawY = 0f
    private val mScaleGestureDetector: ScaleGestureDetector
    private val location = IntArray(2)
    private val outRect: Rect? = null
    private var onMultiTouchListener: OnMultiTouchListener? = null
    private var mOnGestureControl: OnGestureControl? = null
    private val frameLayout: View? = null
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        mScaleGestureDetector.onTouchEvent(view, event)
        mGestureListener.onTouchEvent(event)
        if (!isTranslateEnabled) {
            return true
        }
        val action = event.action
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()
        when (action and event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mPrevX = event.x
                mPrevY = event.y
                mPrevRawX = event.rawX
                mPrevRawY = event.rawY
                mActivePointerId = event.getPointerId(0)
                view.bringToFront()
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndexMove = event.findPointerIndex(mActivePointerId)
                if (pointerIndexMove != -1) {
                    val currX = event.getX(pointerIndexMove)
                    val currY = event.getY(pointerIndexMove)
                    if (!mScaleGestureDetector.isInProgress) {
                        adjustTranslation(view, currX - mPrevX, currY - mPrevY)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> mActivePointerId = INVALID_POINTER_ID
            MotionEvent.ACTION_UP -> mActivePointerId = INVALID_POINTER_ID
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndexPointerUp =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = event.getPointerId(pointerIndexPointerUp)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndexPointerUp == 0) 1 else 0
                    mPrevX = event.getX(newPointerIndex)
                    mPrevY = event.getY(newPointerIndex)
                    mActivePointerId = event.getPointerId(newPointerIndex)
                }
            }
        }
        return true
    }

    private fun isViewInBounds(view: View, x: Int, y: Int): Boolean {
        view.getDrawingRect(outRect)
        view.getLocationOnScreen(location)
        outRect!!.offset(location[0], location[1])
        return outRect.contains(x, y)
    }

    fun setOnMultiTouchListener(onMultiTouchListener: OnMultiTouchListener?) {
        this.onMultiTouchListener = onMultiTouchListener
    }

    private inner class ScaleGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var mPivotX = 0f
        private var mPivotY = 0f
        private val mPrevSpanVector = Vector2D()
        override fun onScaleBegin(view: View?, detector: ScaleGestureDetector?): Boolean {
            mPivotX = detector!!.focusX
            mPivotY = detector.focusY
            mPrevSpanVector.set(detector.currentSpanVector)
            return true
        }

        override fun onScale(view: View?, detector: ScaleGestureDetector?): Boolean {
            val info: TransformInfo = TransformInfo()
            info.deltaScale = if (isScaleEnabled) detector!!.scaleFactor else 1.0f
            info.deltaAngle = if (isRotateEnabled) Vector2D.getAngle(
                mPrevSpanVector,
                detector!!.currentSpanVector
            ) else 0.0f
            info.deltaX = if (isTranslateEnabled) detector!!.focusX - mPivotX else 0.0f
            info.deltaY = if (isTranslateEnabled) detector!!.focusY - mPivotY else 0.0f
            info.pivotX = mPivotX
            info.pivotY = mPivotY
            info.minimumScale = minimumScale
            info.maximumScale = maximumScale
            move(view, info)
            return false
        }
    }

    inner class TransformInfo {
        var deltaX = 0f
        var deltaY = 0f
        var deltaScale = 0f
        var deltaAngle = 0f
        var pivotX = 0f
        var pivotY = 0f
        var minimumScale = 0f
        var maximumScale = 0f
    }

    interface OnMultiTouchListener {
        fun onEditTextClickListener(text: String?, colorCode: Int)
        fun onRemoveViewListener(removedView: View?)
    }

    interface OnGestureControl {
        fun onClick()
        fun onLongClick()
        fun onTouch()
    }

    fun setOnGestureControl(onGestureControl: OnGestureControl?) {
        mOnGestureControl = onGestureControl
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (mOnGestureControl != null) {
                mOnGestureControl!!.onClick()
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
            if (mOnGestureControl != null) {
                mOnGestureControl!!.onLongClick()
                Log.d("OnLoginCliked", "" + e.action)
            }
        }

        override fun onShowPress(e: MotionEvent) {
            super.onShowPress(e)
            if (mOnGestureControl != null) {
                mOnGestureControl!!.onTouch()
            }
        }
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
        fun adjustAngle(degrees: Float): Float {
            var degrees = degrees
            if (degrees > 180.0f) {
                degrees -= 360.0f
            } else if (degrees < -180.0f) {
                degrees += 360.0f
            }
            return degrees
        }

        fun move(view: View?, info: TransformInfo) {
            computeRenderOffset(view, info.pivotX, info.pivotY)
            adjustTranslation(view, info.deltaX, info.deltaY)
            var scale = view!!.scaleX * info.deltaScale
            scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale))
            view.scaleX = scale
            view.scaleY = scale

            //float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
            //view.setRotation(rotation);
        }

        fun adjustTranslation(view: View?, deltaX: Float, deltaY: Float) {
            val deltaVector = floatArrayOf(deltaX, deltaY)
            view!!.matrix.mapVectors(deltaVector)
            view.translationX = view.translationX + deltaVector[0]
            view.translationY = view.translationY + deltaVector[1]
        }

        fun computeRenderOffset(view: View?, pivotX: Float, pivotY: Float) {
            if (view!!.pivotX == pivotX && view.pivotY == pivotY) {
                return
            }
            val prevPoint = floatArrayOf(0.0f, 0.0f)
            view.matrix.mapPoints(prevPoint)
            view.pivotX = pivotX
            view.pivotY = pivotY
            val currPoint = floatArrayOf(0.0f, 0.0f)
            view.matrix.mapPoints(currPoint)
            val offsetX = currPoint[0] - prevPoint[0]
            val offsetY = currPoint[1] - prevPoint[1]
            view.translationX = view.translationX - offsetX
            view.translationY = view.translationY - offsetY
        }
    }

    init {
        mScaleGestureDetector = ScaleGestureDetector(ScaleGestureListener())
        mGestureListener = GestureDetector(GestureListener())
    }
}