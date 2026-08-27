package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentMCU
import com.example.ui.theme.AccentSpidey
import com.example.ui.theme.AccentWatched
import com.example.ui.theme.AccentXMen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.DoomsTab

@Composable
fun TabHeaderBar(
    currentTab: DoomsTab,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    val (title, accentColor, badgeText) = when (currentTab) {
        DoomsTab.MCU -> Triple("MCU MOVIES & SERIES (68)", AccentMCU, "$itemCount Left")
        DoomsTab.WATCHED -> Triple("MOVIES WATCH HISTORY", AccentWatched, "$itemCount Done")
        DoomsTab.XMEN -> Triple("X-MEN MOVIES (14)", AccentXMen, "$itemCount Left")
        DoomsTab.SPIDEY -> Triple("NON MCU SPIDER-MAN MOVIES (5)", AccentSpidey, "$itemCount Left")
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color(0xFF0E121C))
    ) {
        // Accent Top Border line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(accentColor)
        )

        // Bar Header content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 0.5.sp
                ),
                color = accentColor
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x1FFFFFFF))
                    .border(1.dp, Color(0x24FFFFFF), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = TextPrimary
                    )
                )
            }
        }
    }
}
