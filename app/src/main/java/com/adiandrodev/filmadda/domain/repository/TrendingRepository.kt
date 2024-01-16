package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.domain.model.Trending

interface TrendingRepository {

    suspend fun getTrendingData(): Trending?
    suspend fun getTrendingMovies(timeWindow: String): MediaList?
    suspend fun getTrendingTvShows(timeWindow: String): MediaList?
    suspend fun getTrendingPeople(timeWindow: String): PeopleList?
}