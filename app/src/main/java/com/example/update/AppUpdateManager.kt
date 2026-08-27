package com.example.update

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.FileProvider
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class AppUpdateManager(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    // Default remote version check URL
    var updateEndpointUrl: String = "https://raw.githubusercontent.com/arshali1854/dooms-releases/main/version.json"

    /**
     * Checks remote endpoint for a newer version.
     * Compares remote versionCode against BuildConfig.VERSION_CODE.
     */
    suspend fun checkForUpdate(endpoint: String = updateEndpointUrl): Result<UpdateInfo?> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(endpoint)
                .addHeader("Accept", "application/json")
                .build()

            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("HTTP Error: ${response.code}"))
                }
                val bodyString = response.body?.string()
                    ?: return@withContext Result.failure(Exception("Empty response body"))

                val json = JSONObject(bodyString)
                val remoteVersionCode = json.optInt("versionCode", 0)
                val remoteVersionName = json.optString("versionName", "1.0")
                val apkUrl = json.optString("apkUrl", "")
                val releaseNotes = json.optString(
                    "releaseNotes",
                    "• Complete MCU & X-Men timeline updates\n• Bug fixes and speed enhancements"
                )
                val mandatory = json.optBoolean("mandatory", false)
                val fileSize = json.optString("fileSizeMb", "22 MB")

                val currentVersionCode = BuildConfig.VERSION_CODE

                if (remoteVersionCode > currentVersionCode && apkUrl.isNotBlank()) {
                    val updateInfo = UpdateInfo(
                        versionCode = remoteVersionCode,
                        versionName = remoteVersionName,
                        apkUrl = apkUrl,
                        releaseNotes = releaseNotes,
                        mandatory = mandatory,
                        fileSizeMb = fileSize
                    )
                    Result.success(updateInfo)
                } else {
                    Result.success(null) // App is up to date
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Downloads the APK in the background using Android's DownloadManager and emits download progress.
     */
    fun startDownload(updateInfo: UpdateInfo): Flow<UpdateState> = flow {
        emit(UpdateState.Downloading(updateInfo, progressPercent = 0, downloadedBytes = 0, totalBytes = 0))

        val fileName = "DOOMS_v${updateInfo.versionName}.apk"
        val destinationFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        if (destinationFile.exists()) {
            destinationFile.delete()
        }

        val uri = Uri.parse(updateInfo.apkUrl)
        val request = DownloadManager.Request(uri).apply {
            setTitle("DOOMS v${updateInfo.versionName} Update")
            setDescription("Downloading latest DOOMS update...")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationUri(Uri.fromFile(destinationFile))
            setMimeType("application/vnd.android.package-archive")
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }

        val downloadId = try {
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            emit(UpdateState.Error("Failed to start download: ${e.localizedMessage}"))
            return@flow
        }

        var isDownloading = true
        while (isDownloading) {
            delay(400)
            val query = DownloadManager.Query().setFilterById(downloadId)
            val cursor: Cursor? = downloadManager.query(query)

            if (cursor != null && cursor.moveToFirst()) {
                val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val totalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)

                val status = if (statusIndex >= 0) cursor.getInt(statusIndex) else -1
                val downloadedBytes = if (downloadedIndex >= 0) cursor.getLong(downloadedIndex) else 0L
                val totalBytes = if (totalIndex >= 0) cursor.getLong(totalIndex) else 0L

                val progress = if (totalBytes > 0) {
                    ((downloadedBytes * 100) / totalBytes).toInt().coerceIn(0, 100)
                } else {
                    0
                }

                when (status) {
                    DownloadManager.STATUS_RUNNING -> {
                        emit(
                            UpdateState.Downloading(
                                info = updateInfo,
                                progressPercent = progress,
                                downloadedBytes = downloadedBytes,
                                totalBytes = totalBytes
                            )
                        )
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        isDownloading = false
                        val apkUri = getApkUri(destinationFile)
                        emit(UpdateState.ReadyToInstall(apkUri = apkUri, info = updateInfo))
                    }
                    DownloadManager.STATUS_FAILED -> {
                        isDownloading = false
                        val reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                        val reason = if (reasonIndex >= 0) cursor.getInt(reasonIndex) else -1
                        emit(UpdateState.Error("Download failed (Error code: $reason)"))
                    }
                }
                cursor.close()
            } else {
                cursor?.close()
            }
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Generates a secure FileProvider content Uri for installation.
     */
    fun getApkUri(file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * Launches the PackageInstaller Intent to install the updated APK.
     * Checks unknown source install permission on Android 8.0+ (Oreo).
     */
    fun triggerInstall(apkUri: Uri): Boolean {
        return try {
            // Check unknown sources permission on Android 8.0+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!context.packageManager.canRequestPackageInstalls()) {
                    val settingsIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                        data = Uri.parse("package:${context.packageName}")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(settingsIntent)
                    return false
                }
            }

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(installIntent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
