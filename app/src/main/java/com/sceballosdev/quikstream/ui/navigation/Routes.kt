package com.sceballosdev.quikstream.ui.navigation

import com.sceballosdev.quikstream.core.util.Constants.AUTHOR_KEY
import com.sceballosdev.quikstream.core.util.Constants.NAME_KEY
import com.sceballosdev.quikstream.core.util.Constants.URL_KEY

object Routes {
    const val Main = "main"
    const val PlaybackBase = "playback"
    const val Playback = "$PlaybackBase/{${NAME_KEY}}/{${AUTHOR_KEY}}/{${URL_KEY}}"
}