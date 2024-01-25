package com.mbh.moviebrowser.presentation.movieDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MovieDetailsScreenUI(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun MovieDetailsScreenUI(
    uiState: MovieDetailsViewModel.UiState,
    onEvent: (MovieDetailsViewModel.OnEvent) -> Unit
) {
    if (uiState.movie == null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = uiState.movie.coverUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(24.dp))
        val image = if (uiState.movie.isFavorite) {
            painterResource(id = android.R.drawable.btn_star_big_on)
        } else {
            painterResource(id = android.R.drawable.btn_star_big_off)
        }
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier.clickable {
                onEvent(MovieDetailsViewModel.OnEvent.FavoriteButtonClick)
            },
        )
        Text(
            text = uiState.movie.title,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = uiState.movie.overview ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
        )
    }
}
