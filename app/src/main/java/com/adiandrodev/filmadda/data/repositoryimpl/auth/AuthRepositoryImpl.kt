package com.adiandrodev.filmadda.data.repositoryimpl.auth

import com.adiandrodev.filmadda.data.api.AuthService
import com.adiandrodev.filmadda.data.model.AccountDetails
import com.adiandrodev.filmadda.data.model.RequestToken
import com.adiandrodev.filmadda.data.model.SuccessStatus
import com.adiandrodev.filmadda.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val apiKey: String
): AuthRepository {

    override suspend fun getRequestToken(): RequestToken? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async authService.getRequestToken(apiKey).body()
        }
        return deferred.await()
    }

    override suspend fun createSession(requestToken: String): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val token = RequestToken(null, requestToken, null)
            return@async authService.createSession(apiKey, token).body()
        }
        return deferred.await()
    }

    override suspend fun deleteSession(sessionId: String): SuccessStatus? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async authService.deleteSession(apiKey, sessionId).body()
        }
        return deferred.await()
    }

    override suspend fun getAccountDetails(sessionId: String): AccountDetails? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async authService.getAccountDetails(sessionId, apiKey).body()
        }
        return deferred.await()
    }
}