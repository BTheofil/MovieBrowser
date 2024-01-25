package com.mbh.moviebrowser.domain.repository

import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.util.Resource

interface MovieDBRepository {

    suspend fun getPopularMovies(): Resource<List<Movie>>

    suspend fun getMovieById(): Resource<Movie>
}