package com.adiandrodev.filmadda.data.repositoryimpl.trending

import android.util.Log
import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.model.PeopleList
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingRemoteDataSource
import com.adiandrodev.filmadda.domain.model.Trending
import com.adiandrodev.filmadda.domain.repository.TrendingRepository
import kotlinx.coroutines.*

class TrendingRepositoryImpl(
    private val trendingCacheDataSource: TrendingCacheDataSource,
    private val trendingRemoteDataSource: TrendingRemoteDataSource
): TrendingRepository {

    override suspend fun getTrendingData(): Trending? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val deferredTrendingMovies = async(Dispatchers.IO) {
                getTrendingMovies("day")
            }
            val deferredTrendingTvShow = async(Dispatchers.IO) {
                getTrendingTvShows("day")
            }
            val deferredTrendingPeople = async(Dispatchers.IO) {
                getTrendingPeople("day")
            }

            val trending = Trending(
                deferredTrendingMovies.await(),
                deferredTrendingTvShow.await(),
                deferredTrendingPeople.await()
            )

            if (trending.trendingMovies == null || trending.trendingTvShows == null || trending.trendingPeople == null) {
                return@async null
            } else {
                return@async trending
            }
        }

        return deferred.await()
    }

    override suspend fun getTrendingMovies(timeWindow: String): MediaList? {
        Log.d("MY_TAG", "TrendingMovies: NULL")
        var trendingMovies = trendingCacheDataSource.getTrendingMoviesFromCache(timeWindow)
        if (trendingMovies == null) {
            val response = trendingRemoteDataSource.getTrendingMovies(timeWindow)
            trendingMovies = response.body()
            trendingCacheDataSource.saveTrendingMoviesToCache(timeWindow, trendingMovies)
        }
        Log.d("MY_TAG", "TrendingMovies: ${trendingMovies.toString()}")
        return trendingMovies
    }

    override suspend fun getTrendingTvShows(timeWindow: String): MediaList? {
        var trendingTvShows = trendingCacheDataSource.getTrendingTvShowFromCache(timeWindow)
        if (trendingTvShows == null) {
            val response = trendingRemoteDataSource.getTrendingTvShows(timeWindow)
            trendingTvShows = response.body()
            trendingCacheDataSource.saveTrendingTvShowToCache(timeWindow, trendingTvShows)
        }
        return trendingTvShows
    }

    override suspend fun getTrendingPeople(timeWindow: String): PeopleList? {
        var trendingPeople = trendingCacheDataSource.getTrendingPeopleFromCache(timeWindow)
        if (trendingPeople == null) {
            val response = trendingRemoteDataSource.getTrendingPeople(timeWindow)
            trendingPeople = response.body()
            trendingCacheDataSource.saveTrendingPeopleToCache(timeWindow, trendingPeople)
        }
        return trendingPeople
    }
}