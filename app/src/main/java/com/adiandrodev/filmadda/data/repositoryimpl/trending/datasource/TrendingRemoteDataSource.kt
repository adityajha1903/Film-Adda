package com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import retrofit2.Response

interface TrendingRemoteDataSource {

    suspend fun getTrendingMovies(timeWindow: String): Response<MediaList>
    suspend fun getTrendingTvShows(timeWindow: String): Response<MediaList>
    suspend fun getTrendingPeople(timeWindow: String): Response<PeopleList>
}