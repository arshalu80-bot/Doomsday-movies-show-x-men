package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DoomsDatabase
import com.example.data.model.MediaItem
import com.example.data.repository.MediaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import kotlin.random.Random

enum class DoomsTab(val title: String, val badgeKey: String) {
    MCU("Bar 1: MCU", "mcu"),
    WATCHED("Bar 2: Done", "watched"),
    XMEN("Bar 3: X-Men", "xmen"),
    SERIES("Bar 4: Shows", "series")
}

enum class MediaFilter(val label: String) {
    ALL("All"),
    MOVIES("Movies"),
    SERIES("Series"),
    SPECIALS("Specials")
}

data class CountdownTime(
    val months: Int = 0,
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val isFinished: Boolean = false
)

data class DoomsUiState(
    val currentTab: DoomsTab = DoomsTab.MCU,
    val searchQuery: String = "",
    val activeFilter: MediaFilter = MediaFilter.ALL,
    val watchedSubFilter: String = "ALL", // "ALL", "MCU", "XMEN", "SERIES"
    val mcuUnwatched: List<MediaItem> = emptyList(),
    val watchedItems: List<MediaItem> = emptyList(),
    val xmenUnwatched: List<MediaItem> = emptyList(),
    val seriesUnwatched: List<MediaItem> = emptyList(),
    val totalMcuCount: Int = 0,
    val totalXmenCount: Int = 0,
    val totalSeriesCount: Int = 0,
    val totalItemsCount: Int = 0,
    val totalWatchedCount: Int = 0,
    val randomPickedItem: MediaItem? = null,
    val isInitialized: Boolean = false
)

class DoomsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MediaRepository

    private val _currentTab = MutableStateFlow(DoomsTab.MCU)
    private val _searchQuery = MutableStateFlow("")
    private val _activeFilter = MutableStateFlow(MediaFilter.ALL)
    private val _watchedSubFilter = MutableStateFlow("ALL")
    private val _randomPickedItem = MutableStateFlow<MediaItem?>(null)

    private val _countdown = MutableStateFlow(CountdownTime())
    val countdown: StateFlow<CountdownTime> = _countdown

    init {
        val db = DoomsDatabase.getDatabase(application)
        repository = MediaRepository(db.mediaDao())

        viewModelScope.launch {
            repository.seedDatabaseIfEmpty()
        }

        startCountdownTicker()
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            while (isActive) {
                updateCountdown()
                delay(1000L)
            }
        }
    }

    // Target Date: 18 December 2026, 00:00:01 (raat ke 12:00 ke theek 1 second baad)
    private fun updateCountdown() {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(2026, Calendar.DECEMBER, 18, 0, 0, 1)
            set(Calendar.MILLISECOND, 0)
        }

        if (target.after(now)) {
            val targetYear = target.get(Calendar.YEAR)
            val targetMonth = target.get(Calendar.MONTH) // 0-based: December is 11
            val targetDate = target.get(Calendar.DAY_OF_MONTH)
            val targetHour = target.get(Calendar.HOUR_OF_DAY)
            val targetMin = target.get(Calendar.MINUTE)
            val targetSec = target.get(Calendar.SECOND)

            val nowYear = now.get(Calendar.YEAR)
            val nowMonth = now.get(Calendar.MONTH)
            val nowDate = now.get(Calendar.DAY_OF_MONTH)
            val nowHour = now.get(Calendar.HOUR_OF_DAY)
            val nowMin = now.get(Calendar.MINUTE)
            val nowSec = now.get(Calendar.SECOND)

            val years = targetYear - nowYear
            var months = (targetMonth - nowMonth) + (years * 12)
            var days = targetDate - nowDate
            var hours = targetHour - nowHour
            var minutes = targetMin - nowMin
            var seconds = targetSec - nowSec

            if (seconds < 0) {
                seconds += 60
                minutes--
            }
            if (minutes < 0) {
                minutes += 60
                hours--
            }
            if (hours < 0) {
                hours += 24
                days--
            }
            if (days < 0) {
                // Pichle mahine ke din calculate karna (same as JS new Date(year, month + 1, 0).getDate())
                val prevMonthLastDay = now.getActualMaximum(Calendar.DAY_OF_MONTH)
                days += prevMonthLastDay
                months--
            }

            _countdown.value = CountdownTime(
                months = months,
                days = days,
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                isFinished = false
            )
        } else {
            _countdown.value = CountdownTime(
                months = 0,
                days = 0,
                hours = 0,
                minutes = 0,
                seconds = 0,
                isFinished = true
            )
        }
    }

    private data class ViewFilterState(
        val tab: DoomsTab,
        val search: String,
        val filter: MediaFilter,
        val watchedSub: String,
        val randomPick: MediaItem?
    )

    private val filterStateFlow = combine(
        _currentTab,
        _searchQuery,
        _activeFilter,
        _watchedSubFilter,
        _randomPickedItem
    ) { tab, search, filter, watchedSub, randomPick ->
        ViewFilterState(tab, search, filter, watchedSub, randomPick)
    }

    val uiState: StateFlow<DoomsUiState> = combine(
        repository.allItems,
        filterStateFlow
    ) { allItems, filters ->
        val mcuAll = allItems.filter { it.category == "mcu" }
        val xmenAll = allItems.filter { it.category == "xmen" }
        val seriesAll = allItems.filter { it.isShow }

        val mcuUnwatched = mcuAll.filter { !it.watched }
        val xmenUnwatched = xmenAll.filter { !it.watched }
        val seriesUnwatched = seriesAll.filter { !it.watched }
        val watchedItems = allItems.filter { it.watched }

        DoomsUiState(
            currentTab = filters.tab,
            searchQuery = filters.search,
            activeFilter = filters.filter,
            watchedSubFilter = filters.watchedSub,
            mcuUnwatched = mcuUnwatched,
            watchedItems = watchedItems,
            xmenUnwatched = xmenUnwatched,
            seriesUnwatched = seriesUnwatched,
            totalMcuCount = mcuAll.size,
            totalXmenCount = xmenAll.size,
            totalSeriesCount = seriesAll.size,
            totalItemsCount = allItems.size,
            totalWatchedCount = watchedItems.size,
            randomPickedItem = filters.randomPick,
            isInitialized = allItems.isNotEmpty()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DoomsUiState()
    )

    fun selectTab(tab: DoomsTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: MediaFilter) {
        _activeFilter.value = filter
    }

    fun setWatchedSubFilter(category: String) {
        _watchedSubFilter.value = category
    }

    fun toggleWatched(item: MediaItem) {
        viewModelScope.launch {
            repository.toggleWatched(item)
        }
    }

    fun markAllInCurrentTab(watched: Boolean) {
        val tab = _currentTab.value
        viewModelScope.launch {
            when (tab) {
                DoomsTab.MCU -> repository.markAllInCategory("mcu", watched)
                DoomsTab.XMEN -> repository.markAllInCategory("xmen", watched)
                DoomsTab.SERIES -> {
                    val currentSeries = uiState.value.seriesUnwatched
                    currentSeries.forEach { repository.toggleWatched(it.copy(watched = !watched)) }
                }
                DoomsTab.WATCHED -> return@launch
            }
        }
    }

    fun resetAllWatched() {
        viewModelScope.launch {
            repository.resetAllWatched()
        }
    }

    fun pickRandomUnwatched() {
        viewModelScope.launch {
            val currentState = uiState.value
            val unwatchedPool = when (currentState.currentTab) {
                DoomsTab.MCU -> currentState.mcuUnwatched
                DoomsTab.XMEN -> currentState.xmenUnwatched
                DoomsTab.SERIES -> currentState.seriesUnwatched
                DoomsTab.WATCHED -> {
                    // Pick from all unwatched
                    currentState.mcuUnwatched + currentState.xmenUnwatched + currentState.seriesUnwatched
                }
            }
            if (unwatchedPool.isNotEmpty()) {
                _randomPickedItem.value = unwatchedPool[Random.nextInt(unwatchedPool.size)]
            } else {
                _randomPickedItem.value = null
            }
        }
    }

    fun dismissRandomPick() {
        _randomPickedItem.value = null
    }
}
