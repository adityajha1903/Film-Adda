package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class Images(
    val backdrops: List<Image?>?,
    val id: Int?,
    val logos: List<Image?>?,
    val posters: List<Image?>?,
    val profiles: List<Image?>?
): Serializable

data class Image(
    val aspect_ratio: Double?,
    val file_path: String?,
    val height: Int?,
    val width: Int?
): Serializable