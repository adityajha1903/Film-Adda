package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class CastOrCrewIn(
    val backdrop_path: String?,
    val character: String?,
    val credit_id: String?,
    val genre_ids: List<Int?>?,
    val id: Int?,
    val order: Int?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val release_date: String?,
    val title: String?,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?,
    val episode_count: Int?,
    val first_air_date: String?,
    val name: String?,
    val origin_country: List<String?>?,
    val department: String?,
    val job: String?
): Serializable