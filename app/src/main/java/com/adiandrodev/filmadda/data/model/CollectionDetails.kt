package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class CollectionDetails(
    override val backdrop_path: String?,
    val id: Int?,
    val name: String?,
    val original_language: String?,
    val overview: String?,
    val parts: ArrayList<Media?>?,
    val poster_path: String?
): Serializable, ResultItem