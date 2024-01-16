package com.adiandrodev.filmadda.data.repositoryimpl.trending.datasourceimpl

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingCacheDataSource

class TrendingCacheDataSourceImpl: TrendingCacheDataSource {

    private var _trendingMovies: MediaList? = null
    private var moviesTimeWindow: String? = null
    override suspend fun getTrendingMoviesFromCache(timeWindow: String): MediaList? {
        return if (moviesTimeWindow == timeWindow) {
            _trendingMovies
        } else {
            return null
        }
    }
    override suspend fun saveTrendingMoviesToCache(timeWindow: String, trendingMovies: MediaList?) {
        _trendingMovies = trendingMovies
        moviesTimeWindow = timeWindow
    }

    private var _trendingTvShows: MediaList? = null
    private var tvShowTimeWindow: String? = null
    override suspend fun getTrendingTvShowFromCache(timeWindow: String): MediaList? {
        return if (tvShowTimeWindow == timeWindow) {
            _trendingTvShows
        } else {
            null
        }
    }
    override suspend fun saveTrendingTvShowToCache(timeWindow: String, trendingTvShows: MediaList?) {
        _trendingTvShows = trendingTvShows
        tvShowTimeWindow = timeWindow
    }

    private var _trendingPeople: PeopleList? = null
    private var peopleTimeWindow: String? = null
    override suspend fun getTrendingPeopleFromCache(timeWindow: String): PeopleList? {
        return if (peopleTimeWindow == timeWindow) {
            _trendingPeople
        } else {
            null
        }
    }
    override suspend fun saveTrendingPeopleToCache(timeWindow: String, trendingPeople: PeopleList?) {
        _trendingPeople = trendingPeople
        peopleTimeWindow = timeWindow
    }
}