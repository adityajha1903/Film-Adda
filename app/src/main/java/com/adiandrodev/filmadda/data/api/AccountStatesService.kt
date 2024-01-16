package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountStatesService {

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavouriteMovies(
        @Path("account_id") accountId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("account/{account_id}/favorite/tv")
    suspend fun getFavouriteTvShows(
        @Path("account_id") accountId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = "created_at.asc",
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("account/{account_id}/rated/tv")
    suspend fun getRatedTvShows(
        @Path("account_id") accountId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = "created_at.asc",
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getMoviesInWatchlist(
        @Path("account_id") accountId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = "created_at.asc",
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("account/{account_id}/watchlist/tv")
    suspend fun getTvShowsInWatchList(
        @Path("account_id") accountId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("session_id") sessionId: String,
        @Query("sort_by") sortBy: String = "created_at.asc",
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("configuration/countries")
    suspend fun getAvailableCountries(
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<ArrayList<Country>>
}