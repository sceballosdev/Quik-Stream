package com.sceballosdev.quikstream.core.util

/**
 * QuikStream-wide constants for JSON field names and Intent/Bundle keys.
 *
 * These values align with the Streams API schema hosted on GitHub:
 *  - "name": the display title of each video stream
 *  - "author": the creator or uploader of the stream content
 *  - "url": the direct media URL used for playback in the app
 *
 * They are also reused throughout the app when passing stream data
 * between Android components via Intent extras or Bundles.
 */
object Constants {
    const val NAME_KEY = "name"
    const val AUTHOR_KEY = "author"
    const val URL_KEY = "url"
}