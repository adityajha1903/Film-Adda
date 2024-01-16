package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.SuccessStatus

interface AccountActionsRepository {

    suspend fun addOrRemoveMediaToFav(
        accountId: Int,
        sessionId: String,
        favorite: Boolean,
        mediaId: Int,
        mediaType: String
    ): SuccessStatus?

    suspend fun addOrRemoveMediaToWatchList(
        accountId: Int,
        sessionId: String,
        watchlist: Boolean,
        mediaId: Int,
        mediaType: String
    ): SuccessStatus?

    suspend fun rateMovie(
        movieId: Int,
        sessionId: String,
        rating: Double
    ): SuccessStatus?

    suspend fun removeRatingFromMovie(
        movieId: Int,
        sessionId: String
    ): SuccessStatus?

    suspend fun rateTvShow(
        seriesId: Int,
        sessionId: String,
        rating: Double
    ): SuccessStatus?

    suspend fun removeRatingFromTvShow(
        seriesId: Int,
        sessionId: String
    ): SuccessStatus?
}