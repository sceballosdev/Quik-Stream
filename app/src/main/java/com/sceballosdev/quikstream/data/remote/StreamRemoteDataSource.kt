package com.sceballosdev.quikstream.data.remote

import com.sceballosdev.quikstream.data.models.StreamsResponse
import com.sceballosdev.quikstream.data.service.StreamApi
import javax.inject.Inject

/**
 * Data source responsible for fetching video stream metadata from QuikStream's remote API.
 *
 * This class uses the [StreamApi] Retrofit interface to execute a network call
 * against the GitHub-hosted JSON endpoint (configured in ¨NetworkModule¨).
 * It returns the raw [StreamsResponse], which contains the list of streams as obtained
 * from the remote "videos" array. Downstream layers (e.g., ¨StreamRepositoryImpl¨)
 * handle mapping these DTOs into domain models and error handling.
 *
 * @property api injected Retrofit service providing the GET request definition.
 *
 * @constructor Instantiated by Hilt in the SingletonComponent scope.
 */
class StreamRemoteDataSource @Inject constructor(
    private val api: StreamApi
) {
    /**
     * Fetches the list of streams from QuikStream's GitHub JSON.
     *
     * Delegates to [StreamApi.getStreams], which issues a GET request to:
     * "/gopro-interview-lab/example-json/refs/heads/main/takehome-android.json"
     * The returned JSON is parsed by Kotlinx Serialization into [StreamsResponse].
     *
     * @return the raw [StreamsResponse] containing the API's "videos" entries.
     * @throws retrofit2.HttpException if the HTTP response is unsuccessful.
     * @throws java.io.IOException for network connectivity issues.
     */
    suspend fun getStreams(): StreamsResponse = api.getStreams()
}