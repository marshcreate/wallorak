package com.wallora.app.data.paging

import com.wallora.app.domain.model.Wallpaper
import kotlin.math.abs

/** Reorders a page so visually and topically similar wallpapers are not adjacent. */
object FeedDiversifier {
    const val DEFAULT_WINDOW = 3

    private const val CATEGORY_PENALTY = 1.0f
    private const val SOURCE_PENALTY = 0.35f
    private const val HUE_WEIGHT = 1.0f
    private const val MAX_SIMILARITY = CATEGORY_PENALTY + SOURCE_PENALTY + HUE_WEIGHT

    /** Convert an (A)RGB int to HSV: hue 0..360, saturation 0..1, value 0..1. */
    fun argbToHsv(argb: Int): FloatArray {
        val r = ((argb shr 16) and 0xFF) / 255f
        val g = ((argb shr 8) and 0xFF) / 255f
        val b = (argb and 0xFF) / 255f
        val max = maxOf(r, g, b)
        val min = minOf(r, g, b)
        val delta = max - min
        var h = when {
            delta == 0f -> 0f
            max == r -> 60f * (((g - b) / delta) % 6f)
            max == g -> 60f * (((b - r) / delta) + 2f)
            else -> 60f * (((r - g) / delta) + 4f)
        }
        if (h < 0f) h += 360f
        val s = if (max == 0f) 0f else delta / max
        return floatArrayOf(h, s, max)
    }

    fun hueDistance(a: Float, b: Float): Float {
        val d = abs(a - b) % 360f
        return if (d > 180f) 360f - d else d
    }

    private data class VisualInfo(val hue: Float?, val colorfulness: Float)

    private fun visualInfo(wallpaper: Wallpaper): VisualInfo {
        val color = wallpaper.colorHint ?: return VisualInfo(null, 0f)
        val hsv = argbToHsv(color)
        return VisualInfo(hsv[0], hsv[1] * hsv[2])
    }

    private fun similarity(a: Wallpaper, b: Wallpaper, aHue: Float?, bHue: Float?): Float {
        var score = 0f
        if (a.category != null && a.category == b.category) score += CATEGORY_PENALTY
        if (a.sourceId == b.sourceId) score += SOURCE_PENALTY
        if (aHue != null && bHue != null) {
            score += HUE_WEIGHT * (1f - hueDistance(aHue, bHue) / 180f)
        }
        return score
    }

    /** Returns the maximum similarity against the recent window, stopping at the maximum score. */
    private fun badness(
        candidate: Wallpaper,
        candidateHue: Float?,
        window: ArrayDeque<Wallpaper>,
        hueCache: Map<Wallpaper, Float?>,
    ): Float {
        var worst = 0f
        for (placed in window) {
            worst = maxOf(worst, similarity(candidate, placed, candidateHue, hueCache[placed]))
            if (worst >= MAX_SIMILARITY) return worst
        }
        return worst
    }

    /**
     * Greedily chooses the least similar remaining item. The algorithm remains O(n²), but hue and
     * colorfulness are computed once per item instead of once per candidate/window comparison.
     */
    fun diverse(
        items: List<Wallpaper>,
        windowK: Int = DEFAULT_WINDOW,
        tail: List<Wallpaper> = emptyList(),
    ): List<Wallpaper> {
        if (items.size <= 1 || windowK <= 0) return items

        val info = items.associateWith(::visualInfo)
        val remaining = ArrayList(items)
        val result = ArrayList<Wallpaper>(items.size)
        val window = ArrayDeque<Wallpaper>()
        tail.takeLast(windowK).forEach(window::addLast)

        // Tail items are not necessarily in items, so compute their hue once as well.
        val hueCache = HashMap<Wallpaper, Float?>(items.size + tail.size)
        info.forEach { (wallpaper, visual) -> hueCache[wallpaper] = visual.hue }
        tail.forEach { wallpaper -> hueCache.putIfAbsent(wallpaper, visualInfo(wallpaper).hue) }

        while (remaining.isNotEmpty()) {
            var bestIndex = 0
            var bestBadness = Float.MAX_VALUE
            var bestColorfulness = -1f

            for (index in remaining.indices) {
                val candidate = remaining[index]
                val candidateInfo = info.getValue(candidate)
                val candidateBadness = badness(candidate, candidateInfo.hue, window, hueCache)
                if (candidateBadness < bestBadness ||
                    (candidateBadness == bestBadness && candidateInfo.colorfulness > bestColorfulness)
                ) {
                    bestIndex = index
                    bestBadness = candidateBadness
                    bestColorfulness = candidateInfo.colorfulness
                }
            }

            val picked = remaining.removeAt(bestIndex)
            result += picked
            window.addLast(picked)
            if (window.size > windowK) window.removeFirst()
        }
        return result
    }
}
