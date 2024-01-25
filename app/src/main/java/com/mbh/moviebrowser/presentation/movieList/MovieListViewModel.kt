package com.mbh.moviebrowser.presentation.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.repository.MovieDBRepository
import com.mbh.moviebrowser.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieDBRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val errorMessage: String = ""
    )

    sealed class OnEvent {
        data class OnMovieItemClick(val movieId: Long): OnEvent()
    }

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            when(val result = movieRepository.getPopularMovies()){
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            movies = result.data!!,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message!!,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: OnEvent){
        when(event){
            is OnEvent.OnMovieItemClick -> {

            }
        }
    }
}
