package com.example.update

import androidx.annotation.Keep

@Keep
data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    val releaseNotes: String = "• New MCU & X-Men timeline entries\n• Performance improvements\n• Bug fixes",
    val mandatory: Boolean = false,
    val fileSizeMb: String = "22 MB"
)
