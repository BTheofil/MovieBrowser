package com.mbh.moviebrowser.domain.model.dto

data class GenresDto(
    val genres: List<GenreDto>
)

data class GenreDto(
    val id: Int,
    val name: String
)