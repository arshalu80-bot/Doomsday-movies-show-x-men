package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.MediaItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_items ORDER BY category ASC, originalIndex ASC")
    fun getAllItems(): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE category = :category ORDER BY originalIndex ASC")
    fun getItemsByCategory(category: String): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE watched = 1 ORDER BY watchedTimestamp DESC, originalIndex ASC")
    fun getWatchedItems(): Flow<List<MediaItem>>

    @Query("SELECT COUNT(*) FROM media_items")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<MediaItem>)

    @Query("UPDATE media_items SET watched = :watched, watchedTimestamp = :timestamp WHERE id = :id")
    suspend fun updateWatchedStatus(id: String, watched: Boolean, timestamp: Long)

    @Query("UPDATE media_items SET watched = :watched, watchedTimestamp = :timestamp WHERE category = :category")
    suspend fun updateAllInCategory(category: String, watched: Boolean, timestamp: Long)

    @Query("UPDATE media_items SET watched = 0, watchedTimestamp = 0")
    suspend fun resetAllWatched()

    @Query("DELETE FROM media_items")
    suspend fun deleteAll()
}
