package com.adiandrodev.filmadda.data.repositoryimpl.tv

import com.adiandrodev.filmadda.data.model.Images
import com.adiandrodev.filmadda.data.model.Videos
import com.adiandrodev.filmadda.data.model.WatchProviders
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowRemoteDataSource
import com.adiandrodev.filmadda.domain.model.TvDetails
import com.adiandrodev.filmadda.domain.repository.TvShowDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TvShowDetailsRepositoryImpl(
    private val tvShowCacheDataSource: TvShowCacheDataSource,
    private val tvShowRemoteDataSource: TvShowRemoteDataSource
): TvShowDetailsRepository {

    override suspend fun getTvShowDetails(seriesId: Int): TvDetails {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val deferredMovie = CoroutineScope(Dispatchers.IO).async {
                var tvShow = tvShowCacheDataSource.getTvShowDetailsFromCache(seriesId)
                if (tvShow == null) {
                    val response = tvShowRemoteDataSource.getTvShowDetails(seriesId)
                    tvShow = response.body()
                    tvShow?.let {
                        tvShowCacheDataSource.saveTvShowDetailsToCache(seriesId, tvShow)
                    }
                }
                tvShow
            }
            val deferredImages = CoroutineScope(Dispatchers.IO).async {
                getTvShowImages(seriesId)
            }
            val deferredVideos = CoroutineScope(Dispatchers.IO).async {
                getTvShowVideos(seriesId)
            }
            val deferredWatchProviders = CoroutineScope(Dispatchers.IO).async {
                getTvShowWatchProviders(seriesId)
            }
            return@async TvDetails(
                deferredMovie.await(),
                deferredImages.await(),
                deferredVideos.await(),
                deferredWatchProviders.await()
            )
        }
        return deferred.await()
    }

    private suspend fun getTvShowWatchProviders(seriesId: Int): WatchProviders? {
        var watchProviders = tvShowCacheDataSource.getTvShowWatchProvidersFromCache(seriesId)
        if (watchProviders == null) {
            val response = tvShowRemoteDataSource.getTvShowWatchProviders(seriesId)
            watchProviders = response.body()
            watchProviders?.let {
                tvShowCacheDataSource.saveTvShowWatchProvidersToCache(seriesId, watchProviders)
            }
        }
        return watchProviders
    }

    private suspend fun getTvShowImages(seriesId: Int): Images? {
        var images = tvShowCacheDataSource.getTvShowImagesFromCache(seriesId)
        if (images == null) {
            val response = tvShowRemoteDataSource.getTvShowImages(seriesId)
            images = response.body()
            images?.let {
                tvShowCacheDataSource.saveTvShowImagesToCache(seriesId, images)
            }
        }
        return images
    }

    private suspend fun getTvShowVideos(seriesId: Int): Videos? {
        var videos = tvShowCacheDataSource.getTvShowVideosFromCache(seriesId)
        if (videos == null) {
            val response = tvShowRemoteDataSource.getTvShowVideos(seriesId)
            videos = response.body()
            videos?.let {
                tvShowCacheDataSource.saveTvShowVideosToCache(seriesId, videos)
            }
        }
        return videos
    }

    override suspend fun clearTvShowFromCache(seriesId: Int) {
        tvShowCacheDataSource.clearTvShowFromCache(seriesId)
    }
}