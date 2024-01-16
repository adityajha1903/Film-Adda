package com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource

import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.data.model.MediaList
import retrofit2.Response

interface AccountStatesRemoteDataSource {

    suspend fun getFavouriteMovies(accountId: Int, sessionId: String): Response<MediaList>
    suspend fun getFavouriteTvShows(accountId: Int, sessionId: String): Response<MediaList>
    suspend fun getRatedMovies(accountId: Int, sessionId: String): Response<MediaList>
    suspend fun getRatedTvShows(accountId: Int, sessionId: String): Response<MediaList>
    suspend fun getMoviesInWatchlist(accountId: Int, sessionId: String): Response<MediaList>
    suspend fun getTvShowsInWatchlist(accountId: Int, sessionId: String): Response<MediaList>
    suspend fun getAvailableCountries(): Response<ArrayList<Country>>
}