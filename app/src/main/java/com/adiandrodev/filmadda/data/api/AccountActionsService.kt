package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.MediaStatesBody
import com.adiandrodev.filmadda.data.model.RateMediaBody
import com.adiandrodev.filmadda.data.model.SuccessStatus
import retrofit2.Response
import retrofit2.http.*

interface AccountActionsService {

    @POST("account/{account_id}/favorite")
    suspend fun addOrRemoveMediaToFav(
        @Path(value = "account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Body mediaFavStatus: MediaStatesBody
    ): Response<SuccessStatus>

    @POST("account/{account_id}/watchlist")
    suspend fun addOrRemoveMediaToWatchList(
        @Path(value = "account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Body mediaWatchlistStatus: MediaStatesBody
    ): Response<SuccessStatus>

    @POST("movie/{movie_id}/rating")
    @Headers("Content-Type: application/json;charset=utf-8")
    suspend fun rateMovie(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Body rateMediaRequest: RateMediaBody
    ): Response<SuccessStatus>

    @DELETE("movie/{movie_id}/rating")
    @Headers("Content-Type: application/json;charset=utf-8")
    suspend fun removeRatingFromMovie(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Response<SuccessStatus>

    @POST("tv/{series_id}/rating")
    @Headers("Content-Type: application/json;charset=utf-8")
    suspend fun rateTvShow(
        @Path("series_id") seriesId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Body rateMediaRequest: RateMediaBody
    ): Response<SuccessStatus>

    @DELETE("tv/{series_id}/rating")
    @Headers("Content-Type: application/json;charset=utf-8")
    suspend fun removeRatingFromTvShow(
        @Path("series_id") seriesId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Response<SuccessStatus>
}