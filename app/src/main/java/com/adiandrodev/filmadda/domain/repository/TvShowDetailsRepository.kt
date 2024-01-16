package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.domain.model.TvDetails

interface TvShowDetailsRepository {

    suspend fun getTvShowDetails(seriesId: Int): TvDetails?
    suspend fun clearTvShowFromCache(seriesId: Int)
}