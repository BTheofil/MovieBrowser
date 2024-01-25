package com.mbh.moviebrowser.data.data_source

import com.mbh.moviebrowser.BuildConfig.API_KEY
import com.mbh.moviebrowser.domain.model.dto.GenresDto
import com.mbh.moviebrowser.domain.model.dto.MovieDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieDBApiSource {

    @GET("movie/now_playing")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey : String = API_KEY,
    ): Response<MovieDto>

    @GET("genre/movie/list")
    suspend fun getGenreIds(
        @Query("api_key") apiKey : String = API_KEY,
    ): Response<GenresDto>

    @GET("find/")
    suspend fun getMovieById(
        @Query("api_key") apiKey : String = API_KEY,
        @Header("id") movieId: String
    ): Response<MovieDto>
}