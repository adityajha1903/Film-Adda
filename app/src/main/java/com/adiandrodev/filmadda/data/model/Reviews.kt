package com.adiandrodev.filmadda.data.model

import java.io.Serializable

data class Reviews(
    val id: Int?,
    val page: Int?,
    val results: List<Review?>?,
    val total_pages: Int?,
    val total_results: Int?
): Serializable

data class Review(
    val author: String?,
    val author_details: AuthorDetails?,
    val content: String?,
    val created_at: String?,
    val id: String?,
    val updated_at: String?,
    val url: String?
): Serializable

data class AuthorDetails(
    val avatar_path: String?,
    val name: String?,
    val rating: Int?,
    val username: String?
): Serializable