package com.guavaapps.components.color;

import static java.lang.Math.max;
import static java.lang.Math.min;

class MathUtils {
    private MathUtils () {
    }

    static float clamp (float min, float max, float input) {
        return min (max (input, min), max);
    }


    public static float lerp (float start, float stop, float amount) {
        return (1.0f - amount) * start + amount * stop;
    }


    public static float differenceDegrees (float a, float b) {
        return 180f - Math.abs (Math.abs (a - b) - 180f);
    }


    public static float sanitizeDegrees (float degrees) {
        if (degrees < 0f) {
            return (degrees % 360.0f) + 360.f;
        } else if (degrees >= 360.0f) {
            return degrees % 360.0f;
        } else {
            return degrees;
        }
    }


    public static int sanitizeDegrees (int degrees) {
        if (degrees < 0) {
            return (degrees % 360) + 360;
        } else if (degrees >= 360) {
            return degrees % 360;
        } else {
            return degrees;
        }
    }


    static float toDegrees (float radians) {
        return radians * 180.0f / (float) Math.PI;
    }


    static float toRadians (float degrees) {
        return degrees / 180.0f * (float) Math.PI;
    }
}
