package com.mbh.moviebrowser.presentation.movieDetails

import androidx.lifecycle.SavedStateHandle
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
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieDBRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    data class UiState(
        val isLoading: Boolean = false,
        val movie: Movie? = null,
        val errorMessage: String = ""
    )

    init {
        viewModelScope.launch {
            val movieId: Long = checkNotNull(savedStateHandle["movieId"])
            when (val movie = repository.getMovieById(movieId)) {
                is Resource.Error -> _uiState.update {
                    it.copy(
                        errorMessage = movie.message!!
                    )
                }

                is Resource.Success -> _uiState.update {
                    it.copy(
                        movie = movie.data
                    )
                }
            }

        }
    }

    sealed class OnEvent {
        object FavoriteButtonClick : OnEvent()
    }

    fun onEvent(event: OnEvent) {
        when (event) {
            OnEvent.FavoriteButtonClick -> {

            }
        }
    }
}
