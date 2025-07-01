package com.sceballosdev.quikstream.domain.usecase

import app.cash.turbine.test
import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.domain.repository.StreamRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetStreamsUseCaseTest {

    private val repository: StreamRepository = mockk()
    private val useCase = GetStreamsUseCase(repository)

    @Test
    fun `invoke should delegate to repository and emit its values`() = runTest {
        // Given
        val streams = listOf(
            Stream(name = "A", author = "AuthA", url = "UrlA"),
            Stream(name = "B", author = "AuthB", url = "UrlB")
        )
        val upstream = flowOf(
            Result.Loading,
            Result.Success(streams)
        )
        every { repository.getStreams() } returns upstream

        // When
        val flowUnderTest = useCase()

        // Then
        flowUnderTest.test {
            assert(awaitItem() is Result.Loading)
            val success = awaitItem() as Result.Success<List<Stream>>
            assert(success.data == streams)
            awaitComplete()
        }
        verify(exactly = 1) { repository.getStreams() }
    }
}