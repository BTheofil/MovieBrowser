package com.mbh.moviebrowser.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mbh.moviebrowser.presentation.movieDetails.MovieDetailsScreen
import com.mbh.moviebrowser.presentation.movieDetails.MovieDetailsViewModel
import com.mbh.moviebrowser.presentation.movieList.MovieListScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            MovieListScreen(
                onDetailsClicked = {
                    navController.navigate("details")
                },
            )
        }
        composable("details") {
            MovieDetailsScreen()
        }
    }
}