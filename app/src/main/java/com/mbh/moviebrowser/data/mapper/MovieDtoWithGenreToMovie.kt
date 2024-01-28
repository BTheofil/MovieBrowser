package com.mbh.moviebrowser.data.mapper

import com.mbh.moviebrowser.domain.model.Genre
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.model.dto.MovieDto

class MovieDtoWithGenreToMovie {

    fun convertResultMovieAndDecodeGenres(
        movieDto: MovieDto,
        genreList: List<Genre>
    ): Movie = Movie(
        id = movieDto.id.toLong(),
        title = movieDto.title,
        genres = when (movieDto) {
            is MovieDto.MovieDetails -> {
                movieDto.genres.map { it.id }.joinToString(" ") { genreId ->
                    genreList.find { it.id == genreId }?.name.orEmpty()
                }
            }

            is MovieDto.MovieItem -> {
                movieDto.genre_ids.joinToString(" ") { genreId ->
                    genreList.find { it.id == genreId }?.name.orEmpty()
                }
            }
        },
        overview = movieDto.overview,
        coverUrl = "https://image.tmdb.org/t/p/w500" + movieDto.poster_path,
        rating = movieDto.vote_average.toFloat(),
        isFavorite = false
    )
}