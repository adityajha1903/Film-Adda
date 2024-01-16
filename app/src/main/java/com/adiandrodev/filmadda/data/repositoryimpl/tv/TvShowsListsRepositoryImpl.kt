package com.adiandrodev.filmadda.data.repositoryimpl.tv

import com.adiandrodev.filmadda.data.model.MediaList
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowRemoteDataSource
import com.adiandrodev.filmadda.domain.model.TvShowsLists
import com.adiandrodev.filmadda.domain.repository.TvShowsListsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TvShowsListsRepositoryImpl(
    private val tvShowCacheDataSource: TvShowCacheDataSource,
    private val tvShowRemoteDataSource: TvShowRemoteDataSource
): TvShowsListsRepository {

    override suspend fun getTvShowsLists(): TvShowsLists? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val deferredAiringToday = async(Dispatchers.IO) {
                getTvShowsAiringToday()
            }
            val deferredOnTheAir = async(Dispatchers.IO) {
                getTvShowsOnTheAir()
            }
            val deferredPopular = async(Dispatchers.IO) {
                getPopularTvShows()
            }
            val deferredTopRated = async(Dispatchers.IO) {
                getTopRatedTvShows()
            }
            val lists = TvShowsLists(
                deferredAiringToday.await(),
                deferredOnTheAir.await(),
                deferredPopular.await(),
                deferredTopRated.await()
            )

            if (
                lists.airingToday == null
                && lists.popular == null
                && lists.topRated == null
                && lists.onTheAir == null
            ) {
                return@async null
            } else {
                return@async lists
            }
        }
        return deferred.await()
    }

    private suspend fun getTvShowsAiringToday(): MediaList? {
        var tvShows = tvShowCacheDataSource.getTvShowsAiringTodayFromCache()
        if (tvShows == null) {
            tvShows = tvShowRemoteDataSource.getTvShowsAiringToday().body()
            tvShowCacheDataSource.saveTvShowsAiringTodayToCache(tvShows)
        }
        return tvShows
    }

    private suspend fun getTvShowsOnTheAir(): MediaList? {
        var tvShows = tvShowCacheDataSource.getTvShowsOnTheAirFromCache()
        if (tvShows == null) {
            tvShows = tvShowRemoteDataSource.getTvShowsOnTheAir().body()
            tvShowCacheDataSource.saveTvShowsOnTheAirToCache(tvShows)
        }
        return tvShows
    }

    private suspend fun getPopularTvShows(): MediaList? {
        var tvShows = tvShowCacheDataSource.getPopularTvShowsFromCache()
        if (tvShows == null) {
            tvShows = tvShowRemoteDataSource.getPopularTvShows().body()
            tvShowCacheDataSource.savePopularTvShowsToCache(tvShows)
        }
        return tvShows
    }

    private suspend fun getTopRatedTvShows(): MediaList? {
        var tvShows = tvShowCacheDataSource.getTopRatedTvShowsFromCache()
        if (tvShows == null) {
            tvShows = tvShowRemoteDataSource.getTopRatedTvShows().body()
            tvShowCacheDataSource.saveTopRatedTvShowsToCache(tvShows)
        }
        return tvShows
    }
}