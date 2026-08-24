package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.MediaItem
import com.example.ui.components.DoomHeader
import com.example.ui.components.DoomsBottomNav
import com.example.ui.components.MediaItemCard
import com.example.ui.components.RandomPickDialog
import com.example.ui.components.TabHeaderBar
import com.example.ui.theme.AccentDoom
import com.example.ui.theme.AccentMCU
import com.example.ui.theme.AccentSeries
import com.example.ui.theme.AccentWatched
import com.example.ui.theme.AccentXMen
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardBg
import com.example.ui.theme.CardBgElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.DoomsTab
import com.example.ui.viewmodel.DoomsViewModel
import com.example.ui.viewmodel.MediaFilter

@Composable
fun DoomsScreen(
    viewModel: DoomsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val countdown by viewModel.countdown.collectAsStateWithLifecycle()

    var showMenu by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    // Filter displayed list based on active tab, search, and category filters
    val displayedItems = remember(
        uiState.currentTab,
        uiState.searchQuery,
        uiState.activeFilter,
        uiState.watchedSubFilter,
        uiState.mcuUnwatched,
        uiState.watchedItems,
        uiState.xmenUnwatched,
        uiState.seriesUnwatched
    ) {
        val baseList: List<MediaItem> = when (uiState.currentTab) {
            DoomsTab.MCU -> uiState.mcuUnwatched
            DoomsTab.WATCHED -> {
                if (uiState.watchedSubFilter == "ALL") uiState.watchedItems
                else uiState.watchedItems.filter { it.category.equals(uiState.watchedSubFilter, ignoreCase = true) }
            }
            DoomsTab.XMEN -> uiState.xmenUnwatched
            DoomsTab.SERIES -> uiState.seriesUnwatched
        }

        // Apply search query
        var filtered = if (uiState.searchQuery.isBlank()) {
            baseList
        } else {
            baseList.filter { it.title.contains(uiState.searchQuery, ignoreCase = true) }
        }

        // Apply type filters (for non-watched tabs)
        if (uiState.currentTab != DoomsTab.WATCHED) {
            filtered = when (uiState.activeFilter) {
                MediaFilter.ALL -> filtered
                MediaFilter.MOVIES -> filtered.filter { it.typeTag.equals("Movie", ignoreCase = true) }
                MediaFilter.SERIES -> filtered.filter { it.typeTag.equals("TV Series", ignoreCase = true) }
                MediaFilter.SPECIALS -> filtered.filter { it.typeTag.equals("Special", ignoreCase = true) || it.typeTag.equals("Animated", ignoreCase = true) }
            }
        }

        filtered
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                // Cyberpunk ambient background gradients
                drawRect(BgDark)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(AccentDoom.copy(alpha = 0.08f), Color.Transparent),
                        center = Offset(size.width * 0.1f, size.height * 0.1f),
                        radius = size.width * 0.7f
                    )
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(AccentMCU.copy(alpha = 0.06f), Color.Transparent),
                        center = Offset(size.width * 0.9f, size.height * 0.9f),
                        radius = size.width * 0.7f
                    )
                )
            },
        bottomBar = {
            DoomsBottomNav(
                currentTab = uiState.currentTab,
                mcuLeftCount = uiState.mcuUnwatched.size,
                watchedCount = uiState.watchedItems.size,
                xmenLeftCount = uiState.xmenUnwatched.size,
                seriesLeftCount = uiState.seriesUnwatched.size,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 650.dp)
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Header with Doom Mask, DOOMS Title, Live Countdown & Progress
                    DoomHeader(
                        countdown = countdown,
                        totalWatched = uiState.totalWatchedCount,
                        totalItems = uiState.totalItemsCount
                    )

                    // Search & Actions Bar
                    SearchBarAndActions(
                        searchQuery = uiState.searchQuery,
                        onSearchChanged = { viewModel.setSearchQuery(it) },
                        onPickRandom = { viewModel.pickRandomUnwatched() },
                        onMenuClick = { showMenu = true }
                    )

                    // Filter chips row
                    FilterChipsRow(
                        currentTab = uiState.currentTab,
                        activeFilter = uiState.activeFilter,
                        watchedSubFilter = uiState.watchedSubFilter,
                        onFilterSelected = { viewModel.setFilter(it) },
                        onWatchedSubFilterSelected = { viewModel.setWatchedSubFilter(it) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tab Content Card Frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardBg)
                            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Active Tab Header Bar
                            TabHeaderBar(
                                currentTab = uiState.currentTab,
                                itemCount = when (uiState.currentTab) {
                                    DoomsTab.MCU -> uiState.mcuUnwatched.size
                                    DoomsTab.WATCHED -> uiState.watchedItems.size
                                    DoomsTab.XMEN -> uiState.xmenUnwatched.size
                                    DoomsTab.SERIES -> uiState.seriesUnwatched.size
                                }
                            )

                            // Item List or Empty Placeholder
                            if (displayedItems.isEmpty()) {
                                EmptyStateView(
                                    currentTab = uiState.currentTab,
                                    searchQuery = uiState.searchQuery,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                LazyColumn(
                                    contentPadding = PaddingValues(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .testTag("media_list")
                                ) {
                                    items(
                                        items = displayedItems,
                                        key = { it.id }
                                    ) { item ->
                                        MediaItemCard(
                                            item = item,
                                            onToggleWatched = { viewModel.toggleWatched(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Dropdown Menu for Tab / Reset actions
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(CardBgElevated)
                ) {
                    if (uiState.currentTab != DoomsTab.WATCHED) {
                        DropdownMenuItem(
                            text = { Text("Mark All in Tab as Watched", color = TextPrimary) },
                            onClick = {
                                viewModel.markAllInCurrentTab(true)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mark All in Tab as Unwatched", color = TextPrimary) },
                            onClick = {
                                viewModel.markAllInCurrentTab(false)
                                showMenu = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Reset All Watch History", color = AccentMCU) },
                        onClick = {
                            showResetConfirm = true
                            showMenu = false
                        }
                    )
                }
            }
        }
    }

    // Reset confirmation dialog
    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            containerColor = Color(0xFF0F1420),
            title = {
                Text(
                    text = "Reset All Progress?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AccentMCU
                    )
                )
            },
            text = {
                Text(
                    text = "This will mark all MCU, X-Men, and Marvel Series items as unwatched.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllWatched()
                        showResetConfirm = false
                    }
                ) {
                    Text("Reset All", color = AccentMCU, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        )
    }

    // Random Pick Next Watch Dialog
    uiState.randomPickedItem?.let { pickedItem ->
        RandomPickDialog(
            item = pickedItem,
            onDismiss = { viewModel.dismissRandomPick() },
            onToggleWatched = { viewModel.toggleWatched(it) },
            onPickAnother = { viewModel.pickRandomUnwatched() }
        )
    }
}

@Composable
private fun SearchBarAndActions(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    onPickRandom: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChanged,
            placeholder = {
                Text(
                    text = "Search movies, series, specials...",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, color = TextMuted)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchChanged("") },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBg,
                unfocusedContainerColor = CardBg,
                focusedBorderColor = AccentDoom,
                unfocusedBorderColor = Color(0x24FFFFFF),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .testTag("search_field")
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Next Watch Pick Button
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AccentDoom.copy(alpha = 0.15f))
                .border(1.dp, AccentDoom.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .clickable { onPickRandom() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "Random Pick",
                tint = AccentDoom,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // More options button
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0x14FFFFFF))
                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(12.dp))
                .clickable { onMenuClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Options",
                tint = TextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun FilterChipsRow(
    currentTab: DoomsTab,
    activeFilter: MediaFilter,
    watchedSubFilter: String,
    onFilterSelected: (MediaFilter) -> Unit,
    onWatchedSubFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (currentTab == DoomsTab.WATCHED) {
            val watchedFilters = listOf("ALL" to "All Done", "MCU" to "MCU", "XMEN" to "X-Men", "SERIES" to "Shows")
            items(watchedFilters) { (key, label) ->
                val isSelected = watchedSubFilter == key
                FilterChip(
                    label = label,
                    isSelected = isSelected,
                    accentColor = AccentWatched,
                    onClick = { onWatchedSubFilterSelected(key) }
                )
            }
        } else {
            val filters = listOf(
                MediaFilter.ALL,
                MediaFilter.MOVIES,
                MediaFilter.SERIES,
                MediaFilter.SPECIALS
            )
            items(filters) { filter ->
                val isSelected = activeFilter == filter
                val accentColor = when (currentTab) {
                    DoomsTab.MCU -> AccentMCU
                    DoomsTab.XMEN -> AccentXMen
                    DoomsTab.SERIES -> AccentSeries
                    else -> AccentDoom
                }
                FilterChip(
                    label = filter.label,
                    isSelected = isSelected,
                    accentColor = accentColor,
                    onClick = { onFilterSelected(filter) }
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.2f) else Color(0x0FFFFFFF))
            .border(
                1.dp,
                if (isSelected) accentColor else Color(0x1AFFFFFF),
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) accentColor else TextSecondary
            )
        )
    }
}

@Composable
private fun EmptyStateView(
    currentTab: DoomsTab,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (searchQuery.isNotEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No matches found",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Try adjusting your search query.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                )
            }
        } else if (currentTab == DoomsTab.WATCHED) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No items marked as watched yet.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Click any movie/show to shift it here!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "All caught up in this bar!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AccentDoom
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Check the Watched tab to see completed items.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}
