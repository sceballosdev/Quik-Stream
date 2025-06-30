package com.sceballosdev.quikstream.data.remote

import com.sceballosdev.quikstream.data.models.StreamsResponse
import com.sceballosdev.quikstream.data.service.StreamApi
import javax.inject.Inject

class StreamRemoteDataSource @Inject constructor(
    private val api: StreamApi
) {
    suspend fun getStreams(): StreamsResponse = api.getStreams()
}