package com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource

import com.adiandrodev.filmadda.data.model.*
import retrofit2.Response

interface TvShowRemoteDataSource {

    suspend fun getTvShowsAiringToday(): Response<MediaList>
    suspend fun getTvShowsOnTheAir(): Response<MediaList>
    suspend fun getPopularTvShows(): Response<MediaList>
    suspend fun getTopRatedTvShows(): Response<MediaList>
    suspend fun getTvShowDetails(seriesId: Int): Response<Media>
    suspend fun getTvShowWatchProviders(seriesId: Int): Response<WatchProviders>
    suspend fun getTvShowImages(seriesId: Int): Response<Images>
    suspend fun getTvShowVideos(seriesId: Int): Response<Videos>
    suspend fun getTvShowSeasonDetails(seriesId: Int, seasonNumber: Int): Response<Season>
}