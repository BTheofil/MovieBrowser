package com.mbh.moviebrowser.domain.model.dto

data class MovieCollectionDto(
    val results: List<MovieDto.MovieItem>,
)

sealed interface MovieDto{

    val id: Int
    val overview: String
    val poster_path: String
    val title: String
    val vote_average: Double

    data class MovieItem(
        override val id: Int,
        override val overview: String,
        override val poster_path: String,
        override val title: String,
        override val vote_average: Double,
        val genre_ids: List<Int>
    ): MovieDto

    data class MovieDetails(
        override val id: Int,
        override val overview: String,
        override val poster_path: String,
        override val title: String,
        override val vote_average: Double,
        val genres: List<GenreDto>
    ): MovieDto
}