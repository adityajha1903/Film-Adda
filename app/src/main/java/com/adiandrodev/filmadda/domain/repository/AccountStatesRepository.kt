package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.domain.model.CombinedAccountStates

interface AccountStatesRepository {

    suspend fun getAllAccountStates(accountId: Int, sessionId: String): CombinedAccountStates
    suspend fun getAvailableCountries(): ArrayList<Country>

    suspend fun clearFavMoviesFromCache()
    suspend fun clearFavTvShowsFromCache()
    suspend fun clearRatedMoviesFromCache()
    suspend fun clearRatedTvShowsFromCache()
    suspend fun clearWatchlistMoviesFromCache()
    suspend fun clearWatchlistTvShowsFromCache()
}