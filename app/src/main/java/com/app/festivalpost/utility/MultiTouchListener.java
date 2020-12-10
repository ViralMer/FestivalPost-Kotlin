package com.app.festivalpost.utility;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MultiTouchListener implements OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    public boolean isRotateEnabled = true;
    public boolean isTranslateEnabled = true;
    public boolean isScaleEnabled = true;
    public float minimumScale = 0.5F;
    public float maximumScale = 10.0F;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(new MultiTouchListener.ScaleGestureListener((MultiTouchListener.ScaleGestureListener)null));

    public MultiTouchListener() {
    }

    private static float adjustAngle(float degrees) {
        if (degrees > 180.0F) {
            degrees -= 360.0F;
        } else if (degrees < -180.0F) {
            degrees += 360.0F;
        }

        return degrees;
    }

    private static void move(View view, MultiTouchListener.TransformInfo info) {
        computeRenderOffset(view, info.pivotX, info.pivotY);
        adjustTranslation(view, info.deltaX, info.deltaY);
        float scale = view.getScaleX() * info.deltaScale;
        scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
        view.setScaleX(scale);
        view.setScaleY(scale);
        float rotation = adjustAngle(view.getRotation() + info.deltaAngle);
        view.setRotation(rotation);
    }

    private static void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = new float[]{deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private static void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() != pivotX || view.getPivotY() != pivotY) {
            float[] prevPoint = new float[]{0.0F, 0.0F};
            view.getMatrix().mapPoints(prevPoint);
            view.setPivotX(pivotX);
            view.setPivotY(pivotY);
            float[] currPoint = new float[]{0.0F, 0.0F};
            view.getMatrix().mapPoints(currPoint);
            float offsetX = currPoint[0] - prevPoint[0];
            float offsetY = currPoint[1] - prevPoint[1];
            view.setTranslationX(view.getTranslationX() - offsetX);
            view.setTranslationY(view.getTranslationY() - offsetY);
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        this.mScaleGestureDetector.onTouchEvent(view, event);
        if (!this.isTranslateEnabled) {
            return true;
        } else {
            int action = event.getAction();
            int pointerIndex;
            switch(action & event.getActionMasked()) {
                case 0:
                    this.mPrevX = event.getX();
                    this.mPrevY = event.getY();
                    this.mActivePointerId = event.getPointerId(0);
                    break;
                case 1:
                case 3:
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    pointerIndex = event.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex != -1) {
                        float currX = event.getX(pointerIndex);
                        float currY = event.getY(pointerIndex);
                        if (!this.mScaleGestureDetector.isInProgress()) {
                            adjustTranslation(view, currX - this.mPrevX, currY - this.mPrevY);
                        }
                    }
                    break;
                case 4:
                case 5:
                default:
                    break;
                case 6:
                    pointerIndex = (action & '\uff00') >> 8;
                    int pointerId = event.getPointerId(pointerIndex);
                    if (pointerId == this.mActivePointerId) {
                        int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        this.mPrevX = event.getX(newPointerIndex);
                        this.mPrevY = event.getY(newPointerIndex);
                        this.mActivePointerId = event.getPointerId(newPointerIndex);
                    }
            }

            return true;
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener(ScaleGestureListener scaleGestureListener) {
            this.mPrevSpanVector = new Vector2D();
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
            this.mPivotX = detector.getFocusX();
            this.mPivotY = detector.getFocusY();
            this.mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector detector) {
            MultiTouchListener.TransformInfo info = MultiTouchListener.this.new TransformInfo((MultiTouchListener.TransformInfo)null);
            info.deltaScale = MultiTouchListener.this.isScaleEnabled ? detector.getScaleFactor() : 1.0F;
            info.deltaAngle = MultiTouchListener.this.isRotateEnabled ? Vector2D.getAngle(this.mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0F;
            info.deltaX = MultiTouchListener.this.isTranslateEnabled ? detector.getFocusX() - this.mPivotX : 0.0F;
            info.deltaY = MultiTouchListener.this.isTranslateEnabled ? detector.getFocusY() - this.mPivotY : 0.0F;
            info.pivotX = this.mPivotX;
            info.pivotY = this.mPivotY;
            info.minimumScale = MultiTouchListener.this.minimumScale;
            info.maximumScale = MultiTouchListener.this.maximumScale;
            MultiTouchListener.move(view, info);
            return false;
        }
    }

    private class TransformInfo {
        public float deltaX;
        public float deltaY;
        public float deltaScale;
        public float deltaAngle;
        public float pivotX;
        public float pivotY;
        public float minimumScale;
        public float maximumScale;

        private TransformInfo(TransformInfo transformInfo) {
        }
    }
}
