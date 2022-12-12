package com.guavaapps.components.color

import com.guavaapps.components.color.ColorUtils.whitePointD65
import com.guavaapps.components.color.ColorUtils.yFromLstar
import com.guavaapps.components.color.MathUtils.lerp

internal class ViewingConditions private constructor(
    val n: Float,
    val aw: Float,
    val nbb: Float,
    val ncb: Float,
    val c: Float,
    val nc: Float,
    val rgbD: FloatArray,
    val fl: Float,
    val flRoot: Float,
    val z: Float
) {

    companion object {
        val DEFAULT = make(
            whitePointD65(), (200.0f / Math.PI * yFromLstar(50.0f) / 100f).toFloat(),
            50.0f,
            2.0f,
            false)

        fun make(
            whitePoint: FloatArray,
            adaptingLuminance: Float,
            backgroundLstar: Float,
            surround: Float,
            discountingIlluminant: Boolean
        ): ViewingConditions {
            val matrix = Cam16.XYZ_TO_CAM16RGB
            val rW =
                whitePoint[0] * matrix[0][0] + whitePoint[1] * matrix[0][1] + whitePoint[2] * matrix[0][2]
            val gW =
                whitePoint[0] * matrix[1][0] + whitePoint[1] * matrix[1][1] + whitePoint[2] * matrix[1][2]
            val bW =
                whitePoint[0] * matrix[2][0] + whitePoint[1] * matrix[2][1] + whitePoint[2] * matrix[2][2]
            val f = 0.8f + surround / 10.0f
            val c = if (f >= 0.9) lerp(0.59f, 0.69f, (f - 0.9f) * 10.0f) else lerp(0.525f,
                0.59f,
                (f - 0.8f) * 10.0f)
            var d =
                if (discountingIlluminant) 1.0f else f * (1.0f - 1.0f / 3.6f * Math.exp(((-adaptingLuminance - 42.0f) / 92.0f).toDouble())
                    .toFloat())
            d = if (d > 1.0) 1.0f else if (d < 0.0) 0.0f else d
            val rgbD = floatArrayOf(
                d * (100.0f / rW) + 1.0f - d,
                d * (100.0f / gW) + 1.0f - d,
                d * (100.0f / bW) + 1.0f - d
            )
            val k = 1.0f / (5.0f * adaptingLuminance + 1.0f)
            val k4 = k * k * k * k
            val k4F = 1.0f - k4
            val fl = k4 * adaptingLuminance + 0.1f * k4F * k4F * Math.cbrt(5.0 * adaptingLuminance)
                .toFloat()
            val n = yFromLstar(backgroundLstar) / whitePoint[1]
            val z = 1.48f + Math.sqrt(n.toDouble()).toFloat()
            val nbb = 0.725f / Math.pow(n.toDouble(), 0.2).toFloat()
            val rgbAFactors = floatArrayOf(Math.pow(fl * rgbD[0] * rW / 100.0, 0.42).toFloat(),
                Math.pow(fl * rgbD[1] * gW / 100.0, 0.42).toFloat(),
                Math.pow(fl * rgbD[2] * bW / 100.0, 0.42).toFloat())
            val rgbA = floatArrayOf(
                400.0f * rgbAFactors[0] / (rgbAFactors[0] + 27.13f),
                400.0f * rgbAFactors[1] / (rgbAFactors[1] + 27.13f),
                400.0f * rgbAFactors[2] / (rgbAFactors[2] + 27.13f)
            )
            val aw = (2.0f * rgbA[0] + rgbA[1] + 0.05f * rgbA[2]) * nbb
            return ViewingConditions(n,
                aw,
                nbb,
                nbb,
                c,
                f,
                rgbD,
                fl,
                Math.pow(fl.toDouble(), 0.25).toFloat(),
                z)
        }
    }
}