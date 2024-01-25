package com.mbh.moviebrowser.presentation.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.domain.repository.MovieDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieDBRepository
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
            repository.getMovieById()
        }
    }

    sealed class OnEvent(){
        object FavoriteButtonClick: OnEvent()
    }

    fun onEvent(event: OnEvent){
        when(event){
            OnEvent.FavoriteButtonClick -> {

            }
        }
    }
}
