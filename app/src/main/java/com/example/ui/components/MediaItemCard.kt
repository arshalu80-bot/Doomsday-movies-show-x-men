package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MediaItem
import com.example.ui.theme.AccentMCU
import com.example.ui.theme.AccentSeries
import com.example.ui.theme.AccentWatched
import com.example.ui.theme.AccentXMen
import com.example.ui.theme.CardBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MediaItemCard(
    item: MediaItem,
    onToggleWatched: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = when (item.category) {
        "mcu" -> AccentMCU
        "xmen" -> AccentXMen
        "series" -> AccentSeries
        else -> AccentWatched
    }

    val animatedBg by animateColorAsState(
        targetValue = if (item.watched) Color(0x1F221233) else CardBg,
        animationSpec = tween(250),
        label = "cardBg"
    )

    val animatedBorder by animateColorAsState(
        targetValue = if (item.watched) AccentWatched.copy(alpha = 0.4f) else Color(0x1AFFFFFF),
        animationSpec = tween(250),
        label = "cardBorder"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(animatedBg)
            .border(1.dp, animatedBorder, RoundedCornerShape(12.dp))
            .clickable { onToggleWatched(item) }
            .padding(horizontal = 14.dp, vertical = 12.dp)
            .testTag("item_card_${item.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Checkbox
            CustomWatchCheckbox(
                checked = item.watched,
                onCheckedChange = { onToggleWatched(item) },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Index badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(categoryColor.copy(alpha = 0.12f))
                    .border(1.dp, categoryColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("#%02d", item.originalIndex),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (item.watched) TextSecondary else categoryColor
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = if (item.watched) TextSecondary else TextPrimary,
                        textDecoration = if (item.watched) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 2
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    // Category Tag
                    MetaBadge(
                        text = item.category.uppercase(),
                        color = categoryColor
                    )

                    // SHOW tag
                    if (item.isShow) {
                        MetaBadge(
                            text = "SHOW",
                            color = AccentSeries
                        )
                    }

                    // Release Year
                    if (item.releaseYear > 0) {
                        Text(
                            text = "${item.releaseYear}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomWatchCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (checked) AccentWatched else Color(0x1AFFFFFF)
    val borderColor = if (checked) AccentWatched else Color(0x66FFFFFF)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .border(2.dp, borderColor, RoundedCornerShape(6.dp))
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Watched",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun MetaBadge(
    text: String,
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 5.dp, vertical = 1.5.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        )
    }
}
