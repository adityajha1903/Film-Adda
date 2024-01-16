package com.adiandrodev.filmadda.data.api

import com.adiandrodev.filmadda.data.model.AccountDetails
import com.adiandrodev.filmadda.data.model.RequestToken
import com.adiandrodev.filmadda.data.model.SuccessStatus
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @GET("authentication/token/new")
    suspend fun getRequestToken(
        @Query("api_key") apiKey: String
    ): Response<RequestToken>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String,
        @Body requestToken: RequestToken
    ): Response<SuccessStatus>

    @DELETE("authentication/session")
    suspend fun deleteSession(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<SuccessStatus>

    @GET("account")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Response<AccountDetails>
}