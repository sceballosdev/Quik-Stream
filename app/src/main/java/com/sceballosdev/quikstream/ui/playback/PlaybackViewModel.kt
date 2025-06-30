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
 * 	3.	State lifting
 * 	•	Mantén el Player y su PlayerView en un ViewModel (PlaybackViewModel) si necesitas sobrevivir a recreaciones de configuración o persistir posición.
 *
 *
 * Fase 3: Robustez & Manejo de Errores
 *
 * Objetivo: Cubrir casos como timeout, sin Internet, errores JSON o URL inválida.
 * 	1.	Interceptor de red
 * 	•	Agrega un interceptor en OkHttp para simular fallos o medir latencia.
 * 	2.	Error States en UI
 * 	•	En MainUiState.Error, muestra un diálogo o un Snackbar informativo con “Revisar conexión” y un icono.
 * 	3.	Retry / Offline Cache
 * 	•	Implementa un simple cache en memoria (o en DataStore/Room si quieres un extra) para mantener la última lista de streams.
 * 	•	En absence de red, muestra esa caché con un “Cached data” banner.
 */

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val name = savedStateHandle.get<String>(NAME_KEY) ?: error("name missing")
    private val author = savedStateHandle.get<String>(AUTHOR_KEY) ?: error("author missing")
    private val url = savedStateHandle.get<String>(URL_KEY) ?: error("url missing")
    val stream = Stream(name = name, author = author, url = url)

    val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(stream.url))
        prepare()
        playWhenReady = true
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}