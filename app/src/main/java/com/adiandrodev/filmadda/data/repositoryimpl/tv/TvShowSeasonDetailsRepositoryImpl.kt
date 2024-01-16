package com.adiandrodev.filmadda.data.repositoryimpl.tv

import com.adiandrodev.filmadda.data.model.Season
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowRemoteDataSource
import com.adiandrodev.filmadda.domain.repository.TvShowSeasonDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TvShowSeasonDetailsRepositoryImpl(
    private val tvShowCacheDataSource: TvShowCacheDataSource,
    private val tvShowRemoteDataSource: TvShowRemoteDataSource
): TvShowSeasonDetailsRepository {

    override suspend fun getTvShowSeasonDetails(seriesId: Int, seasonNumber: Int): Season? {
        val deferred = CoroutineScope(Dispatchers.Default).async {
            var season = tvShowCacheDataSource.getTvShowSeasonDetailsFromCache(seriesId, seasonNumber)
            if(season == null) {
                season = tvShowRemoteDataSource.getTvShowSeasonDetails(seriesId, seasonNumber).body()
                season?.let {
                    tvShowCacheDataSource.saveTvShowSeasonDetailsToCache(seriesId, seasonNumber, it)
                }
            }
            return@async season
        }
        return deferred.await()
    }
}