package com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList

interface TrendingCacheDataSource {

    suspend fun getTrendingMoviesFromCache(timeWindow: String): MediaList?
    suspend fun saveTrendingMoviesToCache(timeWindow: String, trendingMovies: MediaList?)

    suspend fun getTrendingTvShowFromCache(timeWindow: String): MediaList?
    suspend fun saveTrendingTvShowToCache(timeWindow: String, trendingTvShows: MediaList?)

    suspend fun getTrendingPeopleFromCache(timeWindow: String): PeopleList?
    suspend fun saveTrendingPeopleToCache(timeWindow: String, trendingPeople: PeopleList?)
}