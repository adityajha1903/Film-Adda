package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.CollectionList
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("search/collection")
    suspend fun searchCollection(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String,
        @Query("api_key") apiKey: String
    ): Response<CollectionList>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("region") region: String,
        @Query("api_key") apiKey: String
    ): Response<MediaList>

    @GET("search/person")
    suspend fun searchPerson(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): Response<PeopleList>

    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String
    ): Response<MediaList>
}