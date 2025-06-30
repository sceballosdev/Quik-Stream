package com.sceballosdev.quikstream.ui.playback

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
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

@Composable
fun PlaybackScreen(
    viewModel: PlaybackViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    val stream = viewModel.stream
    val player = viewModel.player

    if (LocalInspectionMode.current) {
        PlaybackScreenPreviewContent(stream.name, stream.author)
        return
    }

    BackHandler { onBack() }

    PlaybackScreenContent(
        player = player,
        name = stream.name,
        author = stream.author,
        onBack = { onBack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackScreenContent(
    player: ExoPlayer,
    name: String,
    author: String,
    onBack: () -> Unit
) {
    var positionMs by remember { mutableStateOf(value = 0L) }
    var durationMs by remember { mutableStateOf(value = player.duration.coerceAtLeast(minimumValue = 0L)) }

    LaunchedEffect(key1 = player) {
        while (true) {
            positionMs = player.currentPosition
            durationMs = player.duration.coerceAtLeast(minimumValue = 0L)
            delay(timeMillis = 500)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Back"
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
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {
            // PlayerView embed
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
                    .aspectRatio(16f / 9f)
                    .background(MaterialTheme.colorScheme.surface)
            )

            Spacer(Modifier.height(16.dp))

            // Título y autor
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(name, style = MaterialTheme.typography.titleLarge)
                Text(author, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(Modifier.height(8.dp))

            // Slider de progreso
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Slider(
                    value = positionMs.toFloat(),
                    onValueChange = { player.seekTo(it.toLong()) },
                    valueRange = 0f..(durationMs.coerceAtLeast(0L).toFloat()),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(formatTime(positionMs), style = MaterialTheme.typography.labelSmall)
                    Text(formatTime(durationMs), style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Controles de reproducción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { player.shuffleModeEnabled = !player.shuffleModeEnabled }) {
                    Icon(Icons.Default.Shuffle, contentDescription = "Shuffle")
                }
                IconButton(onClick = { player.seekTo((positionMs - 10_000).coerceAtLeast(0L)) }) {
                    Icon(Icons.Default.FastRewind, contentDescription = "Rewind")
                }
                IconButton(onClick = {
                    if (player.isPlaying) player.pause() else player.play()
                }) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        modifier = Modifier.size(36.dp)
                    )
                }
                IconButton(onClick = { player.seekTo((positionMs + 10_000).coerceAtMost(durationMs)) }) {
                    Icon(Icons.Default.FastForward, contentDescription = "Forward")
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms) % 60
    return "%d:%02d".format(minutes, seconds)
}

@Composable
private fun PlaybackScreenPreviewContent(
    title: String,
    author: String
) {
    // UI estática para previews
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Placeholder de video
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(MaterialTheme.colorScheme.surface)
        )
        // Título y autor
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(author, style = MaterialTheme.typography.bodyLarge)
        }
        // Slider de ejemplo
        Slider(
            value = 0.3f,
            onValueChange = {},
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
        // Controles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(Icons.Default.Shuffle, contentDescription = "Shuffle")
            Icon(Icons.Default.FastRewind, contentDescription = "Rewind")
            Icon(Icons.Default.PlayArrow, contentDescription = "Play/Pause")
            Icon(Icons.Default.FastForward, contentDescription = "Forward")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaybackLightPreview() {
    QuikStreamTheme(darkTheme = false) {
        PlaybackScreen(
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlaybackDarkPreview() {
    QuikStreamTheme(darkTheme = true) {
        PlaybackScreen(
            onBack = {}
        )
    }
}