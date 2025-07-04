package com.sceballosdev.quikstream.ui.navigation

import android.net.Uri
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sceballosdev.quikstream.core.util.Constants.AUTHOR_KEY
import com.sceballosdev.quikstream.core.util.Constants.NAME_KEY
import com.sceballosdev.quikstream.core.util.Constants.URL_KEY
import com.sceballosdev.quikstream.ui.main.MainScreen
import com.sceballosdev.quikstream.ui.navigation.Routes.Main
import com.sceballosdev.quikstream.ui.navigation.Routes.Playback
import com.sceballosdev.quikstream.ui.navigation.Routes.PlaybackBase
import com.sceballosdev.quikstream.ui.playback.PlaybackScreen

@Composable
fun QuikStreamNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Main,
        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 150)) },
        exitTransition = { fadeOut(animationSpec = tween(durationMillis = 100)) },
        popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 150)) },
        popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 100)) }
    ) {
        composable(route = Main) {
            MainScreen(
                onPlay = { stream ->
                    val nameArg = Uri.encode(stream.name)
                    val authorArg = Uri.encode(stream.author)
                    val urlArg = Uri.encode(stream.url)

                    navController.navigate(route = "$PlaybackBase/$nameArg/$authorArg/$urlArg") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Playback,
            arguments = listOf(
                navArgument(name = NAME_KEY) { type = NavType.StringType },
                navArgument(name = AUTHOR_KEY) { type = NavType.StringType },
                navArgument(name = URL_KEY) { type = NavType.StringType }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 150))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth / 3 },
                    animationSpec = tween(durationMillis = 250)
                ) + fadeOut(animationSpec = tween(durationMillis = 100))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth },
                    animationSpec = tween(durationMillis = 300)
                ) + fadeIn(animationSpec = tween(durationMillis = 150))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 250)
                ) + fadeOut(animationSpec = tween(durationMillis = 100))
            }
        ) {
            PlaybackScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}