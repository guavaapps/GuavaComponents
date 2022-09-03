package com.guavaapps.components.color

class Argb private constructor(
    var alpha: Float,
    var red: Float,
    var green: Float,
    var blue: Float
) {
    fun toInt(): Int {
        return alpha.toInt() shl 24 or (red.toInt() shl 16
                ) or (green.toInt() shl 8
                ) or blue.toInt()
    }

    fun alpha(): Float {
        return alpha
    }

    fun red(): Float {
        return red
    }

    fun green(): Float {
        return green
    }

    fun blue(): Float {
        return blue
    }

    companion object {
        fun from(alpha: Float, red: Float, green: Float, blue: Float): Argb {
            return Argb(alpha, red, green, blue)
        }

        fun from(argb: Int): Argb {
            val alpha = (argb shr 24 and 0xff).toFloat()
            val red = (argb shr 16 and 0xff).toFloat()
            val green = (argb shr 8 and 0xff).toFloat()
            val blue = (argb and 0xff).toFloat()
            return Argb(alpha, red, green, blue)
        }
    }
}