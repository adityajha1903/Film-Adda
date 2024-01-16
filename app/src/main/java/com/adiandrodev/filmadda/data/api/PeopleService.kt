package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.People
import com.adiandrodev.filmadda.data.model.PeopleList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleService {

    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Response<PeopleList>

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId: Int,
        @Query("append_to_response") appendToResponse: String = "external_ids,images,movie_credits,tv_credits",
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): Response<People>
}