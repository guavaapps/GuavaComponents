package com.guavaapps.components.color

import java.util.*

internal object ColorUtils {
    private val WHITE_POINT_D65 = floatArrayOf(95.047f, 100.0f, 108.883f)
    fun whitePointD65(): FloatArray {
        return Arrays.copyOf(WHITE_POINT_D65, 3)
    }

    fun redFromInt(argb: Int): Int {
        return argb and 0x00ff0000 shr 16
    }

    fun greenFromInt(argb: Int): Int {
        return argb and 0x0000ff00 shr 8
    }

    fun blueFromInt(argb: Int): Int {
        return argb and 0x000000ff
    }

    fun lstarFromInt(argb: Int): Float {
        return labFromInt(argb)[0].toFloat()
    }

    fun hexFromInt(argb: Int): String {
        val red = redFromInt(argb)
        val blue = blueFromInt(argb)
        val green = greenFromInt(argb)
        return String.format("#%02x%02x%02x", red, green, blue)
    }

    fun xyzFromInt(argb: Int): FloatArray {
        val r = linearized(redFromInt(argb) / 255f) * 100f
        val g = linearized(greenFromInt(argb) / 255f) * 100f
        val b = linearized(blueFromInt(argb) / 255f) * 100f
        val x = 0.41233894f * r + 0.35762064f * g + 0.18051042f * b
        val y = 0.2126f * r + 0.7152f * g + 0.0722f * b
        val z = 0.01932141f * r + 0.11916382f * g + 0.95034478f * b
        return floatArrayOf(x, y, z)
    }

    fun intFromRgb(r: Int, g: Int, b: Int): Int {
        return 255 shl 24 or (r and 0x0ff shl 16) or (g and 0x0ff shl 8) or (b and 0x0ff) ushr 0
    }

    fun labFromInt(argb: Int): DoubleArray {
        val e = 216.0 / 24389.0
        val kappa = 24389.0 / 27.0
        val xyz = xyzFromInt(argb)
        val yNormalized = (xyz[1] / WHITE_POINT_D65[1]).toDouble()
        val fy: Double
        fy = if (yNormalized > e) {
            Math.cbrt(yNormalized)
        } else {
            (kappa * yNormalized + 16) / 116
        }
        val xNormalized = (xyz[0] / WHITE_POINT_D65[0]).toDouble()
        val fx: Double
        fx = if (xNormalized > e) {
            Math.cbrt(xNormalized)
        } else {
            (kappa * xNormalized + 16) / 116
        }
        val zNormalized = (xyz[2] / WHITE_POINT_D65[2]).toDouble()
        val fz: Double
        fz = if (zNormalized > e) {
            Math.cbrt(zNormalized)
        } else {
            (kappa * zNormalized + 16) / 116
        }
        val l = 116.0 * fy - 16
        val a = 500.0 * (fx - fy)
        val b = 200.0 * (fy - fz)
        return doubleArrayOf(l, a, b)
    }

    fun intFromLab(l: Double, a: Double, b: Double): Int {
        val e = 216.0 / 24389.0
        val kappa = 24389.0 / 27.0
        val ke = 8.0
        val fy = (l + 16.0) / 116.0
        val fx = a / 500.0 + fy
        val fz = fy - b / 200.0
        val fx3 = fx * fx * fx
        val xNormalized = if (fx3 > e) fx3 else (116.0 * fx - 16.0) / kappa
        val yNormalized = if (l > ke) fy * fy * fy else l / kappa
        val fz3 = fz * fz * fz
        val zNormalized = if (fz3 > e) fz3 else (116.0 * fz - 16.0) / kappa
        val x = xNormalized * WHITE_POINT_D65[0]
        val y = yNormalized * WHITE_POINT_D65[1]
        val z = zNormalized * WHITE_POINT_D65[2]
        return intFromXyzComponents(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun intFromXyzComponents(x: Float, y: Float, z: Float): Int {
        var x = x
        var y = y
        var z = z
        x = x / 100f
        y = y / 100f
        z = z / 100f
        val rL = x * 3.2406f + y * -1.5372f + z * -0.4986f
        val gL = x * -0.9689f + y * 1.8758f + z * 0.0415f
        val bL = x * 0.0557f + y * -0.204f + z * 1.057f
        val r = delinearized(rL)
        val g = delinearized(gL)
        val b = delinearized(bL)
        val rInt = Math.max(Math.min(255, Math.round(r * 255)), 0)
        val gInt = Math.max(Math.min(255, Math.round(g * 255)), 0)
        val bInt = Math.max(Math.min(255, Math.round(b * 255)), 0)
        return intFromRgb(rInt, gInt, bInt)
    }

    fun intFromXyz(xyz: FloatArray): Int {
        return intFromXyzComponents(xyz[0], xyz[1], xyz[2])
    }

    fun intFromLstar(lstar: Float): Int {
        val fy = (lstar + 16.0f) / 116.0f
        val kappa = 24389f / 27f
        val epsilon = 216f / 24389f
        val cubeExceedEpsilon = fy * fy * fy > epsilon
        val lExceedsEpsilonKappa = lstar > 8.0f
        val y = if (lExceedsEpsilonKappa) fy * fy * fy else lstar / kappa
        val x = if (cubeExceedEpsilon) fy * fy * fy else (116f * fy - 16f) / kappa
        val z = if (cubeExceedEpsilon) fy * fy * fy else (116f * fy - 16f) / kappa
        val xyz = floatArrayOf(
            x * WHITE_POINT_D65[0],
            y * WHITE_POINT_D65[1],
            z * WHITE_POINT_D65[2])
        return intFromXyz(xyz)
    }

    fun yFromLstar(lstar: Float): Float {
        val ke = 8.0f
        return if (lstar > ke) {
            Math.pow((lstar + 16.0) / 116.0, 3.0).toFloat() * 100f
        } else {
            lstar / (24389f / 27f) * 100f
        }
    }

    fun linearized(rgb: Float): Float {
        return if (rgb <= 0.04045f) {
            rgb / 12.92f
        } else {
            Math.pow(((rgb + 0.055f) / 1.055f).toDouble(), 2.4).toFloat()
        }
    }

    fun delinearized(rgb: Float): Float {
        return if (rgb <= 0.0031308f) {
            rgb * 12.92f
        } else {
            1.055f * Math.pow(rgb.toDouble(), (1.0f / 2.4f).toDouble()).toFloat() - 0.055f
        }
    }
}