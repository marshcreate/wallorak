package com.wallora.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wallora.app.domain.model.Wallpaper
import com.wallora.app.domain.model.SourceId

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSmart: Boolean = false,
    val smartRules: String = "",
    val isFavorite: Boolean = false,
)

@Entity(
    tableName = "collection_items",
    primaryKeys = ["collectionId", "globalKey"],
)
data class CollectionItemEntity(
    val collectionId: Long,
    val globalKey: String,
    val sourceId: String,
    val id: String,
    val thumbUrl: String,
    val fullUrl: String,
    val width: Int,
    val height: Int,
    val author: String,
    val authorUrl: String,
    val sourcePageUrl: String,
    val colorHint: Int? = null,
    val addedAt: Long = System.currentTimeMillis(),
) {
    fun toWallpaper(): Wallpaper = Wallpaper(
        id = id,
        sourceId = SourceId.valueOf(sourceId),
        thumbUrl = thumbUrl,
        fullUrl = fullUrl,
        width = width,
        height = height,
        author = author,
        authorUrl = authorUrl,
        sourcePageUrl = sourcePageUrl,
        colorHint = colorHint,
        category = null,
        tags = emptyList(),
    )

    companion object {
        fun fromWallpaper(collectionId: Long, wallpaper: Wallpaper): CollectionItemEntity =
            CollectionItemEntity(
                collectionId = collectionId,
                globalKey = wallpaper.globalKey,
                sourceId = wallpaper.sourceId.name,
                id = wallpaper.id,
                thumbUrl = wallpaper.thumbUrl,
                fullUrl = wallpaper.fullUrl,
                width = wallpaper.width,
                height = wallpaper.height,
                author = wallpaper.author,
                authorUrl = wallpaper.authorUrl,
                sourcePageUrl = wallpaper.sourcePageUrl,
                colorHint = wallpaper.colorHint,
            )
    }
}
