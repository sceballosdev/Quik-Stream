package com.sceballosdev.quikstream.domain.usecase

import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.domain.repository.StreamRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetStreamsUseCase @Inject constructor(
    private val streamRepository: StreamRepository
) {

    operator fun invoke(): Flow<Result<List<Stream>>> = streamRepository.getStreams()
}