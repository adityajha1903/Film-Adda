package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class People(
    val biography: String?,
    val birthday: String?,
    val deathday: String?,
    val external_ids: ExternalIds?,
    val gender: Int?,
    val homepage: String?,
    val id: Int?,
    val images: Images?,
    val imdb_id: String?,
    val known_for_department: String?,
    val movie_credits: PersonCredits?,
    val name: String?,
    val place_of_birth: String?,
    val profile_path: String?,
    val tv_credits: PersonCredits?,
    val known_for: ArrayList<Media?>?,
    val cast_id: Int?,
    val character: String?,
    val credit_id: String?,
    val order: Int?,
    val original_name: String?,
    val department: String?,
    val job: String?,
    override val backdrop_path: String?
): Serializable, ResultItem