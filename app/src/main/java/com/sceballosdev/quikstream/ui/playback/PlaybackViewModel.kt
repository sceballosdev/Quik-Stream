package com.sceballosdev.quikstream.ui.playback

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.sceballosdev.quikstream.core.util.Constants.AUTHOR_KEY
import com.sceballosdev.quikstream.core.util.Constants.NAME_KEY
import com.sceballosdev.quikstream.core.util.Constants.URL_KEY
import com.sceballosdev.quikstream.domain.model.Stream
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * ViewModel responsible for providing the [Stream] data and managing the [ExoPlayer] instance
 * for playback. Stream metadata is retrieved from the [SavedStateHandle] arguments.
 *
 * @param context Application context used to build the ExoPlayer.
 * @param savedStateHandle Handle containing navigation arguments for name, author, and URL.
 */
@HiltViewModel
class PlaybackViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val name = savedStateHandle.get<String>(NAME_KEY) ?: error("name missing")
    private val author = savedStateHandle.get<String>(AUTHOR_KEY) ?: error("author missing")
    private val url = savedStateHandle.get<String>(URL_KEY) ?: error("url missing")
    val stream = Stream(name = name, author = author, url = url)

    /**
     * ExoPlayer instance configured to play the stream URL.
     * Prepared and set to play when ready.
     */
    val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(stream.url))
        prepare()
        playWhenReady = true
    }

    /**
     * Releases the ExoPlayer when the ViewModel is cleared to free resources.
     */
    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}