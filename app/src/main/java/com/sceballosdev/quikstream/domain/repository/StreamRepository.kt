package com.sceballosdev.quikstream.domain.repository

import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.domain.model.Stream
import kotlinx.coroutines.flow.Flow

fun interface StreamRepository {
    fun getStreams(): Flow<Result<List<Stream>>>
}