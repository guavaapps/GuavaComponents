package com.guavaapps.components.color

import com.guavaapps.components.color.ColorUtils.linearized
import com.guavaapps.components.color.ColorUtils.intFromXyzComponents

internal class Cam16 private constructor(
    val hue: Float,
    val chroma: Float,
    val j: Float,
    val q: Float,
    val m: Float,
    val s: Float,
    val jStar: Float,
    val aStar: Float,
    val bStar: Float
) {
    fun distance(other: Cam16): Float {
        val dJ = jStar - other.jStar
        val dA = aStar - other.aStar
        val dB = bStar - other.bStar
        val dEPrime = Math.sqrt((dJ * dJ + dA * dA + dB * dB).toDouble())
        val dE = 1.41 * Math.pow(dEPrime, 0.63)
        return dE.toFloat()
    }

    val int: Int
        get() = viewed(ViewingConditions.DEFAULT)

    fun viewed(viewingConditions: ViewingConditions): Int {
        val alpha =
            if (chroma.toDouble() == 0.0 || j.toDouble() == 0.0) 0.0f else chroma / Math.sqrt(
                j / 100.0).toFloat()
        val t = Math.pow(
            alpha / Math.pow(1.64 - Math.pow(0.29, viewingConditions.n.toDouble()), 0.73),
            1.0 / 0.9).toFloat()
        val hRad = hue * Math.PI.toFloat() / 180.0f
        val eHue = 0.25f * (Math.cos(hRad + 2.0) + 3.8).toFloat()
        val ac: Float = (viewingConditions.aw
                * Math.pow(j / 100.0, 1.0 / viewingConditions.c / viewingConditions.z)
            .toFloat())
        val p1: Float =
            eHue * (50000.0f / 13.0f) * viewingConditions.nc * viewingConditions.ncb
        val p2: Float = ac / viewingConditions.nbb
        val hSin = Math.sin(hRad.toDouble()).toFloat()
        val hCos = Math.cos(hRad.toDouble()).toFloat()
        val gamma = 23.0f * (p2 + 0.305f) * t / (23.0f * p1 + 11.0f * t * hCos + 108.0f * t * hSin)
        val a = gamma * hCos
        val b = gamma * hSin
        val rA = (460.0f * p2 + 451.0f * a + 288.0f * b) / 1403.0f
        val gA = (460.0f * p2 - 891.0f * a - 261.0f * b) / 1403.0f
        val bA = (460.0f * p2 - 220.0f * a - 6300.0f * b) / 1403.0f
        val rCBase = Math.max(0.0, 27.13 * Math.abs(rA) / (400.0 - Math.abs(rA))).toFloat()
        val rC: Float = (Math.signum(rA)
                * (100.0f / viewingConditions.fl)
                * Math.pow(rCBase.toDouble(), 1.0 / 0.42).toFloat())
        val gCBase = Math.max(0.0, 27.13 * Math.abs(gA) / (400.0 - Math.abs(gA))).toFloat()
        val gC: Float = (Math.signum(gA)
                * (100.0f / viewingConditions.fl)
                * Math.pow(gCBase.toDouble(), 1.0 / 0.42).toFloat())
        val bCBase = Math.max(0.0, 27.13 * Math.abs(bA) / (400.0 - Math.abs(bA))).toFloat()
        val bC: Float = (Math.signum(bA)
                * (100.0f / viewingConditions.fl)
                * Math.pow(bCBase.toDouble(), 1.0 / 0.42).toFloat())
        val rF: Float = rC / viewingConditions.rgbD.get(0)
        val gF: Float = gC / viewingConditions.rgbD.get(1)
        val bF: Float = bC / viewingConditions.rgbD.get(2)
        val matrix = CAM16RGB_TO_XYZ
        val x = rF * matrix[0][0] + gF * matrix[0][1] + bF * matrix[0][2]
        val y = rF * matrix[1][0] + gF * matrix[1][1] + bF * matrix[1][2]
        val z = rF * matrix[2][0] + gF * matrix[2][1] + bF * matrix[2][2]
        return intFromXyzComponents(x, y, z)
    }

    companion object {
        val XYZ_TO_CAM16RGB = arrayOf(floatArrayOf(0.401288f, 0.650173f, -0.051461f),
            floatArrayOf(-0.250268f, 1.204414f, 0.045854f),
            floatArrayOf(-0.002079f, 0.048952f, 0.953127f))
        val CAM16RGB_TO_XYZ = arrayOf(floatArrayOf(1.8620678f, -1.0112547f, 0.14918678f),
            floatArrayOf(0.38752654f, 0.62144744f, -0.00897398f),
            floatArrayOf(-0.01584150f, -0.03412294f, 1.0499644f))

        fun fromInt(argb: Int): Cam16 {
            return fromIntInViewingConditions(argb, ViewingConditions.DEFAULT)
        }

        fun fromIntInViewingConditions(argb: Int, viewingConditions: ViewingConditions): Cam16 {
            val red = argb and 0x00ff0000 shr 16
            val green = argb and 0x0000ff00 shr 8
            val blue = argb and 0x000000ff
            val redL = linearized(red / 255f) * 100f
            val greenL = linearized(green / 255f) * 100f
            val blueL = linearized(blue / 255f) * 100f
            val x = 0.41233895f * redL + 0.35762064f * greenL + 0.18051042f * blueL
            val y = 0.2126f * redL + 0.7152f * greenL + 0.0722f * blueL
            val z = 0.01932141f * redL + 0.11916382f * greenL + 0.95034478f * blueL
            val matrix = XYZ_TO_CAM16RGB
            val rT = x * matrix[0][0] + y * matrix[0][1] + z * matrix[0][2]
            val gT = x * matrix[1][0] + y * matrix[1][1] + z * matrix[1][2]
            val bT = x * matrix[2][0] + y * matrix[2][1] + z * matrix[2][2]
            val rD: Float = viewingConditions.rgbD.get(0) * rT
            val gD: Float = viewingConditions.rgbD.get(1) * gT
            val bD: Float = viewingConditions.rgbD.get(2) * bT
            val rAF = Math.pow(viewingConditions.fl * Math.abs(rD) / 100.0, 0.42).toFloat()
            val gAF = Math.pow(viewingConditions.fl * Math.abs(gD) / 100.0, 0.42).toFloat()
            val bAF = Math.pow(viewingConditions.fl * Math.abs(bD) / 100.0, 0.42).toFloat()
            val rA = Math.signum(rD) * 400.0f * rAF / (rAF + 27.13f)
            val gA = Math.signum(gD) * 400.0f * gAF / (gAF + 27.13f)
            val bA = Math.signum(bD) * 400.0f * bAF / (bAF + 27.13f)
            val a = (11.0 * rA + -12.0 * gA + bA).toFloat() / 11.0f
            val b = (rA + gA - 2.0 * bA).toFloat() / 9.0f
            val u = (20.0f * rA + 20.0f * gA + 21.0f * bA) / 20.0f
            val p2 = (40.0f * rA + 20.0f * gA + bA) / 20.0f
            val atan2 = Math.atan2(b.toDouble(), a.toDouble()).toFloat()
            val atanDegrees = atan2 * 180.0f / Math.PI.toFloat()
            val hue =
                if (atanDegrees < 0) atanDegrees + 360.0f else if (atanDegrees >= 360) atanDegrees - 360.0f else atanDegrees
            val hueRadians = hue * Math.PI.toFloat() / 180.0f
            val ac: Float = p2 * viewingConditions.nbb
            val j = (100.0f
                    * Math.pow((
                    ac / viewingConditions.aw).toDouble(), (
                    viewingConditions.c * viewingConditions.z).toDouble()).toFloat())
            val q: Float = ((4.0f
                    / viewingConditions.c) * Math.sqrt((j / 100.0f).toDouble())
                .toFloat() * (viewingConditions.aw + 4.0f)
                    * viewingConditions.flRoot)
            val huePrime = if (hue < 20.14) hue + 360 else hue
            val eHue = 0.25f * (Math.cos(Math.toRadians(huePrime.toDouble()) + 2.0) + 3.8).toFloat()
            val p1: Float =
                50000.0f / 13.0f * eHue * viewingConditions.nc * viewingConditions.ncb
            val t = p1 * Math.hypot(a.toDouble(), b.toDouble()).toFloat() / (u + 0.305f)
            val alpha = Math.pow(1.64 - Math.pow(0.29, viewingConditions.n.toDouble()), 0.73)
                .toFloat() * Math.pow(t.toDouble(), 0.9).toFloat()
            val c = alpha * Math.sqrt(j / 100.0).toFloat()
            val m: Float = c * viewingConditions.flRoot
            val s = (50.0f
                    * Math.sqrt((alpha * viewingConditions.c / (viewingConditions.aw + 4.0f)).toDouble())
                .toFloat())
            val jstar = (1.0f + 100.0f * 0.007f) * j / (1.0f + 0.007f * j)
            val mstar = 1.0f / 0.0228f * Math.log1p((0.0228f * m).toDouble()).toFloat()
            val astar = mstar * Math.cos(hueRadians.toDouble()).toFloat()
            val bstar = mstar * Math.sin(hueRadians.toDouble()).toFloat()
            return Cam16(hue, c, j, q, m, s, jstar, astar, bstar)
        }

        fun fromJch(j: Float, c: Float, h: Float): Cam16 {
            return fromJchInViewingConditions(j, c, h, ViewingConditions.DEFAULT)
        }

        private fun fromJchInViewingConditions(
            j: Float, c: Float, h: Float, viewingConditions: ViewingConditions
        ): Cam16 {
            val q: Float = ((4.0f
                    / viewingConditions.c) * Math.sqrt(j / 100.0)
                .toFloat() * (viewingConditions.aw + 4.0f)
                    * viewingConditions.flRoot)
            val m: Float = c * viewingConditions.flRoot
            val alpha = c / Math.sqrt(j / 100.0).toFloat()
            val s = (50.0f
                    * Math.sqrt((alpha * viewingConditions.c / (viewingConditions.aw + 4.0f)).toDouble())
                .toFloat())
            val hueRadians = h * Math.PI.toFloat() / 180.0f
            val jstar = (1.0f + 100.0f * 0.007f) * j / (1.0f + 0.007f * j)
            val mstar = 1.0f / 0.0228f * Math.log1p(0.0228 * m).toFloat()
            val astar = mstar * Math.cos(hueRadians.toDouble()).toFloat()
            val bstar = mstar * Math.sin(hueRadians.toDouble()).toFloat()
            return Cam16(h, c, j, q, m, s, jstar, astar, bstar)
        }

        fun fromUcs(jstar: Float, astar: Float, bstar: Float): Cam16 {
            return fromUcsInViewingConditions(jstar, astar, bstar, ViewingConditions.DEFAULT)
        }

        fun fromUcsInViewingConditions(
            jstar: Float, astar: Float, bstar: Float, viewingConditions: ViewingConditions
        ): Cam16 {
            val m = Math.hypot(astar.toDouble(), bstar.toDouble())
            val m2 = Math.expm1(m * 0.0228f) / 0.0228f
            val c: Double = m2 / viewingConditions.flRoot
            var h = Math.atan2(bstar.toDouble(), astar.toDouble()) * (180.0f / Math.PI)
            if (h < 0.0) {
                h += 360.0
            }
            val j = jstar / (1f - (jstar - 100f) * 0.007f)
            return fromJchInViewingConditions(j, c.toFloat(), h.toFloat(), viewingConditions)
        }
    }
}