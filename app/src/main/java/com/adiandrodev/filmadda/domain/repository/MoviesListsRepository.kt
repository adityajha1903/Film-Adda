package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.domain.model.MoviesLists

interface MoviesListsRepository {

    suspend fun getMoviesLists(region: String): MoviesLists?
}