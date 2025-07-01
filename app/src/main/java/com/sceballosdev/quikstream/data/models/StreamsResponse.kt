package com.sceballosdev.quikstream.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data model representing a single video stream entry as returned by the GoPro API.
 *
 * This class is used by Retrofit with Kotlinx Serialization to parse each JSON element
 * under the "videos" array from the raw GitHub endpoint:
 * https://raw.githubusercontent.com/gopro-interview-lab/example-json/refs/heads/main/takehome-android.json
 *
 * @property name   the display title of the video stream, shown in the app's list screen.
 * @property author the uploader or creator of the video stream, displayed under the title.
 * @property url    the direct URL for playback, passed to the media player component.
 */
@Serializable
data class StreamResponse(
    val name: String,
    val author: String,
    val url: String
)

/**
 * Wrapper for the list of [StreamResponse] objects returned by the QuikStream streams endpoint.
 *
 * The JSON response has a top-level "videos" field containing the list of streams.
 * This class maps that field into Kotlin objects for further mapping to domain models.
 *
 * @property videos the raw list of streams as provided by the API.
 */
@Serializable
data class StreamsResponse(
    @SerialName(value = "videos")
    val videos: List<StreamResponse>
)