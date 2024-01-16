package com.adiandrodev.filmadda.domain.model

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import java.io.Serializable

data class Trending(
    val trendingMovies: MediaList?,
    val trendingTvShows: MediaList?,
    val trendingPeople: PeopleList?
): Serializable
