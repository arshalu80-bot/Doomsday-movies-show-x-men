package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.AccentDoom
import com.example.ui.theme.AccentMCU
import com.example.ui.theme.AccentSeries
import com.example.ui.theme.AccentWatched
import com.example.ui.theme.CardBg
import com.example.ui.theme.CardBgElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.update.UpdateInfo
import com.example.update.UpdateState

@Composable
fun UpdateDialog(
    updateState: UpdateState,
    onStartDownload: (UpdateInfo) -> Unit,
    onInstall: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (updateState) {
        is UpdateState.UpdateAvailable -> {
            val info = updateState.info
            AlertDialog(
                onDismissRequest = {
                    if (!info.mandatory) onDismiss()
                },
                containerColor = CardBgElevated,
                modifier = modifier
                    .testTag("update_available_dialog")
                    .border(1.dp, AccentDoom.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .border(1.5.dp, AccentDoom, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = "Update Icon",
                                tint = AccentDoom,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "New Update Available",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "v${BuildConfig.VERSION_NAME} → v${info.versionName}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = AccentDoom,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Main Prompt Text
                        Text(
                            text = "New Update Available. Download and Install?",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        )

                        // Size & Status Tag
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color.White.copy(alpha = 0.08f)
                            ) {
                                Text(
                                    text = "Size: ${info.fileSizeMb}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            if (info.mandatory) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AccentMCU.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "REQUIRED",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = AccentMCU,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        // Release Notes Box
                        if (info.releaseNotes.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black.copy(alpha = 0.4f))
                                    .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "WHAT'S NEW",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextMuted,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    Text(
                                        text = info.releaseNotes,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { onStartDownload(info) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentDoom,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("update_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Update",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                },
                dismissButton = {
                    if (!info.mandatory) {
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("later_button")
                        ) {
                            Text("Later", color = TextMuted, fontSize = 13.sp)
                        }
                    }
                }
            )
        }

        is UpdateState.Downloading -> {
            val progress = updateState.progressPercent
            val downloadedMb = String.format("%.1f", updateState.downloadedBytes / (1024f * 1024f))
            val totalMb = String.format("%.1f", updateState.totalBytes / (1024f * 1024f))

            AlertDialog(
                onDismissRequest = { /* Non-cancellable during download */ },
                containerColor = CardBgElevated,
                modifier = modifier
                    .testTag("downloading_dialog")
                    .border(1.dp, AccentSeries.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = AccentSeries,
                            strokeWidth = 2.5.dp
                        )
                        Text(
                            text = "Downloading Update...",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "DOOMS v${updateState.info.versionName}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                            Text(
                                text = "$progress%",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = AccentSeries,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        LinearProgressIndicator(
                            progress = { progress / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AccentSeries,
                            trackColor = Color.White.copy(alpha = 0.1f)
                        )

                        if (updateState.totalBytes > 0) {
                            Text(
                                text = "$downloadedMb MB / $totalMb MB",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        } else {
                            Text(
                                text = "Connecting background download manager...",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Hide", color = TextMuted)
                    }
                }
            )
        }

        is UpdateState.ReadyToInstall -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                containerColor = CardBgElevated,
                modifier = modifier
                    .testTag("ready_to_install_dialog")
                    .border(1.dp, AccentDoom.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Ready",
                            tint = AccentDoom,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Download Complete",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                },
                text = {
                    Text(
                        text = "DOOMS v${updateState.info.versionName} is ready to install. User data and watched history will be safely preserved.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = onInstall,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentDoom,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("install_now_button")
                    ) {
                        Text("Install Now", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Later", color = TextMuted)
                    }
                }
            )
        }

        is UpdateState.UpToDate -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                containerColor = CardBgElevated,
                title = {
                    Text(
                        text = "App Up to Date",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AccentDoom
                        )
                    )
                },
                text = {
                    Text(
                        text = "You are running the latest version of DOOMS (v${BuildConfig.VERSION_NAME}).",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                },
                confirmButton = {
                    TextButton(onClick = onDismiss) {
                        Text("OK", color = AccentDoom, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        is UpdateState.Error -> {
            AlertDialog(
                onDismissRequest = onDismiss,
                containerColor = CardBgElevated,
                title = {
                    Text(
                        text = "Update Check",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = AccentMCU
                        )
                    )
                },
                text = {
                    Text(
                        text = updateState.message,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                },
                confirmButton = {
                    TextButton(onClick = onDismiss) {
                        Text("Dismiss", color = TextSecondary)
                    }
                }
            )
        }

        is UpdateState.Checking, is UpdateState.Idle -> {
            // No full-screen dialog needed for idle or silent initial check
        }
    }
}
