package com.wallora.app.domain.model

import android.graphics.Color

/**
 * Enhanced Wallpaper model with additional metadata for filtering, analytics, and recommendations.
 * This extends the existing model with optional fields for new features.
 */
data class WallpaperEnhanced(
    // Existing fields
    val id: String,
    val sourceId: String,
    val thumbUrl: String,
    val fullUrl: String,
    val width: Int,
    val height: Int,
    val author: String,
    val authorUrl: String,
    val sourcePageUrl: String,
    val colorHint: Int? = null,
    
    // New analytics fields
    val viewCount: Int = 0,
    val favoriteCount: Int = 0,
    val shareCount: Int = 0,
    val dwellTime: Long = 0L,  // ms user kept this wallpaper
    val addedAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long? = null,
    
    // New metadata fields
    val tags: List<String> = emptyList(),  // auto-generated tags
    val collection: String? = null,  // collection ID
    val isNsfw: Boolean = false,
    val quality: Quality = Quality.MEDIUM,
    val aspectRatio: String = "unknown",  // "16:9", "9:16", etc.
    val dominantHue: Int? = null,  // dominant color hue (0-360)
    val isBlurry: Boolean? = null,  // ML-detected blur
    val hasText: Boolean? = null,  // contains text overlay
    val hasLandmark: Boolean? = null,  // famous landmark detection
    val sentiment: Sentiment = Sentiment.NEUTRAL,  // emotional tone
    
    // Recommendation fields
    val userRating: Float? = null,  // user's personal rating (1-5)
    val doNotRecommend: Boolean = false,
    val recommendationScore: Float = 0f,  // internal ML score
    val similarWallpaperIds: List<String> = emptyList(),
    
    // Sync fields
    val isSynced: Boolean = false,
    val lastSyncedAt: Long? = null,
    val isDraft: Boolean = false,
    val syncConflict: Boolean = false,
) {
    enum class Quality {
        LOW, MEDIUM, HIGH, ULTRA_HD
    }
    
    enum class Sentiment {
        CALM, NEUTRAL, ENERGETIC, MOODY, INSPIRING
    }
    
    val globalKey: String
        get() = "$sourceId:$id"
}
