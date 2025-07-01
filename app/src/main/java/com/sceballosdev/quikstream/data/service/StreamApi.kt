package com.sceballosdev.quikstream.data.service

import com.sceballosdev.quikstream.data.models.StreamsResponse
import retrofit2.http.GET

fun interface StreamApi {

    /**
     * Fetches the current list of video streams for QuikStream.
     *
     * The returned JSON structure is deserialized into [StreamsResponse],
     * containing a list of ¨StreamResponse¨ entries under the "videos" field.
     *
     * @return [StreamsResponse] parsed from the API response.
     * @throws retrofit2.HttpException if the HTTP status code is not successful.
     * @throws java.io.IOException for network failures or timeouts.
     */
    @GET("gopro-interview-lab/example-json/refs/heads/main/takehome-android.json")
    suspend fun getStreams(): StreamsResponse
}