package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.AccountDetails
import com.adiandrodev.filmadda.data.model.RequestToken
import com.adiandrodev.filmadda.data.model.SuccessStatus

interface AuthRepository {

    suspend fun getRequestToken(): RequestToken?
    suspend fun createSession(requestToken: String): SuccessStatus?
    suspend fun deleteSession(sessionId: String): SuccessStatus?
    suspend fun getAccountDetails(sessionId: String): AccountDetails?
}