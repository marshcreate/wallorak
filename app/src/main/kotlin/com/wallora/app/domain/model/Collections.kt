package com.wallora.app.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Collections allow users to organize wallpapers by theme, mood, or custom criteria.
 * Collections can be standard (manually added) or smart (rule-based).
 */
@Entity(tableName = "collections")
data class Collection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val coverImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val itemCount: Int = 0,
    val isSmartCollection: Boolean = false,
    val smartRules: String = "",  // JSON-encoded rules if smart
    val isPublic: Boolean = false,
    val shareToken: String? = null,
    val tags: String = "",  // comma-separated tags
    val isFavorite: Boolean = false,
)

/**
 * Playlists define rotation behavior: which wallpapers to rotate through and how.
 */
@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val collectionId: Long? = null,  // if null, rotate from all favorites
    val rotationMode: RotationMode = RotationMode.RANDOM,
    val rotationInterval: Long = 3_600_000L,  // default 1 hour in ms
    val isActive: Boolean = false,
    val startTime: String? = null,  // HH:MM format
    val endTime: String? = null,
    val daysOfWeek: String = "1234567",  // "1234567" = every day
    val isWeatherDependent: Boolean = false,
    val weatherConditions: String = "",  // sunny, cloudy, rainy, etc.
    val isLocationDependent: Boolean = false,
    val locationBounds: String = "",  // JSON GeoJSON polygon
    val isSpotifyIntegrated: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
) {
    enum class RotationMode {
        RANDOM, SEQUENTIAL, LEAST_RECENTLY_USED, WEIGHTED, ALGORITHM_BASED
    }
}

/**
 * Filters allow users to narrow wallpapers by criteria.
 */
data class WallpaperFilter(
    val colors: List<String> = emptyList(),  // red, blue, green, etc.
    val colorSaturation: Pair<Float, Float>? = null,  // min, max (0..1)
    val brightness: Pair<Float, Float>? = null,  // min, max (0..1)
    val aspectRatios: List<String> = emptyList(),  // "16:9", "9:16", etc.
    val resolutionMin: Pair<Int, Int>? = null,  // width, height
    val resolutionMax: Pair<Int, Int>? = null,
    val categories: List<String> = emptyList(),
    val sources: List<String> = emptyList(),
    val contentTags: List<String> = emptyList(),  // landscape, portrait, people, nature, etc.
    val excludeNsfw: Boolean = true,
    val excludeBlurry: Boolean = false,
    val excludeWithText: Boolean = false,
    val minQuality: String = "MEDIUM",
    val sentiments: List<String> = emptyList(),  // calm, energetic, inspiring, etc.
    val dateRange: Pair<Long, Long>? = null,  // min, max timestamps
)
