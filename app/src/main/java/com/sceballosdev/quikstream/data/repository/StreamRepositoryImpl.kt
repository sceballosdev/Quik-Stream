package com.sceballosdev.quikstream.data.repository

import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.core.util.Result.Error
import com.sceballosdev.quikstream.core.util.Result.Loading
import com.sceballosdev.quikstream.core.util.Result.Success
import com.sceballosdev.quikstream.data.remote.StreamRemoteDataSource
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.domain.repository.StreamRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class StreamRepositoryImpl @Inject constructor(
    private val remoteDataSource: StreamRemoteDataSource
) : StreamRepository {

    override fun getStreams(): Flow<Result<List<Stream>>> = flow {
        emit(value = Loading)
        try {
            val response = remoteDataSource.getStreams()
            val streams = response.videos.map { dto ->
                Stream(name = dto.name, author = dto.author, url = dto.url)
            }
            emit(value = Success(data = streams))
        } catch (e: Exception) {
            emit(value = Error(exception = e))
        }
    }.flowOn(context = Dispatchers.IO)
}