package com.adiandrodev.filmadda.domain.model

import com.adiandrodev.filmadda.data.model.MediaList
import java.io.Serializable

data class CombinedAccountStates(
    val favouriteMovies: MediaList?,
    val favouriteTvShows: MediaList?,
    val ratedMovies: MediaList?,
    val ratedTvShows: MediaList?,
    val watchlistMovies: MediaList?,
    val watchlistTvShows: MediaList?
): Serializable
