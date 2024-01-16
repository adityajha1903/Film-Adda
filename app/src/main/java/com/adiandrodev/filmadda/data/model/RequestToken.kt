package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class RequestToken(
    val expires_at: String?,
    val request_token: String?,
    val success: Boolean?
): Serializable