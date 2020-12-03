package com.app.festivalpost.activity

import android.view.animation.Interpolator

class MyBounceInterpolator(amplitude: Double, frequency: Double) : Interpolator {
    private var mAmplitude = 1.0
    private var mFrequency = 10.0
    override fun getInterpolation(time: Float): Float {
        return (-0.5 * Math.pow(Math.E, -time / mAmplitude) *
                Math.cos(mFrequency * time) + 1).toFloat()
    }

    init {
        mAmplitude = amplitude
        mFrequency = frequency
    }
}