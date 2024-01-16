package com.adiandrodev.filmadda.data.repositoryimpl.tv.datasourceimpl

import com.adiandrodev.filmadda.data.model.*
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowCacheDataSource

class TvShowCacheDataSourceImpl: TvShowCacheDataSource {

    private var tvShowsAiringToday: MediaList? = null
    override suspend fun getTvShowsAiringTodayFromCache(): MediaList? {
        return tvShowsAiringToday
    }
    override suspend fun saveTvShowsAiringTodayToCache(tvShows: MediaList?) {
        tvShowsAiringToday = tvShows
    }

    private var tvShowsOnTheAir: MediaList? = null
    override suspend fun getTvShowsOnTheAirFromCache(): MediaList? {
        return tvShowsOnTheAir
    }
    override suspend fun saveTvShowsOnTheAirToCache(tvShows: MediaList?) {
        tvShowsOnTheAir = tvShows
    }

    private var popularTvShows: MediaList? = null
    override suspend fun getPopularTvShowsFromCache(): MediaList? {
        return popularTvShows
    }
    override suspend fun savePopularTvShowsToCache(tvShows: MediaList?) {
        popularTvShows = tvShows
    }

    private var topRatedTvShows: MediaList? = null
    override suspend fun getTopRatedTvShowsFromCache(): MediaList? {
        return topRatedTvShows
    }
    override suspend fun saveTopRatedTvShowsToCache(tvShows: MediaList?) {
        topRatedTvShows = tvShows
    }

    private var _seriesId: Int? = null
    private var _tvShow: Media? = null
    override suspend fun getTvShowDetailsFromCache(seriesId: Int): Media? {
        return if (_seriesId == seriesId) {
            _tvShow
        } else {
            null
        }
    }
    override suspend fun saveTvShowDetailsToCache(seriesId: Int, tvShow: Media?) {
        _seriesId = seriesId
        _tvShow = tvShow
    }
    override suspend fun clearTvShowFromCache(seriesId: Int) {
        if (_seriesId == seriesId) {
            _tvShow = null
        }
    }

    private var _seriesIdWatchProviders: Int? = null
    private var _watchProviders: WatchProviders? = null
    override suspend fun getTvShowWatchProvidersFromCache(seriesId: Int): WatchProviders? {
        return if (_seriesIdWatchProviders == seriesId) {
            _watchProviders
        } else {
            null
        }
    }
    override suspend fun saveTvShowWatchProvidersToCache(
        seriesId: Int,
        watchProviders: WatchProviders?
    ) {
        _seriesIdWatchProviders = seriesId
        _watchProviders = watchProviders
    }

    private var _seriesIdImages: Int? = null
    private var _images: Images? = null
    override suspend fun getTvShowImagesFromCache(seriesId: Int): Images? {
        return if (_seriesIdImages == seriesId) {
            _images
        } else {
            null
        }
    }
    override suspend fun saveTvShowImagesToCache(seriesId: Int, images: Images?) {
        _seriesIdImages = seriesId
        _images = images
    }

    private var _seriesIdVideos: Int? = null
    private var _videos: Videos? = null
    override suspend fun getTvShowVideosFromCache(seriesId: Int): Videos? {
        return if (_seriesIdVideos == seriesId) {
            _videos
        } else {
            null
        }
    }
    override suspend fun saveTvShowVideosToCache(seriesId: Int, videos: Videos?) {
        _seriesIdVideos = seriesId
        _videos = videos
    }

    private var _seriesIdForSeries: Int? = null
    private var _seasonNumber: Int? = null
    private var _season: Season? = null
    override suspend fun getTvShowSeasonDetailsFromCache(seriesId: Int, seasonNumber: Int): Season? {
        return if (_seriesIdForSeries == seriesId && _seasonNumber == seasonNumber) {
            _season
        } else {
            null
        }
    }
    override suspend fun saveTvShowSeasonDetailsToCache(seriesId: Int, seasonNumber: Int, season: Season) {
        _seriesIdForSeries = seriesId
        _seasonNumber = seasonNumber
        _season = season
    }
}