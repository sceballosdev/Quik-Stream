package com.sceballosdev.quikstream.ui.main

import app.cash.turbine.test
import com.sceballosdev.quikstream.core.util.Result
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.domain.usecase.GetStreamsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Test
    fun `fetchStreams emits Idle then Loading then Success when useCase succeeds`() = runTest {
        // Given
        val streams = listOf(
            Stream(name = "Name1", author = "Author1", url = "Url1"),
            Stream(name = "Name2", author = "Author2", url = "Url2")
        )
        val fakeFlow = flowOf(
            Result.Loading,
            Result.Success(streams)
        )
        val useCase = mockk<GetStreamsUseCase>()
        every { useCase() } returns fakeFlow

        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        val viewModel = MainViewModel(useCase)

        // When / Then
        viewModel.uiState.test {
            assertEquals(MainUiState.Idle, awaitItem())

            viewModel.fetchStreams()
            testScheduler.advanceUntilIdle()

            assertEquals(MainUiState.Loading, awaitItem())
            assertEquals(MainUiState.Success(streams), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        Dispatchers.resetMain()
    }

    @Test
    fun `fetchStreams emits Idle then Loading then Error when useCase fails`() = runTest {
        // Given
        val ex = RuntimeException("Oops")
        val fakeFlow = flowOf<Result<List<Stream>>>(
            Result.Loading,
            Result.Error(ex)
        )
        val useCase = mockk<GetStreamsUseCase>()
        every { useCase() } returns fakeFlow

        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        val viewModel = MainViewModel(useCase)

        // When / Then
        viewModel.uiState.test {
            assertEquals(MainUiState.Idle, awaitItem())

            viewModel.fetchStreams()
            testScheduler.advanceUntilIdle()

            assertEquals(MainUiState.Loading, awaitItem())
            assertEquals("Oops", (awaitItem() as MainUiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }

        Dispatchers.resetMain()
    }
}