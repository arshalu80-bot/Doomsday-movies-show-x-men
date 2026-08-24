package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentDoom
import com.example.ui.theme.AccentDoomDark
import com.example.ui.theme.AccentSeries
import com.example.ui.theme.CardBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.CountdownTime

@Composable
fun DoomHeader(
    countdown: CountdownTime,
    totalWatched: Int,
    totalItems: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Brand row: Doctor Doom Glowing Icon + DOOMS Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            DoomMaskIcon(modifier = Modifier.size(44.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "DOOMS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp,
                    fontSize = 28.sp
                ),
                color = AccentDoom,
                modifier = Modifier.testTag("app_title")
            )
        }

        // Live Countdown Card
        CountdownBox(
            countdown = countdown,
            modifier = Modifier.fillMaxWidth()
        )

        // Progress bar to full collection completion
        if (totalItems > 0) {
            val progress = totalWatched.toFloat() / totalItems.toFloat()
            val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
                label = "watchProgress"
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ROAD TO DOOMSDAY",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = AccentDoom,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "$totalWatched of $totalItems (${(progress * 100).toInt()}%)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = AccentDoom,
                trackColor = Color(0x3300FF88)
            )
        }
    }
}

@Composable
fun DoomMaskIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(12.dp, shape = CircleShape, spotColor = AccentDoom)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(AccentDoom.copy(alpha = 0.85f), Color(0xFF05070A)),
                    radius = 80f
                )
            )
            .border(2.dp, AccentDoom, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(32.dp)) {
            val w = size.width
            val h = size.height

            // Outer mask shape
            val maskPath = Path().apply {
                moveTo(w * 0.5f, h * 0.12f)
                lineTo(w * 0.78f, h * 0.82f)
                lineTo(w * 0.62f, h * 0.82f)
                lineTo(w * 0.55f, h * 0.64f)
                lineTo(w * 0.45f, h * 0.64f)
                lineTo(w * 0.38f, h * 0.82f)
                lineTo(w * 0.22f, h * 0.82f)
                close()
            }
            drawPath(maskPath, color = Color(0xFF111827))
            drawPath(maskPath, color = AccentDoom, style = Stroke(width = 2.5f))

            // Eyes / forehead
            val eyePath = Path().apply {
                moveTo(w * 0.5f, h * 0.32f)
                lineTo(w * 0.43f, h * 0.54f)
                lineTo(w * 0.57f, h * 0.54f)
                close()
            }
            drawPath(eyePath, color = Color(0xFF111827))
            drawPath(eyePath, color = AccentDoom, style = Stroke(width = 2f))

            // Mouth plate
            val platePath = Path().apply {
                moveTo(w * 0.38f, h * 0.55f)
                lineTo(w * 0.74f, h * 0.55f)
                lineTo(w * 0.70f, h * 0.64f)
                lineTo(w * 0.34f, h * 0.64f)
                close()
            }
            drawPath(platePath, color = AccentDoom)
        }
    }
}

@Composable
fun CountdownBox(
    countdown: CountdownTime,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(14.dp), ambientColor = Color.Black, spotColor = AccentDoom.copy(alpha = 0.3f))
            .background(Color(0xE60B0F18), RoundedCornerShape(14.dp))
            .border(1.dp, AccentDoom.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp)
            .testTag("countdown_box")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimerUnit(value = countdown.months, label = "Months")
            TimerDivider()
            TimerUnit(value = countdown.days, label = "Days")
            TimerDivider()
            TimerUnit(value = countdown.hours, label = "Hours")
            TimerDivider()
            TimerUnit(value = countdown.minutes, label = "Mins")
            TimerDivider()
            TimerUnit(value = countdown.seconds, label = "Secs")
        }
    }
}

@Composable
private fun TimerUnit(
    value: Int,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = String.format("%02d", value),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace
            ),
            color = AccentDoom
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                letterSpacing = 0.8.sp,
                fontWeight = FontWeight.Medium
            ),
            color = TextSecondary
        )
    }
}

@Composable
private fun TimerDivider() {
    Text(
        text = ":",
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = AccentDoom.copy(alpha = 0.4f)
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
