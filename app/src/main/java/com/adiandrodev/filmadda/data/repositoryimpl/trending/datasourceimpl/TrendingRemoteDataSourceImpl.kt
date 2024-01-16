package com.adiandrodev.filmadda.data.repositoryimpl.trending.datasourceimpl

import com.adiandrodev.filmadda.data.api.TrendingService
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingRemoteDataSource
import retrofit2.Response

class TrendingRemoteDataSourceImpl(
    private val trendingService: TrendingService,
    private val apiKey: String
): TrendingRemoteDataSource {

    override suspend fun getTrendingMovies(timeWindow: String): Response<MediaList> {
        return trendingService.getTrendingMovies(timeWindow = timeWindow, apiKey = apiKey)
    }

    override suspend fun getTrendingTvShows(timeWindow: String): Response<MediaList> {
        return trendingService.getTrendingTvShows(timeWindow = timeWindow, apiKey = apiKey)
    }

    override suspend fun getTrendingPeople(timeWindow: String): Response<PeopleList> {
        return trendingService.getTrendingPeople(timeWindow = timeWindow, apiKey = apiKey)
    }
}