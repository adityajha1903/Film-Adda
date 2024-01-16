package com.adiandrodev.filmadda.data.repositoryimpl.tv.datasourceimpl

import com.adiandrodev.filmadda.data.api.TvShowService
import com.adiandrodev.filmadda.data.model.*
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

class TvShowRemoteDataSourceImpl(
    private val tvShowService: TvShowService,
    private val apiKey: String
): TvShowRemoteDataSource {

    override suspend fun getTvShowsAiringToday(): Response<MediaList> {
        return tvShowService.getTvShowsAiringToday(apiKey = apiKey)
    }

    override suspend fun getTvShowsOnTheAir(): Response<MediaList> {
        return tvShowService.getTvShowsOnTheAir(apiKey = apiKey)
    }

    override suspend fun getPopularTvShows(): Response<MediaList> {
        return tvShowService.getPopularTvShows(apiKey = apiKey)
    }

    override suspend fun getTopRatedTvShows(): Response<MediaList> {
        return tvShowService.getTopRatedTvShows(apiKey = apiKey)
    }

    override suspend fun getTvShowDetails(seriesId: Int): Response<Media> {
        return tvShowService.getTvShowDetails(seriesId = seriesId, apiKey = apiKey)
    }

    override suspend fun getTvShowWatchProviders(seriesId: Int): Response<WatchProviders> {
        return tvShowService.getTvShowWatchProviders(seriesId, apiKey)
    }

    override suspend fun getTvShowImages(seriesId: Int): Response<Images> {
        return tvShowService.getTvShowImages(seriesId = seriesId, apiKey = apiKey)
    }

    override suspend fun getTvShowVideos(seriesId: Int): Response<Videos> {
        return tvShowService.getTvShowVideos(seriesId = seriesId, apiKey = apiKey)
    }

    override suspend fun getTvShowSeasonDetails(seriesId: Int, seasonNumber: Int): Response<Season> {
        val def = CoroutineScope(Dispatchers.Default).async {
            return@async tvShowService.getTvShowSeasonDetails(seriesId = seriesId, seasonNumber = seasonNumber, apiKey = apiKey)
        }
        return def.await()
    }
}