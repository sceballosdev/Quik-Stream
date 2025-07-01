package com.sceballosdev.quikstream.core.util

/**
 * Collection of test tags used for UI testing of QuikStream's Compose screens.
 *
 * Tags are applied via `Modifier.testTag` to key UI elements, allowing tests to locate
 * and assert on components in a stable, unambiguous way.
 */
object TestTags {
    /** Tag for the "Fetch" button on the main screen. */
    const val FETCH_BUTTON = "fetchButton"

    /** Tag for the "Play" button on the main screen. */
    const val PLAY_BUTTON = "playButton"

    /** Tag for the stream URL text box. */
    const val URL_TEXT = "urlText"

    /** Tag for the idle state message. */
    const val IDLE_MESSAGE = "idleMessage"

    /** Tag for the loading indicator in the main screen. */
    const val LOADING_INDICATOR = "loadingIndicator"

    /** Tag for the error message text in the main screen. */
    const val ERROR_MESSAGE = "errorMessage"

    /** Tag for the retry button in the main screen's error state. */
    const val RETRY_BUTTON = "retryButton"

    /**
     * Generates a tag for a specific stream item in the list by index.
     *
     * @param index position of the stream in the list, zero-based.
     */
    fun streamItemTag(index: Int) = "streamItem_$index"
}