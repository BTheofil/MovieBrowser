package com.mbh.moviebrowser.presentation.movieList

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mbh.moviebrowser.domain.model.Movie
import com.mbh.moviebrowser.presentation.movieList.components.MovieListItem

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    onDetailsClicked: (Movie) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    MovieListScreenUI(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun MovieListScreenUI(
    uiState: MovieListViewModel.UiState,
    onEvent: (MovieListViewModel.OnEvent) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(uiState.movies) { item ->
            MovieListItem(
                movie = item,
                onMovieItemClicked = {
                    onEvent(MovieListViewModel.OnEvent.OnMovieItemClick(it))
                },
            )
        }
    }
}
