package com.sceballosdev.quikstream.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sceballosdev.quikstream.R
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.ui.main.MainUiState.Error
import com.sceballosdev.quikstream.ui.main.MainUiState.Idle
import com.sceballosdev.quikstream.ui.main.MainUiState.Loading
import com.sceballosdev.quikstream.ui.main.MainUiState.Success
import com.sceballosdev.quikstream.ui.theme.BorderGray
import com.sceballosdev.quikstream.ui.theme.ItemBg
import com.sceballosdev.quikstream.ui.theme.QuikBlue
import com.sceballosdev.quikstream.ui.theme.QuikStreamTheme
import com.sceballosdev.quikstream.ui.theme.TextGray

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onPlay: (Stream) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val streams = (uiState as? Success)?.streams.orEmpty()
    MainScreen(
        uiState = uiState,
        streams = streams,
        onFetch = { viewModel.fetchStreams() },
        onPlay = { stream -> onPlay(stream) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: MainUiState,
    streams: List<Stream>,
    onFetch: () -> Unit,
    onPlay: (Stream) -> Unit
) {
    // Local selection state
    var selected by remember { mutableStateOf<Stream?>(value = null) }
    LaunchedEffect(key1 = streams) {
        if (uiState is Success && selected == null && streams.isNotEmpty()) {
            selected = streams.first()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        // Logo “Quik”
        Text(
            text = stringResource(id = R.string.quik_label),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            val fetchEnabled = uiState !is Loading && uiState !is Success
            val playEnabled = uiState is Success && selected != null

            // Fetch Stream
            Button(
                onClick = { onFetch() },
                enabled = fetchEnabled,
                modifier = Modifier
                    .weight(weight = 1f)
                    .height(height = 40.dp),
                shape = RoundedCornerShape(size = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (fetchEnabled) QuikBlue else BorderGray,
                    contentColor = if (fetchEnabled) Color.White else TextGray
                )
            ) {
                Text(
                    text = stringResource(id = R.string.fetch_stream_label),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            // Play Stream
            Button(
                onClick = { selected?.let { onPlay(it) } },
                enabled = playEnabled,
                modifier = Modifier
                    .weight(weight = 1f)
                    .height(height = 40.dp),
                shape = RoundedCornerShape(size = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (playEnabled) QuikBlue else BorderGray,
                    contentColor = if (playEnabled) Color.White else TextGray
                )
            ) {
                Text(
                    text = stringResource(id = R.string.play_stream_label),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        // Stream URL input
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 56.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (selected != null) {
                Text(
                    text = selected?.url.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else {
                Text(
                    text = stringResource(id = R.string.stream_url_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Main content: loading, list or error
        Crossfade(targetState = uiState) { state ->
            when (state) {
                is Idle -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.idle_message),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = streams) { stream ->
                            Card(
                                onClick = {
                                    selected = stream
                                    onPlay(stream)
                                },
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(all = 12.dp)
                                ) {
                                    Box(
                                        Modifier
                                            .size(size = 64.dp)
                                            .clip(shape = RoundedCornerShape(size = 12.dp))
                                            .background(color = ItemBg)
                                    )
                                    Spacer(Modifier.width(width = 12.dp))
                                    Column(Modifier.weight(weight = 1f)) {
                                        Text(
                                            text = stream.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = stream.author,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(size = 24.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                is Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        Button(
                            onClick = onFetch,
                            modifier = Modifier.height(height = 40.dp),
                            shape = RoundedCornerShape(size = 12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = QuikBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.retry_label),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "MainScreen • Light",
    showBackground = true,
    widthDp = 360, heightDp = 640
)
@Composable
fun MainScreenLightPreview() {
    QuikStreamTheme(darkTheme = false) {
        MainScreen(
            /*uiState = Success(
                listOf(
                    Stream("Beach Time", "Alicia Parker", "http://ex.com/1.mp4"),
                    Stream("Thrill Seekers", "John Smith", "http://ex.com/2.mp4")
                )
            ),*/
            uiState = Idle,
            //uiState = Error("Something went wrong"),
            //uiState = Loading,
            streams = listOf(
                Stream("Beach Time", "Alicia Parker", "http://ex.com/1.mp4"),
                Stream("Thrill Seekers", "John Smith", "http://ex.com/2.mp4")
            ),
            onFetch = {},
            onPlay = {}
        )
    }
}

@Preview(
    name = "MainScreen • Dark",
    widthDp = 360, heightDp = 640
)
@Composable
fun MainScreenDarkPreview() {
    QuikStreamTheme(darkTheme = true) {
        MainScreen(
            uiState = Success(
                listOf(
                    Stream("Beach Time", "Alicia Parker", "http://ex.com/1.mp4"),
                    Stream("Thrill Seekers", "John Smith", "http://ex.com/2.mp4")
                )
            ),
            //uiState = Idle,
            //uiState = Error("Something went wrong"),
            //uiState = Loading,
            streams = listOf(
                Stream("Beach Time", "Alicia Parker", "http://ex.com/1.mp4"),
                Stream("Thrill Seekers", "John Smith", "http://ex.com/2.mp4")
            ),
            onFetch = {},
            onPlay = {}
        )
    }
}
