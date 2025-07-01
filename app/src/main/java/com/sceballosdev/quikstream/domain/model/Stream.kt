package com.sceballosdev.quikstream.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model representing a single video stream in the QuikStream app.
 *
 * Instances of this class are used throughout the presentation layer to display
 * stream information and to initiate playback. Implements [Parcelable] to allow easy
 * transmission between Android components (e.g., Activities, Fragments).
 *
 * @property name   the title of the stream shown in the main list and header.
 * @property author the uploader or creator name displayed under the title.
 * @property url    the direct media URL passed to the player for playback.
 */
@Parcelize
data class Stream(
    val name: String,
    val author: String,
    val url: String
) : Parcelable