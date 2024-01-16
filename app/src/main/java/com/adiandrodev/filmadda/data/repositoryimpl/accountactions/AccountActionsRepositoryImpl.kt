package com.adiandrodev.filmadda.data.repositoryimpl.accountactions

import com.adiandrodev.filmadda.data.api.AccountActionsService
import com.adiandrodev.filmadda.data.model.MediaStatesBody
import com.adiandrodev.filmadda.data.model.RateMediaBody
import com.adiandrodev.filmadda.data.model.SuccessStatus
import com.adiandrodev.filmadda.domain.repository.AccountActionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class AccountActionsRepositoryImpl(
    private val accountActionsService: AccountActionsService,
    private val apiKey: String
): AccountActionsRepository {

    override suspend fun addOrRemoveMediaToFav(
        accountId: Int,
        sessionId: String,
        favorite: Boolean,
        mediaId: Int,
        mediaType: String
    ): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async accountActionsService.addOrRemoveMediaToFav(
                accountId = accountId,
                sessionId = sessionId,
                apiKey = apiKey,
                mediaFavStatus = MediaStatesBody(
                    favorite = favorite,
                    media_id = mediaId,
                    media_type = mediaType,
                    watchlist = null
                )
            ).body()
        }

        return deferred.await()
    }

    override suspend fun addOrRemoveMediaToWatchList(
        accountId: Int,
        sessionId: String,
        watchlist: Boolean,
        mediaId: Int,
        mediaType: String
    ): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async accountActionsService.addOrRemoveMediaToWatchList(
                accountId = accountId,
                sessionId = sessionId,
                apiKey = apiKey,
                mediaWatchlistStatus = MediaStatesBody(
                    favorite = null,
                    media_id = mediaId,
                    media_type = mediaType,
                    watchlist = watchlist
                )
            ).body()
        }
        return deferred.await()
    }

    override suspend fun rateMovie(
        movieId: Int,
        sessionId: String,
        rating: Double
    ): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async accountActionsService.rateMovie(
                movieId = movieId,
                sessionId = sessionId,
                apiKey = apiKey,
                rateMediaRequest = RateMediaBody(rating)
            ).body()
        }
        return deferred.await()
    }

    override suspend fun removeRatingFromMovie(
        movieId: Int,
        sessionId: String
    ): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async accountActionsService.removeRatingFromMovie(
                movieId = movieId,
                sessionId = sessionId,
                apiKey = apiKey
            ).body()
        }
        return deferred.await()
    }

    override suspend fun rateTvShow(
        seriesId: Int,
        sessionId: String,
        rating: Double
    ): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async accountActionsService.rateTvShow(
                seriesId = seriesId,
                sessionId = sessionId,
                apiKey = apiKey,
                rateMediaRequest = RateMediaBody(rating)
            ).body()
        }
        return deferred.await()
    }

    override suspend fun removeRatingFromTvShow(
        seriesId: Int,
        sessionId: String
    ): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async accountActionsService.removeRatingFromTvShow(
                seriesId = seriesId,
                sessionId = sessionId,
                apiKey = apiKey
            ).body()
        }
        return deferred.await()
    }
}