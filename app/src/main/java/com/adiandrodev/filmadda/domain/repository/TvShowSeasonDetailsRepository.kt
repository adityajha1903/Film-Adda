package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.Season

interface TvShowSeasonDetailsRepository {

    suspend fun getTvShowSeasonDetails(seriesId: Int, seasonNumber: Int): Season?
}