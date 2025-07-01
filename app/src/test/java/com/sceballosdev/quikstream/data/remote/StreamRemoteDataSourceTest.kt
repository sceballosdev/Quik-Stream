package com.sceballosdev.quikstream.data.remote

import com.sceballosdev.quikstream.data.models.StreamsResponse
import com.sceballosdev.quikstream.data.service.StreamApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class StreamRemoteDataSourceTest {

    private val api: StreamApi = mockk()
    private lateinit var dataSource: StreamRemoteDataSource

    @Before
    fun setup() {
        dataSource = StreamRemoteDataSource(api)
    }

    @Test
    fun `getStreams should call api and return its result`() = runTest {
        // Given
        val dummy = StreamsResponse(videos = emptyList())
        coEvery { api.getStreams() } returns dummy

        // When
        val result = dataSource.getStreams()

        // Then
        assert(result === dummy)
        coVerify(exactly = 1) { api.getStreams() }
    }

    @Test(expected = RuntimeException::class)
    fun `getStreams should propagate exception from api`() = runTest {
        // Given
        coEvery { api.getStreams() } throws RuntimeException("fail")

        // When / Then
        dataSource.getStreams()
    }
}