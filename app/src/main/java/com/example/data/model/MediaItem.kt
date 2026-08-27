package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItem(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // "mcu", "xmen"
    val originalIndex: Int,
    val watched: Boolean = false,
    val watchedTimestamp: Long = 0L,
    val releaseYear: Int = 2008,
    val typeTag: String = "Movie", // "Movie", "Series", "Special", "Animated"
    val isShow: Boolean = false
)
