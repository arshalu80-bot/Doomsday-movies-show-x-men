package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
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
        // Brand row: Exact Original Metallic Avengers Doom Icon + Gradient DOOMS Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            DoomMaskIcon(modifier = Modifier.size(46.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "DOOMS",
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp,
                    fontSize = 26.sp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFF00FF88),
                            Color(0xFF00D2FF)
                        )
                    )
                ),
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

            Spacer(modifier = Modifier.height(12.dp))
            
            // Custom Gradient Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x0DFFFFFF))
                    .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(8.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(AccentDoom, Color(0xFFA855F7))
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = "$totalWatched of $totalItems (${(progress * 100).toInt()}%)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
    }
}

@Composable
fun DoomMaskIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(16.dp, shape = CircleShape, spotColor = AccentDoom.copy(alpha = 0.6f))
            .clip(CircleShape)
            .background(Color.Black)
            .border(2.dp, AccentDoom, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "A",
            color = AccentDoom,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif
        )
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
