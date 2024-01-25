package com.mbh.moviebrowser.domain.model

data class ResultMovie(
    val genre_ids: List<Int>,
    val id: Int,
    val overview: String,
    val poster_path: String,
    val title: String,
    val vote_average: Double
)
