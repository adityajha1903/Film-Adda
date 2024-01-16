package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.domain.model.MovieDetails

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): MovieDetails?
    suspend fun clearMovieDetailsFromCache(movieId: Int)
}