package com.sceballosdev.quikstream.domain.usecase

import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.domain.repository.StreamRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case that provides a stream of [Result] containing a list of [Stream] models.
 *
 * Delegates fetching and mapping logic to [StreamRepository], emitting:
 *  - [Result.Loading] when the request starts,
 *  - [Result.Success] with data on success,
 *  - [Result.Error] if an exception occurs.
 *
 * Designed for collection by ViewModels to update UI state in QuikStream’s main screen.
 *
 * @param streamRepository repository responsible for data retrieval.
 */
class GetStreamsUseCase @Inject constructor(
    private val streamRepository: StreamRepository
) {

    /**
     * Executes the use case to fetch and map video streams.
     *
     * @return a cold [Flow] emitting [Result] states of a list of [Stream].
     */
    operator fun invoke(): Flow<Result<List<Stream>>> = streamRepository.getStreams()
}