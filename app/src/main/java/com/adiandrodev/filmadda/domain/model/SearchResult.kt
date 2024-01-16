package com.adiandrodev.filmadda.domain.model

import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import java.io.Serializable

data class SearchResult(
    val collections: CollectionList?,
    val movies: MediaList?,
    val tvShows: MediaList?,
    val people: PeopleList?
): Serializable
