package com.sceballosdev.quikstream.data.service

import com.sceballosdev.quikstream.data.models.StreamsResponse
import retrofit2.http.GET

fun interface StreamApi {

    @GET("gopro-interview-lab/example-json/refs/heads/main/takehome-android.json")
    suspend fun getStreams(): StreamsResponse
}