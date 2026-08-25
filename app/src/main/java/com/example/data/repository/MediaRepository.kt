package com.example.data.repository

import com.example.data.local.MediaDao
import com.example.data.model.DefaultMediaData
import com.example.data.model.MediaItem
import kotlinx.coroutines.flow.Flow

class MediaRepository(private val mediaDao: MediaDao) {
    val allItems: Flow<List<MediaItem>> = mediaDao.getAllItems()
    val watchedItems: Flow<List<MediaItem>> = mediaDao.getWatchedItems()

    suspend fun seedDatabaseIfEmpty() {
        val currentCount = mediaDao.getCount()
        if (currentCount != 79) {
            mediaDao.deleteAll()
            val initialItems = DefaultMediaData.generateInitialItems()
            mediaDao.insertAll(initialItems)
        }
    }

    suspend fun toggleWatched(item: MediaItem) {
        val newStatus = !item.watched
        val timestamp = if (newStatus) System.currentTimeMillis() else 0L
        mediaDao.updateWatchedStatus(item.id, newStatus, timestamp)
    }

    suspend fun markAllInCategory(category: String, watched: Boolean) {
        val timestamp = if (watched) System.currentTimeMillis() else 0L
        mediaDao.updateAllInCategory(category, watched, timestamp)
    }

    suspend fun resetAllWatched() {
        mediaDao.resetAllWatched()
    }
}
