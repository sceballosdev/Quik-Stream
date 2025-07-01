package com.sceballosdev.quikstream.ui.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.sceballosdev.quikstream.core.util.TestTags.ERROR_MESSAGE
import com.sceballosdev.quikstream.core.util.TestTags.FETCH_BUTTON
import com.sceballosdev.quikstream.core.util.TestTags.IDLE_MESSAGE
import com.sceballosdev.quikstream.core.util.TestTags.LOADING_INDICATOR
import com.sceballosdev.quikstream.core.util.TestTags.PLAY_BUTTON
import com.sceballosdev.quikstream.core.util.TestTags.RETRY_BUTTON
import com.sceballosdev.quikstream.core.util.TestTags.streamItemTag
import com.sceballosdev.quikstream.domain.model.Stream
import com.sceballosdev.quikstream.ui.main.MainUiState.Error
import com.sceballosdev.quikstream.ui.main.MainUiState.Idle
import com.sceballosdev.quikstream.ui.main.MainUiState.Loading
import com.sceballosdev.quikstream.ui.main.MainUiState.Success
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the stateless MainScreen composable.
 * Validates Idle, Loading, Success states using test tags for reliable node selection.
 */
class MainScreenUiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun idleState_showsFetchOnly() {
        composeRule.setContent {
            MainScreen(
                uiState = Idle,
                streams = emptyList(),
                onFetch = {},
                onPlay = {}
            )
        }

        composeRule.onNodeWithTag(FETCH_BUTTON)
            .assertIsDisplayed()
            .assertIsEnabled()

        composeRule.onNodeWithTag(PLAY_BUTTON)
            .assertIsDisplayed()
            .assertIsNotEnabled()

        val idleNodes = composeRule
            .onAllNodesWithTag(IDLE_MESSAGE)
            .fetchSemanticsNodes()

        assertEquals(2, idleNodes.size)
    }

    @Test
    fun loadingState_showsLoadingIndicator() {
        composeRule.setContent {
            MainScreen(
                uiState = Loading,
                streams = emptyList(),
                onFetch = {},
                onPlay = {}
            )
        }

        composeRule.onNodeWithTag(LOADING_INDICATOR)
            .assertIsDisplayed()
    }

    @Test
    fun successState_showsStreamListAndButtons() {
        val sampleStreams = listOf(
            Stream("A", "AuthA", "urlA"),
            Stream("B", "AuthB", "urlB")
        )
        composeRule.setContent {
            MainScreen(
                uiState = Success(sampleStreams),
                streams = sampleStreams,
                onFetch = {},
                onPlay = {}
            )
        }

        composeRule.onNodeWithTag(FETCH_BUTTON)
            .assertIsDisplayed()
            .assertIsNotEnabled()
        composeRule.onNodeWithTag(PLAY_BUTTON)
            .assertIsDisplayed()
            .assertIsEnabled()

        composeRule.onNodeWithTag(streamItemTag(0))
            .assertIsDisplayed()
        composeRule.onNodeWithTag(streamItemTag(1))
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsErrorMessageAndRetryButton() {
        val errorText = "Network failed"
        composeRule.setContent {
            MainScreen(
                uiState = Error(errorText),
                streams = emptyList(),
                onFetch = {},
                onPlay = {}
            )
        }

        composeRule.onNodeWithTag(ERROR_MESSAGE)
            .assertIsDisplayed()
            .assertTextEquals(errorText)
        composeRule.onNodeWithTag(RETRY_BUTTON)
            .assertIsDisplayed()
    }
}