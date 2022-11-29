package com.guavaapps.components.color

class Argb {
    var alpha: Float = 0.0f
    var red: Float = 0.0f
    var green: Float = 0.0f
    var blue: Float = 0.0f

    fun toInt(): Int {
        return (alpha.toInt() shl 24) or (red.toInt() shl 16) or (green.toInt() shl 8) or blue.toInt()
    }

    companion object {
        fun from(alpha: Float, red: Float, green: Float, blue: Float): Argb {
            return Argb().apply {
                this.alpha = alpha
                this.red = red
                this.green = green
                this.blue = blue
            }
        }

        fun from(argb: Int): Argb {
            return Argb().apply {
                alpha = ((argb shr 24) and 0xff).toFloat()
                red = ((argb shr 16) and 0xff).toFloat()
                green = ((argb shr 8) and 0xff).toFloat()
                blue = (argb and 0xff).toFloat()
            }
        }
    }
}