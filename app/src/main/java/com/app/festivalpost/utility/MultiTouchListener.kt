package com.app.festivalpost.utility

import android.view.MotionEvent
import android.view.View

class MultiTouchListener : View.OnTouchListener {
    var isRotateEnabled = true
    var isTranslateEnabled = true
    var isScaleEnabled = true
    var minimumScale = 0.5f
    var maximumScale = 10.0f
    private var mActivePointerId = -1
    private var mPrevX = 0f
    private var mPrevY = 0f
    private val mScaleGestureDetector =
        ScaleGestureDetector(ScaleGestureListener((null as ScaleGestureListener?)!!))

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        mScaleGestureDetector.onTouchEvent(view, event)
        return if (!isTranslateEnabled) {
            true
        } else {
            val action = event.action
            val pointerIndex: Int
            when (action and event.actionMasked) {
                0 -> {
                    mPrevX = event.x
                    mPrevY = event.y
                    mActivePointerId = event.getPointerId(0)
                }
                1 -> mActivePointerId = -1
                2 -> {
                    pointerIndex = event.findPointerIndex(mActivePointerId)
                    if (pointerIndex != -1) {
                        val currX = event.getX(pointerIndex)
                        val currY = event.getY(pointerIndex)
                        if (!mScaleGestureDetector.isInProgress) {
                            adjustTranslation(
                                view,
                                currX - mPrevX,
                                currY - mPrevY
                            )
                        }
                    }
                }
                3 -> mActivePointerId = -1
                4, 5 -> {
                }
                6 -> {
                    pointerIndex = action and '\uff00'.toInt() shr 8
                    val pointerId = event.getPointerId(pointerIndex)
                    if (pointerId == mActivePointerId) {
                        val newPointerIndex = if (pointerIndex == 0) 1 else 0
                        mPrevX = event.getX(newPointerIndex)
                        mPrevY = event.getY(newPointerIndex)
                        mActivePointerId = event.getPointerId(newPointerIndex)
                    }
                }
                else -> {
                }
            }
            true
        }
    }

    private inner class ScaleGestureListener(scaleGestureListener: ScaleGestureListener) :
        ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var mPivotX = 0f
        private var mPivotY = 0f
        private val mPrevSpanVector: Vector2D
        override fun onScaleBegin(view: View?, detector: ScaleGestureDetector?): Boolean {
            mPivotX = detector!!.focusX
            mPivotY = detector.focusY
            mPrevSpanVector.set(detector.currentSpanVector)
            return true
        }

        override fun onScale(view: View?, detector: ScaleGestureDetector?): Boolean {
            val info = this@MultiTouchListener.TransformInfo(null as TransformInfo?)
            info.deltaScale = if (isScaleEnabled) detector!!.scaleFactor else 1.0f
            info.deltaAngle = if (isRotateEnabled) Vector2D.getAngle(
                mPrevSpanVector, detector!!.currentSpanVector
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

        init {
            mPrevSpanVector = Vector2D()
        }
    }

    private inner class TransformInfo(transformInfo: TransformInfo?) {
        var deltaX = 0f
        var deltaY = 0f
        var deltaScale = 0f
        var deltaAngle = 0f
        var pivotX = 0f
        var pivotY = 0f
        var minimumScale = 0f
        var maximumScale = 0f
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
        private fun adjustAngle(degrees: Float): Float {
            var degrees = degrees
            if (degrees > 180.0f) {
                degrees -= 360.0f
            } else if (degrees < -180.0f) {
                degrees += 360.0f
            }
            return degrees
        }

        private fun move(view: View?, info: TransformInfo) {
            computeRenderOffset(view, info.pivotX, info.pivotY)
            adjustTranslation(view, info.deltaX, info.deltaY)
            var scale = view!!.scaleX * info.deltaScale
            scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale))
            view.scaleX = scale
            view.scaleY = scale
            val rotation = adjustAngle(
                view.rotation + info.deltaAngle
            )
            view.rotation = rotation
        }

        private fun adjustTranslation(view: View?, deltaX: Float, deltaY: Float) {
            val deltaVector = floatArrayOf(deltaX, deltaY)
            view!!.matrix.mapVectors(deltaVector)
            view.translationX = view.translationX + deltaVector[0]
            view.translationY = view.translationY + deltaVector[1]
        }

        private fun computeRenderOffset(view: View?, pivotX: Float, pivotY: Float) {
            if (view!!.pivotX != pivotX || view.pivotY != pivotY) {
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
    }
}