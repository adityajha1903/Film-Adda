package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class SuccessStatus(
    val failure: Boolean?,
    val session_id: String?,
    val status_code: Int?,
    val status_message: String?,
    val success: Boolean?
): Serializable