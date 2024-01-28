package com.mbh.moviebrowser.presentation.movieDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.repository.MovieDBRepository
import com.mbh.moviebrowser.common.Resource
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

            repository.movieList.collect { resource ->
                when (resource) {
                    is Resource.Empty -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                movie = resource.data?.first {
                                    it.id == movieId
                                }
                            )
                        }
                    }
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
                repository.addFavoriteMovie(uiState.value.movie!!.id)
            }
        }
    }
}
