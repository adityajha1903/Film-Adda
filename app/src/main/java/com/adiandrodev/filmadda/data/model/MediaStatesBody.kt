package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class MediaStatesBody(
    val favorite: Boolean?,
    val media_id: Int?,
    val media_type: String?,
    val watchlist: Boolean?
): Serializable