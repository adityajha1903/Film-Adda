package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class AccountDetails(
    val avatar: Avatar?,
    var id: Int?,
    val include_adult: Boolean?,
    var iso_3166_1: String?,
    var username: String?
): Serializable

data class Avatar(
    val gravatar: Gravatar?,
    val tmdb: Tmdb?
): Serializable

data class Gravatar(
    var hash: String?
): Serializable

data class Tmdb(
    var avatar_path: String?
): Serializable