package com.mbh.moviebrowser.domain.repository

import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.common.Resource
import kotlinx.coroutines.flow.StateFlow

interface MovieDBRepository {

    val movieList: StateFlow<Resource<List<Movie>>>

    suspend fun getPopularMovies()

    fun addFavoriteMovie(id: Long)
}