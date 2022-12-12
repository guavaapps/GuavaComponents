package com.guavaapps.components.color

internal object MathUtils {
    fun clamp(min: Float, max: Float, input: Float): Float {
        return Math.min(Math.max(input, min), max)
    }

    fun lerp(start: Float, stop: Float, amount: Float): Float {
        return (1.0f - amount) * start + amount * stop
    }

    fun differenceDegrees(a: Float, b: Float): Float {
        return 180f - Math.abs(Math.abs(a - b) - 180f)
    }

    fun sanitizeDegrees(degrees: Float): Float {
        return if (degrees < 0f) {
            degrees % 360.0f + 360f
        } else if (degrees >= 360.0f) {
            degrees % 360.0f
        } else {
            degrees
        }
    }

    fun sanitizeDegrees(degrees: Int): Int {
        return if (degrees < 0) {
            degrees % 360 + 360
        } else if (degrees >= 360) {
            degrees % 360
        } else {
            degrees
        }
    }

    fun toDegrees(radians: Float): Float {
        return radians * 180.0f / Math.PI.toFloat()
    }

    fun toRadians(degrees: Float): Float {
        return degrees / 180.0f * Math.PI.toFloat()
    }
}