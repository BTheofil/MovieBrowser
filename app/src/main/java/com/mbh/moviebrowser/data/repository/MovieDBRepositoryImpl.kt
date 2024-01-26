package com.mbh.moviebrowser.data.repository

import android.util.Log
import com.mbh.moviebrowser.data.data_source.MovieDBApiSource
import com.mbh.moviebrowser.data.error.GenreNetworkCallError
import com.mbh.moviebrowser.data.error.MovieNetworkCallError
import com.mbh.moviebrowser.data.mapper.GenresDtoToGenre
import com.mbh.moviebrowser.domain.model.Genre
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.model.dto.MovieDto
import com.mbh.moviebrowser.domain.repository.MovieDBRepository
import com.mbh.moviebrowser.domain.util.Resource
import javax.inject.Inject

class MovieDBRepositoryImpl @Inject constructor(
    private val api: MovieDBApiSource
) : MovieDBRepository {

    override suspend fun getPopularMovies(): Resource<List<Movie>> {
        return try {
            val genreList = getGenreList()
            val result = api.getPopularMovies()

            if (result.isSuccessful) {
                Resource.Success(
                    data = result.body()!!.results.map { resultMovie ->
                        convertResultMovieAndDecodeGenres(resultMovie, genreList)
                    }
                )
            } else {
                Resource.Error(
                    message = "Unexpected error"
                )
            }
        } catch (e: GenreNetworkCallError) {
            Log.e("Movie genre repository", e.message.toString())
            Resource.Error(
                message = "Genre error: " + e.message.toString()
            )
        } catch (e: MovieNetworkCallError) {
            Log.e("Movie result repository", e.message.toString())
            Resource.Error(
                message = "Result error: " + e.message.toString()
            )
        } catch (e: Exception) {
            Log.e("Movie repository", e.message.toString())
            Resource.Error(
                message = "Repository error: " + e.message.toString()
            )
        }
    }

    override suspend fun getMovieById(id: Long): Resource<Movie> {
        return try {
            val genreList = getGenreList()
            val movieDetailsDto = getMovieResultById(id)

            Resource.Success(
                data = convertResultMovieAndDecodeGenres(movieDetailsDto, genreList)
            )
        } catch (e: Exception) {
            Log.e("Movie id repository", e.message.toString())
            Resource.Error(
                message = "Repository error: " + e.message.toString()
            )
        }
    }

    private fun convertResultMovieAndDecodeGenres(
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

    private suspend fun getMovieResultById(id: Long): MovieDto.MovieDetails {
        val result = api.getMovieById(movieId = id)

        return if (result.isSuccessful) {
            MovieDto.MovieDetails(
                genres = result.body()!!.genres,
                id = result.body()!!.id,
                overview = result.body()!!.overview,
                poster_path = result.body()!!.poster_path,
                title = result.body()!!.title,
                vote_average = result.body()!!.vote_average
            )
        } else {
            throw MovieNetworkCallError()
        }
    }

    private suspend fun getGenreList(): List<Genre> {
        val result = api.getGenreIds()

        return if (result.isSuccessful) {
            GenresDtoToGenre().invoke(result.body()!!)
        } else {
            throw GenreNetworkCallError()
        }
    }
}