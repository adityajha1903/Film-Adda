package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendingService {

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("trending/tv/{time_window}")
    suspend fun getTrendingTvShows(
        @Path("time_window") timeWindow: String,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("trending/person/{time_window}")
    suspend fun getTrendingPeople(
        @Path("time_window") timeWindow: String,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<PeopleList>
}