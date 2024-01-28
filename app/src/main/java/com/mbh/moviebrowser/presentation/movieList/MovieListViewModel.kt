package com.mbh.moviebrowser.presentation.movieList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.repository.MovieDBRepository
import com.mbh.moviebrowser.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    sealed class UiEvent {
        data class OnItemClick(val movieId: Long) : UiEvent()
    }

    sealed class OnEvent {
        data class OnMovieItemClick(val movieId: Long) : OnEvent()
    }

    init {
        viewModelScope.launch {
            movieRepository.getPopularMovies()
        }
        viewModelScope.launch {
            movieRepository.movieList.collect{ resource ->
                when(resource){
                    is Resource.Empty -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                movies = resource.data?: emptyList()
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            is OnEvent.OnMovieItemClick -> viewModelScope.launch {
                _uiEvent.send(UiEvent.OnItemClick(event.movieId))
            }
        }
    }
}
