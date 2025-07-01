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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sceballosdev.quikstream.R
import com.sceballosdev.quikstream.core.util.TestTags.ERROR_MESSAGE
import com.sceballosdev.quikstream.core.util.TestTags.FETCH_BUTTON
import com.sceballosdev.quikstream.core.util.TestTags.IDLE_MESSAGE
import com.sceballosdev.quikstream.core.util.TestTags.LOADING_INDICATOR
import com.sceballosdev.quikstream.core.util.TestTags.PLAY_BUTTON
import com.sceballosdev.quikstream.core.util.TestTags.RETRY_BUTTON
import com.sceballosdev.quikstream.core.util.TestTags.URL_TEXT
import com.sceballosdev.quikstream.core.util.TestTags.streamItemTag
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.ui.main.MainUiState.Error
import com.sceballosdev.quikstream.ui.main.MainUiState.Idle
import com.sceballosdev.quikstream.ui.main.MainUiState.Loading
import com.sceballosdev.quikstream.ui.main.MainUiState.Success
import com.sceballosdev.quikstream.ui.theme.BorderGray
import com.sceballosdev.quikstream.ui.theme.QuikBlue
import com.sceballosdev.quikstream.ui.theme.QuikStreamTheme
import com.sceballosdev.quikstream.ui.theme.TextGray

/**
 * Top-level composable that ties QuikStream's main screen UI to its [MainViewModel].
 *
 * This function collects the [MainUiState] from the ViewModel and passes
 * the current state, list of [Stream] models, and callbacks down to the
 * stateless [MainScreen] composable.
 *
 * @param viewModel Hilt-injected ViewModel providing UI state and fetch action.
 * @param onPlay callback invoked when the user clicks Play, receiving the selected [Stream].
 */
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

/**
 * Stateless composable rendering QuikStream's main screen.
 *
 * Renders:
 *  - A header with logo and action buttons (Fetch, Play)
 *  - A URL display box for the selected stream
 *  - A content area that crossfades between Idle, Loading, Success list, or Error states.
 *
 * @param uiState current UI status determining content to show.
 * @param streams list of [Stream] data to display in Success state.
 * @param onFetch callback to trigger stream fetching.
 * @param onPlay callback to initiate playback of a given [Stream].
 */
@Composable
fun MainScreen(
    uiState: MainUiState,
    streams: List<Stream>,
    onFetch: () -> Unit,
    onPlay: (Stream) -> Unit
) {
    var selected by rememberSaveable { mutableStateOf<Stream?>(value = null) }
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
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag(IDLE_MESSAGE)
        )

        Spacer(Modifier.height(height = 8.dp))

        // Buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            val fetchEnabled = uiState !is Loading && uiState !is Success
            val playEnabled = uiState is Success && selected != null

            // Fetch Stream button
            Button(
                onClick = { onFetch() },
                enabled = fetchEnabled,
                modifier = Modifier
                    .weight(weight = 1f)
                    .height(height = 40.dp)
                    .testTag(FETCH_BUTTON),
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

            // Play Stream button
            Button(
                onClick = { selected?.let { onPlay(it) } },
                enabled = playEnabled,
                modifier = Modifier
                    .weight(weight = 1f)
                    .height(height = 40.dp)
                    .testTag(PLAY_BUTTON),
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
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag(URL_TEXT),
                    maxLines = 2
                )
            } else {
                Text(
                    text = stringResource(id = R.string.stream_url_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Main content
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag(IDLE_MESSAGE)
                        )
                    }
                }

                is Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag(LOADING_INDICATOR)
                        )
                    }
                }

                is Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(items = streams) { index, stream ->
                            Card(
                                onClick = {
                                    selected = stream
                                    onPlay(stream)
                                },
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag(streamItemTag(index))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(all = 12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(size = 64.dp)
                                            .clip(shape = RoundedCornerShape(size = 12.dp))
                                            .background(color = MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.OndemandVideo,
                                            contentDescription = "Placeholder",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(size = 32.dp)
                                        )
                                    }
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag(ERROR_MESSAGE)
                        )
                        Spacer(modifier = Modifier.height(height = 8.dp))
                        Button(
                            onClick = onFetch,
                            modifier = Modifier
                                .height(height = 40.dp)
                                .testTag(RETRY_BUTTON),
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
            uiState = Success(
                listOf(
                    Stream("Beach Time", "Alicia Parker", "http://ex.com/1.mp4"),
                    Stream("Thrill Seekers", "John Smith", "http://ex.com/2.mp4")
                )
            ),
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
            streams = listOf(
                Stream("Beach Time", "Alicia Parker", "http://ex.com/1.mp4"),
                Stream("Thrill Seekers", "John Smith", "http://ex.com/2.mp4")
            ),
            onFetch = {},
            onPlay = {}
        )
    }
}
