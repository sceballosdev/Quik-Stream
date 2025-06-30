package com.sceballosdev.quikstream.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamResponse(
    val name: String,
    val author: String,
    val url: String
)

@Serializable
data class StreamsResponse(
    @SerialName(value = "videos")
    val videos: List<StreamResponse>
)