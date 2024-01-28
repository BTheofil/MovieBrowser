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
import com.mbh.moviebrowser.common.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MovieDBRepositoryImpl @Inject constructor(
    private val api: MovieDBApiSource,
) : MovieDBRepository {

    private val _movieList = MutableStateFlow<Resource<List<Movie>>>(Resource.Empty())
    override val movieList: StateFlow<Resource<List<Movie>>>
        get() = _movieList

    override suspend fun getPopularMovies() {
        try {
            val genreList = getGenreList()
            val result = api.getPopularMovies()

            if (result.isSuccessful) {
                _movieList.emit(
                    Resource.Success(
                        data = result.body()!!.results.map { resultMovie ->
                            convertResultMovieAndDecodeGenres(resultMovie, genreList)
                        }
                    )
                )

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

    override fun addFavoriteMovie(id: Long) {
        val currentMovies = _movieList.value.data.orEmpty()

        val result = currentMovies.map {
            if (it.id == id){
                it.copy(
                    isFavorite = !it.isFavorite
                )
            } else {
                it
            }
        }

        _movieList.value = Resource.Success(result)
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

    private suspend fun getGenreList(): List<Genre> {
        val result = api.getGenreIds()

        return if (result.isSuccessful) {
            GenresDtoToGenre().invoke(result.body()!!)
        } else {
            throw GenreNetworkCallError()
        }
    }
}