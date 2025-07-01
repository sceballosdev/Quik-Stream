package com.sceballosdev.quikstream.core.util

/**
 * Represents the outcome of a stream-fetching operation in QuikStream.
 * This sealed class models the UI and domain states for asynchronous data loading:
 *
 * - [Loading]: the streams request is in progress.
 * - [Success]: the streams request completed successfully with a data payload.
 * - [Error]: the streams request failed with an exception.
 *
 * Consumers of this Result can update UI state or trigger side effects based on these cases.
 *
 * @param T the type of data carried by [Success].
 */
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}