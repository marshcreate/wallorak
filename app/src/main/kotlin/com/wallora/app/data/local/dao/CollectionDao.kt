package com.wallora.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String = "",
    val collectionId: Long? = null,
    val rotationMode: String = "RANDOM",
    val rotationIntervalMs: Long = 3_600_000L,
    val isActive: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
)
