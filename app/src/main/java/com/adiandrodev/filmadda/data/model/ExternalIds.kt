package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class ExternalIds(
    val facebook_id: String?,
    val imdb_id: String?,
    val instagram_id: String?,
    val twitter_id: String?,
    val wikidata_id: String?,
    val youtube_id: String?
): Serializable