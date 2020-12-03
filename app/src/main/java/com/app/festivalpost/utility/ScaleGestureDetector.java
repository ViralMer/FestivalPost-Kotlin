package com.app.festivalpost.utility;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class ScaleGestureDetector {
    private static final String TAG = "ScaleGestureDetector";
    private static final float PRESSURE_THRESHOLD = 0.67F;
    private final com.app.Utils.ScaleGestureDetector.OnScaleGestureListener mListener;
    private boolean mGestureInProgress;
    private MotionEvent mPrevEvent;
    private MotionEvent mCurrEvent;
    private com.app.Utils.Vector2D mCurrSpanVector;
    private float mFocusX;
    private float mFocusY;
    private float mPrevFingerDiffX;
    private float mPrevFingerDiffY;
    private float mCurrFingerDiffX;
    private float mCurrFingerDiffY;
    private float mCurrLen;
    private float mPrevLen;
    private float mScaleFactor;
    private float mCurrPressure;
    private float mPrevPressure;
    private long mTimeDelta;
    private boolean mInvalidGesture;
    private int mActiveId0;
    private int mActiveId1;
    private boolean mActive0MostRecent;

    public ScaleGestureDetector(com.app.Utils.ScaleGestureDetector.OnScaleGestureListener listener) {
        this.mListener = listener;
        this.mCurrSpanVector = new com.app.Utils.Vector2D();
    }

    public boolean onTouchEvent(View view, MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 0) {
            this.reset();
        }

        boolean handled = true;
        if (this.mInvalidGesture) {
            handled = false;
        } else {
            int index1;
            int actionIndex;
            if (!this.mGestureInProgress) {
                switch(action) {
                    case 0:
                        this.mActiveId0 = event.getPointerId(0);
                        this.mActive0MostRecent = true;
                        break;
                    case 1:
                        this.reset();
                    case 2:
                    case 3:
                    case 4:
                    default:
                        break;
                    case 5:
                        if (this.mPrevEvent != null) {
                            this.mPrevEvent.recycle();
                        }

                        this.mPrevEvent = MotionEvent.obtain(event);
                        this.mTimeDelta = 0L;
                        index1 = event.getActionIndex();
                        actionIndex = event.findPointerIndex(this.mActiveId0);
                        this.mActiveId1 = event.getPointerId(index1);
                        if (actionIndex < 0 || actionIndex == index1) {
                            actionIndex = this.findNewActiveIndex(event, this.mActiveId1, -1);
                            this.mActiveId0 = event.getPointerId(actionIndex);
                        }

                        this.mActive0MostRecent = false;
                        this.setContext(view, event);
                        this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                }
            } else {
                int actionId;
                switch(action) {
                    case 1:
                        this.reset();
                        break;
                    case 2:
                        this.setContext(view, event);
                        if (this.mCurrPressure / this.mPrevPressure > 0.67F) {
                            boolean updatePrevious = this.mListener.onScale(view, this);
                            if (updatePrevious) {
                                this.mPrevEvent.recycle();
                                this.mPrevEvent = MotionEvent.obtain(event);
                            }
                        }
                        break;
                    case 3:
                        this.mListener.onScaleEnd(view, this);
                        this.reset();
                    case 4:
                    default:
                        break;
                    case 5:
                        this.mListener.onScaleEnd(view, this);
                        index1 = this.mActiveId0;
                        actionIndex = this.mActiveId1;
                        this.reset();
                        this.mPrevEvent = MotionEvent.obtain(event);
                        this.mActiveId0 = this.mActive0MostRecent ? index1 : actionIndex;
                        this.mActiveId1 = event.getPointerId(event.getActionIndex());
                        this.mActive0MostRecent = false;
                        actionId = event.findPointerIndex(this.mActiveId0);
                        if (actionId < 0 || this.mActiveId0 == this.mActiveId1) {
                            actionId = this.findNewActiveIndex(event, this.mActiveId1, -1);
                            this.mActiveId0 = event.getPointerId(actionId);
                        }

                        this.setContext(view, event);
                        this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                        break;
                    case 6:
                        index1 = event.getPointerCount();
                        actionIndex = event.getActionIndex();
                        actionId = event.getPointerId(actionIndex);
                        boolean gestureEnded = false;
                        int newIndex;
                        if (index1 > 2) {
                            if (actionId == this.mActiveId0) {
                                newIndex = this.findNewActiveIndex(event, this.mActiveId1, actionIndex);
                                if (newIndex >= 0) {
                                    this.mListener.onScaleEnd(view, this);
                                    this.mActiveId0 = event.getPointerId(newIndex);
                                    this.mActive0MostRecent = true;
                                    this.mPrevEvent = MotionEvent.obtain(event);
                                    this.setContext(view, event);
                                    this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                                } else {
                                    gestureEnded = true;
                                }
                            } else if (actionId == this.mActiveId1) {
                                newIndex = this.findNewActiveIndex(event, this.mActiveId0, actionIndex);
                                if (newIndex >= 0) {
                                    this.mListener.onScaleEnd(view, this);
                                    this.mActiveId1 = event.getPointerId(newIndex);
                                    this.mActive0MostRecent = false;
                                    this.mPrevEvent = MotionEvent.obtain(event);
                                    this.setContext(view, event);
                                    this.mGestureInProgress = this.mListener.onScaleBegin(view, this);
                                } else {
                                    gestureEnded = true;
                                }
                            }

                            this.mPrevEvent.recycle();
                            this.mPrevEvent = MotionEvent.obtain(event);
                            this.setContext(view, event);
                        } else {
                            gestureEnded = true;
                        }

                        if (gestureEnded) {
                            this.setContext(view, event);
                            newIndex = actionId == this.mActiveId0 ? this.mActiveId1 : this.mActiveId0;
                            int index = event.findPointerIndex(newIndex);
                            this.mFocusX = event.getX(index);
                            this.mFocusY = event.getY(index);
                            this.mListener.onScaleEnd(view, this);
                            this.reset();
                            this.mActiveId0 = newIndex;
                            this.mActive0MostRecent = true;
                        }
                }
            }
        }

        return handled;
    }

    private int findNewActiveIndex(MotionEvent ev, int otherActiveId, int removedPointerIndex) {
        int pointerCount = ev.getPointerCount();
        int otherActiveIndex = ev.findPointerIndex(otherActiveId);

        for(int i = 0; i < pointerCount; ++i) {
            if (i != removedPointerIndex && i != otherActiveIndex) {
                return i;
            }
        }

        return -1;
    }

    private void setContext(View view, MotionEvent curr) {
        if (this.mCurrEvent != null) {
            this.mCurrEvent.recycle();
        }

        this.mCurrEvent = MotionEvent.obtain(curr);
        this.mCurrLen = -1.0F;
        this.mPrevLen = -1.0F;
        this.mScaleFactor = -1.0F;
        this.mCurrSpanVector.set(0.0F, 0.0F);
        MotionEvent prev = this.mPrevEvent;
        int prevIndex0 = prev.findPointerIndex(this.mActiveId0);
        int prevIndex1 = prev.findPointerIndex(this.mActiveId1);
        int currIndex0 = curr.findPointerIndex(this.mActiveId0);
        int currIndex1 = curr.findPointerIndex(this.mActiveId1);
        if (prevIndex0 >= 0 && prevIndex1 >= 0 && currIndex0 >= 0 && currIndex1 >= 0) {
            float px0 = prev.getX(prevIndex0);
            float py0 = prev.getY(prevIndex0);
            float px1 = prev.getX(prevIndex1);
            float py1 = prev.getY(prevIndex1);
            float cx0 = curr.getX(currIndex0);
            float cy0 = curr.getY(currIndex0);
            float cx1 = curr.getX(currIndex1);
            float cy1 = curr.getY(currIndex1);
            float pvx = px1 - px0;
            float pvy = py1 - py0;
            float cvx = cx1 - cx0;
            float cvy = cy1 - cy0;
            this.mCurrSpanVector.set(cvx, cvy);
            this.mPrevFingerDiffX = pvx;
            this.mPrevFingerDiffY = pvy;
            this.mCurrFingerDiffX = cvx;
            this.mCurrFingerDiffY = cvy;
            this.mFocusX = cx0 + cvx * 0.5F;
            this.mFocusY = cy0 + cvy * 0.5F;
            this.mTimeDelta = curr.getEventTime() - prev.getEventTime();
            this.mCurrPressure = curr.getPressure(currIndex0) + curr.getPressure(currIndex1);
            this.mPrevPressure = prev.getPressure(prevIndex0) + prev.getPressure(prevIndex1);
        } else {
            this.mInvalidGesture = true;
            Log.e("ScaleGestureDetector", "Invalid MotionEvent stream detected.", new Throwable());
            if (this.mGestureInProgress) {
                this.mListener.onScaleEnd(view, this);
            }

        }
    }

    private void reset() {
        if (this.mPrevEvent != null) {
            this.mPrevEvent.recycle();
            this.mPrevEvent = null;
        }

        if (this.mCurrEvent != null) {
            this.mCurrEvent.recycle();
            this.mCurrEvent = null;
        }

        this.mGestureInProgress = false;
        this.mActiveId0 = -1;
        this.mActiveId1 = -1;
        this.mInvalidGesture = false;
    }

    public boolean isInProgress() {
        return this.mGestureInProgress;
    }

    public float getFocusX() {
        return this.mFocusX;
    }

    public float getFocusY() {
        return this.mFocusY;
    }

    public float getCurrentSpan() {
        if (this.mCurrLen == -1.0F) {
            float cvx = this.mCurrFingerDiffX;
            float cvy = this.mCurrFingerDiffY;
            this.mCurrLen = (float) Math.sqrt((double)(cvx * cvx + cvy * cvy));
        }

        return this.mCurrLen;
    }

    public com.app.Utils.Vector2D getCurrentSpanVector() {
        return this.mCurrSpanVector;
    }

    public float getCurrentSpanX() {
        return this.mCurrFingerDiffX;
    }

    public float getCurrentSpanY() {
        return this.mCurrFingerDiffY;
    }

    public float getPreviousSpan() {
        if (this.mPrevLen == -1.0F) {
            float pvx = this.mPrevFingerDiffX;
            float pvy = this.mPrevFingerDiffY;
            this.mPrevLen = (float) Math.sqrt((double)(pvx * pvx + pvy * pvy));
        }

        return this.mPrevLen;
    }

    public float getPreviousSpanX() {
        return this.mPrevFingerDiffX;
    }

    public float getPreviousSpanY() {
        return this.mPrevFingerDiffY;
    }

    public float getScaleFactor() {
        if (this.mScaleFactor == -1.0F) {
            this.mScaleFactor = this.getCurrentSpan() / this.getPreviousSpan();
        }

        return this.mScaleFactor;
    }

    public long getTimeDelta() {
        return this.mTimeDelta;
    }

    public long getEventTime() {
        return this.mCurrEvent.getEventTime();
    }

    public interface OnScaleGestureListener {
        boolean onScale(View var1, com.app.Utils.ScaleGestureDetector var2);

        boolean onScaleBegin(View var1, com.app.Utils.ScaleGestureDetector var2);

        void onScaleEnd(View var1, com.app.Utils.ScaleGestureDetector var2);
    }

    public static class SimpleOnScaleGestureListener implements com.app.Utils.ScaleGestureDetector.OnScaleGestureListener {
        public SimpleOnScaleGestureListener() {
        }

        public boolean onScale(View view, com.app.Utils.ScaleGestureDetector detector) {
            return false;
        }

        public boolean onScaleBegin(View view, com.app.Utils.ScaleGestureDetector detector) {
            return true;
        }

        public void onScaleEnd(View view, com.app.Utils.ScaleGestureDetector detector) {
        }
    }
}
