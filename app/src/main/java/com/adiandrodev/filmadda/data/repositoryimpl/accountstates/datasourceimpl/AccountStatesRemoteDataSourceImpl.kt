package com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasourceimpl

import com.adiandrodev.filmadda.data.api.AccountStatesService
import com.adiandrodev.filmadda.data.model.Country
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesRemoteDataSource
import retrofit2.Response

class AccountStatesRemoteDataSourceImpl(
    private val accountStatesService: AccountStatesService,
    private val apiKey: String
): AccountStatesRemoteDataSource {

    override suspend fun getFavouriteMovies(
        accountId: Int,
        sessionId: String
    ): Response<MediaList> {
        return accountStatesService.getFavouriteMovies(
            accountId = accountId,
            sessionId = sessionId,
            apiKey = apiKey
        )
    }

    override suspend fun getFavouriteTvShows(
        accountId: Int,
        sessionId: String
    ): Response<MediaList> {
        return accountStatesService.getFavouriteTvShows(
            accountId = accountId,
            sessionId = sessionId,
            apiKey = apiKey
        )
    }

    override suspend fun getRatedMovies(
        accountId: Int,
        sessionId: String
    ): Response<MediaList> {
        return accountStatesService.getRatedMovies(
            accountId = accountId,
            sessionId = sessionId,
            apiKey = apiKey
        )
    }

    override suspend fun getRatedTvShows(
        accountId: Int,
        sessionId: String
    ): Response<MediaList> {
        return accountStatesService.getRatedTvShows(
            accountId = accountId,
            sessionId = sessionId,
            apiKey = apiKey
        )
    }

    override suspend fun getMoviesInWatchlist(
        accountId: Int,
        sessionId: String
    ): Response<MediaList> {
        return accountStatesService.getMoviesInWatchlist(
            accountId = accountId,
            sessionId = sessionId,
            apiKey = apiKey
        )
    }

    override suspend fun getTvShowsInWatchlist(
        accountId: Int,
        sessionId: String
    ): Response<MediaList> {
        return accountStatesService.getTvShowsInWatchList(
            accountId = accountId,
            sessionId = sessionId,
            apiKey = apiKey
        )
    }

    override suspend fun getAvailableCountries(): Response<ArrayList<Country>> {
        return accountStatesService.getAvailableCountries(apiKey = apiKey)
    }
}