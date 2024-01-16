package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowService {

    @GET("tv/airing_today")
    suspend fun getTvShowsAiringToday(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("tv/on_the_air")
    suspend fun getTvShowsOnTheAir(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("tv/{series_id}")
    suspend fun getTvShowDetails(
        @Path("series_id") seriesId: Int,
        @Query("append_to_response") appendToResponse: String = "account_states,credits,external_ids,images,recommendations,watch/providers",
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<Media>

    @GET("tv/{series_id}/watch/providers")
    suspend fun getTvShowWatchProviders(
        @Path("series_id") seriesId: Int,
        @Query("api_key") apiKey: String
    ): Response<WatchProviders>

    @GET("tv/{series_id}/images")
    suspend fun getTvShowImages(
        @Path("series_id") seriesId: Int,
        @Query("include_image_language") includeImageLanguage: String = "en",
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<Images>

    @GET("tv/{series_id}/videos")
    suspend fun getTvShowVideos(
        @Path("series_id") seriesId: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<Videos>

    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getTvShowSeasonDetails(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("append_to_response") appendToResponse: String = "account_states,credits,external_ids,images,videos,watch/providers",
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<Season>
}