package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.domain.model.TvShowsLists

interface TvShowsListsRepository {

    suspend fun getTvShowsLists(): TvShowsLists?

}