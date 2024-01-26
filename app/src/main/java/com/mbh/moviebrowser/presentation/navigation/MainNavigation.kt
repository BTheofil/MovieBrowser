package com.mbh.moviebrowser.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mbh.moviebrowser.presentation.movieDetails.MovieDetailsScreen
import com.mbh.moviebrowser.presentation.movieList.MovieListScreen

private const val DETAILS_SCREEN = "details"
private const val LIST_SCREEN = "list"

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = LIST_SCREEN) {
        composable(LIST_SCREEN) {
            MovieListScreen(
                onDetailsClicked = {
                    navController.navigate(DETAILS_SCREEN + "/${it}")
                },
            )
        }
        composable(
            DETAILS_SCREEN + "/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.LongType })
        ) {
            MovieDetailsScreen()
        }
    }
}