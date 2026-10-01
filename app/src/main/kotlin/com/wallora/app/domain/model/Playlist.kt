package com.wallora.app.domain.model

/**
 * Small domain model for collections used by the UI layer.
 */
data class WallpaperCollection(
    val id: Long,
    val name: String,
    val description: String = "",
    val wallpaperCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
)

/**
 * Small domain model for playlists used by the UI layer.
 */
data class WallpaperPlaylist(
    val id: Long,
    val name: String,
    val description: String = "",
    val collectionId: Long? = null,
    val rotationMode: String = "RANDOM",
    val rotationIntervalMs: Long = 3_600_000L,
    val isActive: Boolean = false,
)
