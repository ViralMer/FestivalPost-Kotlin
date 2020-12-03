package com.app.festivalpost.activity

import android.view.animation.Interpolator

class MyBounceInterpolatorNew(amplitude: Double, frequency: Double) : Interpolator {
    private var mAmplitude = 1.0
    private var mFrequency = 10.0
    override fun getInterpolation(time: Float): Float {
        return (1 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 0.7).toFloat()
    }

    init {
        mAmplitude = amplitude
        mFrequency = frequency
    }
}