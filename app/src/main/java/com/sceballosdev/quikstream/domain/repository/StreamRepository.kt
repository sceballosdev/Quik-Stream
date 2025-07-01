package com.sceballosdev.quikstream.domain.repository

import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.domain.model.Stream
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for retrieving video streams in QuikStream.
 *
 * Implementations must provide a [Flow] of [Result] wrapping a list of [Stream] domain models.
 * Consumers (e.g., use cases, ViewModels) collect this flow to drive UI state,
 * receiving:
 *  - [Result.Loading] when the fetch operation starts,
 *  - [Result.Success] with the mapped domain data on completion,
 *  - [Result.Error] if any exception occurs during fetching or mapping.
 *
 * The concrete implementation "StreamRepositoryImpl" fetches from the remote API
 * on "Dispatchers.IO" and maps network DTOs to [Stream] models.
 */
fun interface StreamRepository {

    /**
     * Retrieves the list of available video streams.
     *
     * @return a cold [Flow] emitting [Result] states for loading, success, and error.
     */
    fun getStreams(): Flow<Result<List<Stream>>>
}