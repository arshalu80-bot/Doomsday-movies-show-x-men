package com.example.data.model

data class MediaEntry(val title: String, val isShow: Boolean)

object DefaultMediaData {
    // COMPLETE 68 MCU TIMELINE (MOVIES + SERIES + SPECIALS)
    val rawMCU = listOf(
        MediaEntry("Iron Man (2008)", false),
        MediaEntry("The Incredible Hulk (2008)", false),
        MediaEntry("Iron Man 2 (2010)", false),
        MediaEntry("Thor (2011)", false),
        MediaEntry("Captain America: The First Avenger (2011)", false),
        MediaEntry("The Avengers (2012)", false),
        MediaEntry("Iron Man 3 (2013)", false),
        MediaEntry("Thor: The Dark World (2013)", false),
        MediaEntry("Captain America: The Winter Soldier (2014)", false),
        MediaEntry("Guardians of the Galaxy (2014)", false),
        MediaEntry("Avengers: Age of Ultron (2015)", false),
        MediaEntry("Ant-Man (2015)", false),
        MediaEntry("Captain America: Civil War (2016)", false),
        MediaEntry("Doctor Strange (2016)", false),
        MediaEntry("Guardians of the Galaxy Vol. 2 (2017)", false),
        MediaEntry("Spider-Man: Homecoming (2017)", false),
        MediaEntry("Thor: Ragnarok (2017)", false),
        MediaEntry("Black Panther (2018)", false),
        MediaEntry("Avengers: Infinity War (2018)", false),
        MediaEntry("Ant-Man and the Wasp (2018)", false),
        MediaEntry("Captain Marvel (2019)", false),
        MediaEntry("Avengers: Endgame (2019)", false),
        MediaEntry("Avengers: Endgame (Encore Edition) (2019)", false),
        MediaEntry("Spider-Man: Far From Home (2019)", false),
        MediaEntry("WandaVision (TV Series) (2021)", true),
        MediaEntry("The Falcon and the Winter Soldier (TV Series) (2021)", true),
        MediaEntry("Loki (Season 1) (2021)", true),
        MediaEntry("Black Widow (2021)", false),
        MediaEntry("What If...? (Season 1) (2021)", true),
        MediaEntry("Shang-Chi and the Legend of the Ten Rings (2021)", false),
        MediaEntry("Eternals (2021)", false),
        MediaEntry("Hawkeye (TV Series) (2021)", true),
        MediaEntry("Spider-Man: No Way Home (2021)", false),
        MediaEntry("Moon Knight (TV Series) (2022)", true),
        MediaEntry("Doctor Strange in the Multiverse of Madness (2022)", false),
        MediaEntry("Ms. Marvel (TV Series) (2022)", true),
        MediaEntry("Thor: Love and Thunder (2022)", false),
        MediaEntry("I Am Groot (Season 1) (2022)", true),
        MediaEntry("She-Hulk: Attorney at Law (TV Series) (2022)", true),
        MediaEntry("Werewolf by Night (TV Special) (2022)", true),
        MediaEntry("Black Panther: Wakanda Forever (2022)", false),
        MediaEntry("The Guardians of the Galaxy Holiday Special (TV Special) (2022)", true),
        MediaEntry("Ant-Man and the Wasp: Quantumania (2023)", false),
        MediaEntry("Guardians of the Galaxy Vol. 3 (2023)", false),
        MediaEntry("Secret Invasion (TV Series) (2023)", true),
        MediaEntry("I Am Groot (Season 2) (2023)", true),
        MediaEntry("Loki (Season 2) (2023)", true),
        MediaEntry("The Marvels (2023)", false),
        MediaEntry("What If...? (Season 2) (2023)", true),
        MediaEntry("Echo (TV Series) (2024)", true),
        MediaEntry("Deadpool & Wolverine (2024)", false),
        MediaEntry("Agatha All Along (TV Series) (2024)", true),
        MediaEntry("What If...? (Season 3) (2024)", true),
        MediaEntry("Your Friendly Neighborhood Spider-Man (Season 1) (2025)", true),
        MediaEntry("Captain America: Brave New World (2025)", false),
        MediaEntry("Daredevil: Born Again (Season 1) (2025)", true),
        MediaEntry("Thunderbolts* (2025)", false),
        MediaEntry("Ironheart (TV Series) (2025)", true),
        MediaEntry("The Fantastic Four: First Steps (2025)", false),
        MediaEntry("Eyes of Wakanda (TV Series) (2025)", true),
        MediaEntry("Marvel Zombies (TV Series) (2025)", true),
        MediaEntry("Wonder Man (TV Series) (2026)", true),
        MediaEntry("Daredevil: Born Again (Season 2) (2026)", true),
        MediaEntry("The Punisher: One Last Kill (TV Special) (2026)", true),
        MediaEntry("Spider-Man: Brand New Day (2026)", false),
        MediaEntry("VisionQuest (TV Series) (2026)", true),
        MediaEntry("Your Friendly Neighborhood Spider-Man (Season 2) (2026)", true),
        MediaEntry("Avengers: Doomsday (2026)", false)
    )

    // EXACT 14 PURE X-MEN MOVIES
    val rawXMen = listOf(
        "X-Men (2000)", "X2: X-Men United (2003)", "X-Men: The Last Stand (2006)",
        "X-Men Origins: Wolverine (2009)", "X-Men: First Class (2011)", "The Wolverine (2013)",
        "X-Men: Days of Future Past (2014)", "Deadpool (2016)", "X-Men: Apocalypse (2016)",
        "Logan (2017)", "Deadpool 2 (2018)", "Dark Phoenix (2019)", "The New Mutants (2020)",
        "Deadpool & Wolverine (2024)"
    )

    // 5 NON MCU SPIDER-MAN MOVIES
    val rawSpidey = listOf(
        "Spider-Man (2002)",
        "Spider-Man 2 (2004)",
        "Spider-Man 3 (2007)",
        "The Amazing Spider-Man (2012)",
        "The Amazing Spider-Man 2 (2014)"
    )

    fun extractYear(title: String): Int {
        val yearRegex = "(?:\\(|\\b)(19\\d{2}|20\\d{2})(?:\\)|\\b)".toRegex()
        val match = yearRegex.findAll(title).lastOrNull()
        return match?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 2026
    }

    fun extractType(title: String, isShow: Boolean): String {
        return when {
            title.contains("Special", ignoreCase = true) -> "Special"
            title.contains("Animated", ignoreCase = true) -> "Animated"
            isShow || title.contains("Season", ignoreCase = true) || title.contains("Series", ignoreCase = true) -> "Series"
            else -> "Movie"
        }
    }

    fun generateInitialItems(): List<MediaItem> {
        val list = mutableListOf<MediaItem>()
        rawMCU.forEachIndexed { index, entry ->
            list.add(
                MediaItem(
                    id = "mcu_$index",
                    title = entry.title,
                    category = "mcu",
                    originalIndex = index + 1,
                    watched = false,
                    releaseYear = extractYear(entry.title),
                    typeTag = extractType(entry.title, entry.isShow),
                    isShow = entry.isShow
                )
            )
        }
        rawXMen.forEachIndexed { index, title ->
            val isShow = title.contains("Season", ignoreCase = true)
            list.add(
                MediaItem(
                    id = "xmen_$index",
                    title = title,
                    category = "xmen",
                    originalIndex = index + 1,
                    watched = false,
                    releaseYear = extractYear(title),
                    typeTag = extractType(title, isShow),
                    isShow = isShow
                )
            )
        }
        rawSpidey.forEachIndexed { index, title ->
            list.add(
                MediaItem(
                    id = "spidey_$index",
                    title = title,
                    category = "spidey",
                    originalIndex = index + 1,
                    watched = false,
                    releaseYear = extractYear(title),
                    typeTag = "Movie",
                    isShow = false
                )
            )
        }
        return list
    }
}
