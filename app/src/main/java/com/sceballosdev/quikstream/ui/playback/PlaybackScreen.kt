package com.sceballosdev.quikstream.ui.playback

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.sceballosdev.quikstream.R
import com.sceballosdev.quikstream.ui.theme.QuikStreamTheme
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay

/**
 * Obtains an [ExoPlayer] and stream metadata from a [PlaybackViewModel], handles back navigation,
 * and delegates UI rendering to the overloaded [PlaybackScreen].
 *
 * @param viewModel Hilt-provided ViewModel that holds the ExoPlayer instance and current Stream.
 * @param onBack Callback invoked when the user presses the back button.
 */
@Composable
fun PlaybackScreen(
    viewModel: PlaybackViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    PlaybackScreen(
        player = viewModel.player,
        name = viewModel.stream.name,
        author = viewModel.stream.author,
        onBack = { onBack() }
    )
}

/**
 * Renders the full playback UI, including:
 *  - A video surface via [PlayerView]
 *  - Stream title and author
 *  - A progress [Slider] with current and total time
 *  - Rewind, Play/Pause (with dynamic icon and circular background), and Fast-Forward controls
 *
 * @param player The [ExoPlayer] instance driving media playback.
 * @param name The display name/title of the current stream.
 * @param author The author or source of the current stream.
 * @param onBack Callback invoked when the user presses the navigation (back) icon.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackScreen(
    player: ExoPlayer,
    name: String,
    author: String,
    onBack: () -> Unit
) {
    var positionMs by rememberSaveable { mutableStateOf(value = 0L) }
    var durationMs by rememberSaveable { mutableStateOf(value = player.duration.coerceAtLeast(minimumValue = 0L)) }
    var isPlaying by rememberSaveable { mutableStateOf(value = player.playWhenReady) }

    LaunchedEffect(key1 = player) {
        while (true) {
            positionMs = player.currentPosition
            durationMs = player.duration.coerceAtLeast(minimumValue = 0L)
            delay(timeMillis = 500)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(size = 44.dp)
                        )
                    }
                },

                title = {
                    Text(
                        text = stringResource(id = R.string.quik_label),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(height = 32.dp))

            // PlayerView
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        useController = false
                        this.player = player
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 16f / 9f)
                    .background(color = MaterialTheme.colorScheme.surface)
            )

            Spacer(Modifier.height(height = 16.dp))

            // Name and author of the stream
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = author,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(Modifier.height(height = 8.dp))

            // Slider to control the playback
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Slider(
                    value = positionMs.toFloat(),
                    onValueChange = { player.seekTo(it.toLong()) },
                    valueRange = 0f..(durationMs.coerceAtLeast(minimumValue = 0L).toFloat()),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        activeTrackColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(positionMs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = formatTime(durationMs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.height(height = 16.dp))

            // Buttons to control the playback
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind button
                IconButton(
                    onClick = {
                        player.seekTo((positionMs - 10_000).coerceAtLeast(minimumValue = 0L))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = "Rewind",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(size = 36.dp)
                    )
                }

                // Play/pause button
                val bgColor = if (isSystemInDarkTheme()) Color(0xFF2A2F36) else Color.Black
                Box(
                    modifier = Modifier
                        .size(size = 64.dp)
                        .clip(CircleShape)
                        .background(bgColor)
                        .clickable {
                            if (isPlaying) player.pause() else player.play()
                            isPlaying = !isPlaying
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(size = 44.dp)
                    )
                }

                // Forward button
                IconButton(
                    onClick = {
                        player.seekTo((positionMs + 10_000).coerceAtMost(maximumValue = durationMs))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = "Forward",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(size = 36.dp)
                    )
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
    return "%d:%02d".format(minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun PlaybackLightPreview() {
    QuikStreamTheme(darkTheme = false) {
        PlaybackScreen(
            player = ExoPlayer.Builder(LocalContext.current).build(),
            name = "Test Stream",
            author = "Test Author",
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlaybackDarkPreview() {
    QuikStreamTheme(darkTheme = true) {
        PlaybackScreen(
            player = ExoPlayer.Builder(LocalContext.current).build(),
            name = "Test Stream",
            author = "Test Author",
            onBack = {}
        )
    }
}