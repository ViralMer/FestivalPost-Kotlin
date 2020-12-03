package com.app.festivalpost.utility;

import android.graphics.PointF;

public class Vector2D extends PointF {
    public Vector2D() {
    }

    public Vector2D(float x, float y) {
        super(x, y);
    }

    public static float getAngle(com.app.Utils.Vector2D vector1, com.app.Utils.Vector2D vector2) {
        vector1.normalize();
        vector2.normalize();
        double degrees = 57.29577951308232D * (Math.atan2((double)vector2.y, (double)vector2.x) - Math.atan2((double)vector1.y, (double)vector1.x));
        return (float)degrees;
    }

    public void normalize() {
        float length = (float) Math.sqrt((double)(this.x * this.x + this.y * this.y));
        this.x /= length;
        this.y /= length;
    }
}
