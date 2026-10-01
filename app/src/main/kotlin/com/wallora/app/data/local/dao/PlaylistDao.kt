package com.wallora.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.wallora.app.data.local.entity.CollectionEntity
import com.wallora.app.data.local.entity.CollectionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity): Long

    @Query("DELETE FROM collections WHERE id = :collectionId")
    suspend fun deleteCollection(collectionId: Long)

    @Query("SELECT * FROM collections ORDER BY updatedAt DESC")
    fun observeCollections(): Flow<List<CollectionEntity>>

    @Query("SELECT * FROM collections WHERE id = :collectionId LIMIT 1")
    suspend fun getCollection(collectionId: Long): CollectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CollectionItemEntity)

    @Query("DELETE FROM collection_items WHERE collectionId = :collectionId AND globalKey = :globalKey")
    suspend fun deleteItem(collectionId: Long, globalKey: String)

    @Query("SELECT * FROM collection_items WHERE collectionId = :collectionId ORDER BY addedAt DESC")
    suspend fun getItems(collectionId: Long): List<CollectionItemEntity>

    @Transaction
    @Query("SELECT * FROM collections WHERE id = :collectionId LIMIT 1")
    fun observeCollectionWithItems(collectionId: Long): Flow<CollectionWithItems?>
}

class CollectionWithItems(
    val collection: CollectionEntity,
    val items: List<CollectionItemEntity>,
)
