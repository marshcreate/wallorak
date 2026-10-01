package com.wallora.app.domain

/**
 * Use case for filtering wallpapers by multiple criteria.
 * Supports color, aspect ratio, resolution, content tags, and more.
 */
interface WallpaperFilterUseCase {
    suspend operator fun invoke(
        query: String? = null,
        colorFilter: String? = null,  // "red", "blue", etc.
        aspectRatio: String? = null,  // "16:9", "9:16", etc.
        minResolution: Pair<Int, Int>? = null,
        maxResolution: Pair<Int, Int>? = null,
        minQuality: String? = null,  // MEDIUM, HIGH, ULTRA_HD
        contentTags: List<String> = emptyList(),  // nature, urban, people, etc.
        excludeNsfw: Boolean = true,
        collections: List<String> = emptyList(),
        sources: List<String> = emptyList(),
    ): Result<List<WallpaperEnhanced>>
}

/**
 * Use case for recommending wallpapers to users.
 * Uses ML models and collaborative filtering.
 */
interface RecommendWallpapersUseCase {
    suspend operator fun invoke(
        userHistory: List<WallpaperEnhanced>,
        limit: Int = 20,
        diversity: Float = 0.5f,  // 0 = similar, 1 = diverse
    ): Result<List<WallpaperEnhanced>>
}

/**
 * Use case for managing collections.
 */
interface ManageCollectionsUseCase {
    suspend fun createCollection(name: String, description: String = ""): Result<Long>
    suspend fun addToCollection(wallpaperId: String, collectionId: Long): Result<Unit>
    suspend fun removeFromCollection(wallpaperId: String, collectionId: Long): Result<Unit>
    suspend fun deleteCollection(collectionId: Long): Result<Unit>
    suspend fun renameCollection(collectionId: Long, newName: String): Result<Unit>
    suspend fun shareCollection(collectionId: Long): Result<String>  // returns share token
}

/**
 * Use case for managing playlists and rotation policies.
 */
interface ManagePlaylistsUseCase {
    suspend fun createPlaylist(
        name: String,
        collectionId: Long? = null,
        rotationMode: String = "RANDOM",
        rotationInterval: Long = 3_600_000L,
    ): Result<Long>
    
    suspend fun setRotationPolicy(
        playlistId: Long,
        adaptive: Boolean = false,
        batteryAware: Boolean = true,
        networkAware: Boolean = true,
    ): Result<Unit>
    
    suspend fun activatePlaylist(playlistId: Long): Result<Unit>
    suspend fun deactivatePlaylist(playlistId: Long): Result<Unit>
}

/**
 * Use case for analytics and user behavior tracking.
 */
interface AnalyticsUseCase {
    suspend fun trackWallpaperView(wallpaperId: String): Result<Unit>
    suspend fun trackWallpaperFavorite(wallpaperId: String): Result<Unit>
    suspend fun trackDwellTime(wallpaperId: String, durationMs: Long): Result<Unit>
    suspend fun getUserPreferences(): Result<UserPreferences>
    suspend fun getTrendingWallpapers(limit: Int = 20): Result<List<WallpaperEnhanced>>
}

data class UserPreferences(
    val favoriteColors: List<String> = emptyList(),
    val favoriteCategories: List<String> = emptyList(),
    val favoriteSources: List<String> = emptyList(),
    val averageDwellTime: Long = 0L,
    val totalWallpapersViewed: Int = 0,
    val totalFavorites: Int = 0,
)
