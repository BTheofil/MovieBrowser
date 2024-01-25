package com.mbh.moviebrowser.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val genres: List<String>,
    val overview: String?,
    val coverUrl: String?,
    val rating: Float,
    val isFavorite: Boolean,
)
