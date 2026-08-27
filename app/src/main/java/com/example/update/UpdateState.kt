package com.example.update

import android.net.Uri

sealed interface UpdateState {
    object Idle : UpdateState
    object Checking : UpdateState
    data class UpdateAvailable(val info: UpdateInfo) : UpdateState
    data class Downloading(
        val info: UpdateInfo,
        val progressPercent: Int = 0,
        val downloadedBytes: Long = 0L,
        val totalBytes: Long = 0L
    ) : UpdateState
    data class ReadyToInstall(
        val apkUri: Uri,
        val info: UpdateInfo
    ) : UpdateState
    data class Error(val message: String) : UpdateState
    data class UpToDate(val message: String = "DOOMS is already on the latest version!") : UpdateState
}
