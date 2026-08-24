package com.example.data.model

object DefaultMediaData {
    val rawMCU = listOf(
        "Iron Man (2008)", "The Incredible Hulk (2008)", "Iron Man 2 (2010)", "Thor (2011)",
        "Captain America: The First Avenger (2011)", "The Avengers (2012)", "Iron Man 3 (2013)",
        "Thor: The Dark World (2013)", "Captain America: The Winter Soldier (2014)",
        "Guardians of the Galaxy (2014)", "Avengers: Age of Ultron (2015)", "Ant-Man (2015)",
        "Captain America: Civil War (2016)", "Doctor Strange (2016)", "Guardians of the Galaxy Vol. 2 (2017)",
        "Spider-Man: Homecoming (2017)", "Thor: Ragnarok (2017)", "Black Panther (2018)",
        "Avengers: Infinity War (2018)", "Ant-Man and the Wasp (2018)", "Captain Marvel (2019)",
        "Avengers: Endgame (2019)", "Spider-Man: Far From Home (2019)", "WandaVision (TV Series) (2021)",
        "The Falcon and the Winter Soldier (TV Series) (2021)", "Loki (TV Series - Season 1) (2021)",
        "Black Widow (2021)", "What If...? (TV Series - Season 1) (2021)",
        "Shang-Chi and the Legend of the Ten Rings (2021)", "Eternals (2021)", "Hawkeye (TV Series) (2021)",
        "Moon Knight (TV Series) (2022)", "Doctor Strange in the Multiverse of Madness (2022)",
        "Ms. Marvel (TV Series) (2022)", "Thor: Love and Thunder (2022)", "I Am Groot (TV Series - Season 1) (2022)",
        "She-Hulk: Attorney at Law (TV Series) (2022)", "Werewolf by Night (TV Special) (2022)",
        "Black Panther: Wakanda Forever (2022)", "The Guardians of the Galaxy Holiday Special (TV Special) (2022)",
        "Ant-Man and the Wasp: Quantumania (2023)", "Guardians of the Galaxy Vol. 3 (2023)",
        "Secret Invasion (TV Series) (2023)", "Loki (TV Series - Season 2) (2023)", "The Marvels (2023)",
        "What If...? (TV Series - Season 2) (2023)", "Echo (TV Series) (2024)", "Deadpool & Wolverine (2024)",
        "Agatha All Along (TV Series) (2024)", "What If...? (TV Series - Season 3) (2024)",
        "Captain America: Brave New World (2025)", "Daredevil: Born Again (TV Series - Season 1) (2025)",
        "Thunderbolts* (2025)", "Ironheart (TV Series) (2025)", "The Fantastic Four: First Steps (2025)",
        "Eyes of Wakanda (TV Series - Animated) (2025)", "Marvel Zombies (TV Series - Animated) (2025)",
        "Wonder Man (TV Series) (2026)", "Daredevil: Born Again (TV Series - Season 2) (2026)",
        "X-Men '97 (TV Series - Season 2) (2026)", "The Punisher: One Last Kill (TV Special) (2026)",
        "Spider-Man: Brand New Day (2026)", "VisionQuest (TV Series) (2026)",
        "Your Friendly Neighborhood Spider-Man (TV Series - Season 2) (2026)", "Avengers: Doomsday (2026)"
    )

    val rawXMen = listOf(
        "X-Men (2000)", "X2: X-Men United (2003)", "X-Men: The Last Stand (2006)",
        "X-Men Origins: Wolverine (2009)", "X-Men: First Class (2011)", "The Wolverine (2013)",
        "X-Men: Days of Future Past (2014)", "Deadpool (2016)", "X-Men: Apocalypse (2016)",
        "Logan (2017)", "Deadpool 2 (2018)", "Dark Phoenix (2019)", "The New Mutants (2020)",
        "Deadpool & Wolverine (2024)"
    )

    val rawSeries = listOf(
        "WandaVision (2021)", "The Falcon and the Winter Soldier (2021)", "Loki (Season 1) (2021)",
        "What If...? (Season 1) (2021)", "Hawkeye (2021)", "Moon Knight (2022)", "Ms. Marvel (2022)",
        "I Am Groot (Season 1) (2022)", "She-Hulk: Attorney at Law (2022)", "Werewolf by Night (Special) (2022)",
        "The Guardians of the Galaxy Holiday Special (Special) (2022)", "Secret Invasion (2023)",
        "Loki (Season 2) (2023)", "What If...? (Season 2) (2023)", "Echo (2024)", "Agatha All Along (2024)",
        "What If...? (Season 3) (2024)", "Daredevil: Born Again (Season 1) (2025)", "Ironheart (2025)",
        "Eyes of Wakanda (Animated) (2025)", "Marvel Zombies (Animated) (2025)", "Wonder Man (2026)",
        "Daredevil: Born Again (Season 2) (2026)", "X-Men '97 (Season 2) (2026)", "VisionQuest (2026)",
        "Your Friendly Neighborhood Spider-Man (Season 2) (2026)"
    )

    fun extractYear(title: String): Int {
        val yearRegex = "\\((\\d{4})\\)".toRegex()
        val match = yearRegex.findAll(title).lastOrNull()
        return match?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 2026
    }

    fun extractType(title: String, category: String): String {
        return when {
            title.contains("Special", ignoreCase = true) -> "Special"
            title.contains("Animated", ignoreCase = true) -> "Animated"
            title.contains("TV Series", ignoreCase = true) || category == "series" -> "TV Series"
            else -> "Movie"
        }
    }

    fun generateInitialItems(): List<MediaItem> {
        val list = mutableListOf<MediaItem>()
        rawMCU.forEachIndexed { index, title ->
            list.add(
                MediaItem(
                    id = "mcu_$index",
                    title = title,
                    category = "mcu",
                    originalIndex = index + 1,
                    watched = false,
                    releaseYear = extractYear(title),
                    typeTag = extractType(title, "mcu")
                )
            )
        }
        rawXMen.forEachIndexed { index, title ->
            list.add(
                MediaItem(
                    id = "xmen_$index",
                    title = title,
                    category = "xmen",
                    originalIndex = index + 1,
                    watched = false,
                    releaseYear = extractYear(title),
                    typeTag = extractType(title, "xmen")
                )
            )
        }
        rawSeries.forEachIndexed { index, title ->
            list.add(
                MediaItem(
                    id = "series_$index",
                    title = title,
                    category = "series",
                    originalIndex = index + 1,
                    watched = false,
                    releaseYear = extractYear(title),
                    typeTag = extractType(title, "series")
                )
            )
        }
        return list
    }
}
