package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = "account_states,credits,external_ids,recommendations",
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<Media>

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<WatchProviders>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<Videos>

    @GET("movie/{movie_id}/images")
    suspend fun getMovieImages(
        @Path("movie_id") movieId: Int,
        @Query("include_image_language") includeImageLanguage: String = "en",
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<Images>

    @GET("collection/{collection_id}")
    suspend fun getCollectionDetails(
        @Path("collection_id") collectionId: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<CollectionDetails>
}