package com.guavaapps.components.color

import androidx.annotation.FloatRange

class Hct {
    private var h: Float = 0f
    private var c: Float = 0f
    private var t: Float = 0f

    var hue: Float
        get() = h
        set(newHue) = setInternalState(gamutMap(MathUtils.sanitizeDegrees(newHue), c, t))

    var chroma: Float
        get() = c
        set(newChroma) = setInternalState(gamutMap(h, newChroma, t))

    var tone: Float
        get() = t
        set(newTone) = setInternalState(gamutMap(h, c, newTone))

    fun toInt(): Int {
        return gamutMap(h, c, t)
    }

    private fun setInternalState(argb: Int) {
        val cam = Cam16.fromInt(argb)
        val tone = ColorUtils.lstarFromInt(argb)
        h = cam.hue
        c = cam.chroma
        t = tone
    }

    companion object {
        fun fromInt(argb: Int): Hct {
            val cam = Cam16.fromInt(argb)
            return Hct ().apply {
                h = cam.hue
                c = cam.chroma
                t = ColorUtils.lstarFromInt(argb)
            }
        }

        private const val CHROMA_SEARCH_ENDPOINT = 0.4f
        private const val DE_MAX = 1.0f
        private const val DL_MAX = 0.2f
        private const val DE_MAX_ERROR = 0.000000001f
        private const val LIGHTNESS_SEARCH_ENDPOINT = 0.01f
        private fun gamutMap(hue: Float, chroma: Float, tone: Float): Int {
            return gamutMapInViewingConditions(hue, chroma, tone, ViewingConditions.DEFAULT)
        }

        private fun gamutMapInViewingConditions(
            hue: Float, chroma: Float, tone: Float, viewingConditions: ViewingConditions,
        ): Int {
            var hue = hue
            if (chroma < 1.0 || Math.round(tone) <= 0.0 || Math.round(tone) >= 100.0) {
                return ColorUtils.intFromLstar(tone)
            }
            hue = MathUtils.sanitizeDegrees(hue)
            var high = chroma
            var mid = chroma
            var low = 0.0f
            var isFirstLoop = true
            var answer: Cam16? = null
            while (Math.abs(low - high) >= CHROMA_SEARCH_ENDPOINT) {
                val possibleAnswer = findCamByJ(hue, mid, tone)
                if (isFirstLoop) {
                    return if (possibleAnswer != null) {
                        possibleAnswer.viewed(viewingConditions)
                    } else {
                        isFirstLoop = false
                        mid = low + (high - low) / 2.0f
                        continue
                    }
                }
                if (possibleAnswer == null) {
                    high = mid
                } else {
                    answer = possibleAnswer
                    low = mid
                }
                mid = low + (high - low) / 2.0f
            }
            return answer?.viewed(viewingConditions) ?: ColorUtils.intFromLstar(tone)
        }

        private fun findCamByJ(hue: Float, chroma: Float, tone: Float): Cam16? {
            var low = 0.0f
            var high = 100.0f
            var mid = 0.0f
            var bestdL = 1000.0f
            var bestdE = 1000.0f
            var bestCam: Cam16? = null
            while (Math.abs(low - high) > LIGHTNESS_SEARCH_ENDPOINT) {
                mid = low + (high - low) / 2
                val camBeforeClip = Cam16.fromJch(mid, chroma, hue)
                val clipped = camBeforeClip.int
                val clippedLstar = ColorUtils.lstarFromInt(clipped)
                val dL = Math.abs(tone - clippedLstar)
                if (dL < DL_MAX) {
                    val camClipped = Cam16.fromInt(clipped)
                    val dE =
                        camClipped.distance(Cam16.fromJch(camClipped.j, camClipped.chroma, hue))
                    if (dE <= DE_MAX && dE <= bestdE) {
                        bestdL = dL
                        bestdE = dE
                        bestCam = camClipped
                    }
                }
                if (bestdL == 0f && bestdE < DE_MAX_ERROR) {
                    break
                }
                if (clippedLstar < tone) {
                    low = mid
                } else {
                    high = mid
                }
            }
            return bestCam
        }
    }
}