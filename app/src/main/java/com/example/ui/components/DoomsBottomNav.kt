package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentMCU
import com.example.ui.theme.AccentSpidey
import com.example.ui.theme.AccentWatched
import com.example.ui.theme.AccentXMen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.DoomsTab

@Composable
fun DoomsBottomNav(
    currentTab: DoomsTab,
    mcuLeftCount: Int,
    watchedCount: Int,
    xmenLeftCount: Int,
    spideyLeftCount: Int,
    onTabSelected: (DoomsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xF505070A))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color.Black)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xF2101520))
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavButton(
                title = "MCU Timeline",
                badgeCount = mcuLeftCount,
                isSelected = currentTab == DoomsTab.MCU,
                accentColor = AccentMCU,
                onClick = { onTabSelected(DoomsTab.MCU) },
                testTag = "nav_btn_mcu",
                modifier = Modifier.weight(1f)
            )

            NavButton(
                title = "Watch History",
                badgeCount = watchedCount,
                isSelected = currentTab == DoomsTab.WATCHED,
                accentColor = AccentWatched,
                onClick = { onTabSelected(DoomsTab.WATCHED) },
                testTag = "nav_btn_watched",
                modifier = Modifier.weight(1f)
            )

            NavButton(
                title = "X-Men",
                badgeCount = xmenLeftCount,
                isSelected = currentTab == DoomsTab.XMEN,
                accentColor = AccentXMen,
                onClick = { onTabSelected(DoomsTab.XMEN) },
                testTag = "nav_btn_xmen",
                modifier = Modifier.weight(1f)
            )

            NavButton(
                title = "Spider-Man",
                badgeCount = spideyLeftCount,
                isSelected = currentTab == DoomsTab.SPIDEY,
                accentColor = AccentSpidey,
                onClick = { onTabSelected(DoomsTab.SPIDEY) },
                testTag = "nav_btn_spidey",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavButton(
    title: String,
    badgeCount: Int,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) accentColor else TextSecondary,
        label = "navTextColor"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Color(0x1FFFFFFF) else Color.Transparent,
        label = "navBgColor"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, color = accentColor),
                onClick = onClick
            )
            .padding(vertical = 8.dp, horizontal = 2.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = textColor,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(3.dp))
        // Badge pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isSelected) accentColor.copy(alpha = 0.2f) else Color(0x14FFFFFF)
                )
                .padding(horizontal = 7.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$badgeCount",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) accentColor else TextMuted
                )
            )
        }
    }
}
