package com.sceballosdev.quikstream.data.repository


import app.cash.turbine.test
import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.data.models.StreamResponse
import com.sceballosdev.quikstream.data.models.StreamsResponse
import com.sceballosdev.quikstream.data.remote.StreamRemoteDataSource
import com.sceballosdev.quikstream.domain.model.Stream
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class StreamRepositoryImplTest {

    private val remoteDataSource: StreamRemoteDataSource = mockk()
    private val repository = StreamRepositoryImpl(remoteDataSource)

    @Test
    fun `getStreams emits Loading then Success when remote call succeeds`() = runTest {
        // Given
        val dto = StreamResponse(name = "TestName", author = "TestAuthor", url = "TestUrl")
        val response = StreamsResponse(videos = listOf(dto))
        coEvery { remoteDataSource.getStreams() } returns response

        // When
        repository.getStreams().test {
            // Then
            assert(awaitItem() is Result.Loading)

            val success = awaitItem() as Result.Success<List<Stream>>
            assertEquals(
                listOf(Stream(name = "TestName", author = "TestAuthor", url = "TestUrl")),
                success.data
            )

            awaitComplete()
        }
    }

    @Test
    fun `getStreams emits Loading then Error when remote call throws`() = runTest {
        // Given
        val ioException = IOException("Network failure")
        coEvery { remoteDataSource.getStreams() } throws ioException

        // When
        repository.getStreams().test {
            // Then
            assert(awaitItem() is Result.Loading)

            val errorResult = awaitItem() as Result.Error
            assertSame(ioException, errorResult.exception)

            awaitComplete()
        }
    }
}