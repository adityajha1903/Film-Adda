package com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource

import com.adiandrodev.filmadda.data.model.*

interface TvShowCacheDataSource {

    suspend fun getTvShowsAiringTodayFromCache(): MediaList?
    suspend fun saveTvShowsAiringTodayToCache(tvShows: MediaList?)
    suspend fun getTvShowsOnTheAirFromCache(): MediaList?
    suspend fun saveTvShowsOnTheAirToCache(tvShows: MediaList?)
    suspend fun getPopularTvShowsFromCache(): MediaList?
    suspend fun savePopularTvShowsToCache(tvShows: MediaList?)
    suspend fun getTopRatedTvShowsFromCache(): MediaList?
    suspend fun saveTopRatedTvShowsToCache(tvShows: MediaList?)
    suspend fun getTvShowDetailsFromCache(seriesId: Int): Media?
    suspend fun saveTvShowDetailsToCache(seriesId: Int, tvShow: Media?)
    suspend fun getTvShowWatchProvidersFromCache(seriesId: Int): WatchProviders?
    suspend fun saveTvShowWatchProvidersToCache(seriesId: Int, watchProviders: WatchProviders?)
    suspend fun getTvShowImagesFromCache(seriesId: Int): Images?
    suspend fun saveTvShowImagesToCache(seriesId: Int, images: Images?)
    suspend fun getTvShowVideosFromCache(seriesId: Int): Videos?
    suspend fun saveTvShowVideosToCache(seriesId: Int, videos: Videos?)
    suspend fun clearTvShowFromCache(seriesId: Int)
    suspend fun getTvShowSeasonDetailsFromCache(seriesId: Int, seasonNumber: Int): Season?
    suspend fun saveTvShowSeasonDetailsToCache(seriesId: Int, seasonNumber: Int, season: Season)
}