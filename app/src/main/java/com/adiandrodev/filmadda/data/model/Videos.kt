package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class Videos(
    val id: Int?,
    val results: List<Video?>?
): Serializable

data class Video(
    val id: String?,
    val key: String?,
    val name: String?,
    val official: Boolean?,
    val published_at: String?,
    val site: String?,
    val size: Int?,
    val type: String?
): Serializable