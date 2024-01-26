package com.mbh.moviebrowser.presentation.movieList.components

//noinspection SuspiciousImport
import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.mbh.moviebrowser.domain.model.Movie

@Composable
fun MovieListItem(
    movie: Movie,
    onMovieItemClicked: (id: Long) -> Unit,
) {
    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onMovieItemClicked(movie.id)
            },
    ) {
        Box {
            AsyncImage(
                model = movie.coverUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(80.dp)
                    .zIndex(1.0f),
            )
            val image = if (movie.isFavorite) {
                painterResource(id = R.drawable.btn_star_big_on)
            } else {
                painterResource(id = R.drawable.btn_star_big_off)
            }
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .padding(all = 4.dp)
                    .zIndex(2.0f)
                    .align(Alignment.TopEnd),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.genres,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = movie.rating / 10.0f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}