package com.mbh.moviebrowser.data.repository

import android.util.Log
import com.mbh.moviebrowser.data.data_source.MovieDBApiSource
import com.mbh.moviebrowser.data.error.GenreNetworkCallError
import com.mbh.moviebrowser.data.error.MovieNetworkCallError
import com.mbh.moviebrowser.data.mapper.GenresDtoToGenre
import com.mbh.moviebrowser.data.mapper.MovieDtoToMovie
import com.mbh.moviebrowser.domain.model.Genre
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.model.ResultMovie
import com.mbh.moviebrowser.domain.repository.MovieDBRepository
import com.mbh.moviebrowser.domain.util.Resource
import javax.inject.Inject

class MovieDBRepositoryImpl @Inject constructor(
    private val api: MovieDBApiSource
) : MovieDBRepository {

    override suspend fun getPopularMovies(): Resource<List<Movie>> {
        return try {
            val genreList = getGenreList()
            val resultList = getResultMovieList()

            val mappedMovies = resultList.map { resultMovie ->
                val genreListNames = mutableListOf<String>()

                resultMovie.genre_ids.forEach {
                    genreList.forEach { genre ->
                        if (it == genre.id) {
                            genreListNames.add(genre.name)
                        }
                    }
                }

                Movie(
                    id = resultMovie.id.toLong(),
                    title = resultMovie.title,
                    genres = genreListNames,
                    overview = resultMovie.overview,
                    coverUrl = resultMovie.poster_path,
                    rating = resultMovie.vote_average.toFloat(),
                    isFavorite = false
                )

            }

            Resource.Success(
                data = mappedMovies
            )
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

    override suspend fun getMovieById(): Resource<Movie> {
        TODO("Not yet implemented")
    }

    private suspend fun getResultMovieList(): List<ResultMovie> {
        val result = api.getPopularMovies()

        return if (result.isSuccessful) {
            MovieDtoToMovie().invoke(result.body()!!)
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