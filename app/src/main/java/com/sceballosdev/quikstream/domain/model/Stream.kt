package com.sceballosdev.quikstream.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stream(
    val name: String,
    val author: String,
    val url: String
) : Parcelable