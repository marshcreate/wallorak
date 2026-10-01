package com.wallora.app.data.repository

import com.wallora.app.data.local.dao.CollectionDao
import com.wallora.app.data.local.dao.PlaylistDao
import com.wallora.app.data.local.entity.CollectionEntity
import com.wallora.app.data.local.entity.CollectionItemEntity
import com.wallora.app.data.local.entity.PlaylistEntity
import com.wallora.app.domain.model.Wallpaper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallpaperCollectionRepository @Inject constructor(
    private val collectionDao: CollectionDao,
    private val playlistDao: PlaylistDao,
) {
    suspend fun createCollection(name: String, description: String = ""): Long =
        collectionDao.insertCollection(
            CollectionEntity(
                name = name,
                description = description,
                updatedAt = System.currentTimeMillis(),
            )
        )

    suspend fun deleteCollection(collectionId: Long) = collectionDao.deleteCollection(collectionId)

    suspend fun renameCollection(collectionId: Long, newName: String) {
        val current = collectionDao.getCollection(collectionId) ?: return
        collectionDao.insertCollection(
            current.copy(name = newName, updatedAt = System.currentTimeMillis())
        )
    }

    suspend fun addWallpaperToCollection(collectionId: Long, wallpaper: Wallpaper) {
        collectionDao.insertItem(CollectionItemEntity.fromWallpaper(collectionId, wallpaper))
    }

    suspend fun removeWallpaperFromCollection(collectionId: Long, globalKey: String) =
        collectionDao.deleteItem(collectionId, globalKey)

    suspend fun getCollectionWallpapers(collectionId: Long): List<Wallpaper> =
        collectionDao.getItems(collectionId).map { it.toWallpaper() }

    fun observeCollections(): Flow<List<CollectionEntity>> = collectionDao.observeCollections()

    suspend fun createPlaylist(
        name: String,
        description: String = "",
        collectionId: Long? = null,
        rotationMode: String = "RANDOM",
        rotationIntervalMs: Long = 3_600_000L,
        isActive: Boolean = false,
    ): Long = playlistDao.insertPlaylist(
        PlaylistEntity(
            name = name,
            description = description,
            collectionId = collectionId,
            rotationMode = rotationMode,
            rotationIntervalMs = rotationIntervalMs,
            isActive = isActive,
        )
    )

    suspend fun setPlaylistActive(playlistId: Long, active: Boolean) {
        val current = playlistDao.getPlaylist(playlistId) ?: return
        playlistDao.updatePlaylist(current.copy(isActive = active))
        if (active) playlistDao.clearActivePlaylists()
        val updated = playlistDao.getPlaylist(playlistId) ?: return
        playlistDao.updatePlaylist(updated.copy(isActive = active))
    }

    suspend fun getActivePlaylist(): PlaylistEntity? = playlistDao.getActivePlaylist()

    suspend fun getCollectionWallpaperIds(collectionId: Long): List<String> =
        collectionDao.getItems(collectionId).map { it.globalKey }

    suspend fun getPlaylistWallpapers(playlist: PlaylistEntity): List<Wallpaper> =
        playlist.collectionId?.let { getCollectionWallpapers(it) } ?: emptyList()
}
