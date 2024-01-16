package com.adiandrodev.filmadda.domain.model

import com.adiandrodev.filmadda.data.model.MediaList
import java.io.Serializable

data class MoviesLists(
   val nowPlaying: MediaList?,
   val popular: MediaList?,
   val topRated: MediaList?,
   val upcoming: MediaList?
): Serializable

data class TvShowsLists(
   val airingToday: MediaList?,
   val onTheAir: MediaList?,
   val popular: MediaList?,
   val topRated: MediaList?
): Serializable