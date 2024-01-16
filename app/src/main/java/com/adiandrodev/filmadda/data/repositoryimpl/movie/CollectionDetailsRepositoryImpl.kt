package com.adiandrodev.filmadda.data.repositoryimpl.movie

import com.adiandrodev.filmadda.data.model.CollectionDetails
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieRemoteDataSource
import com.adiandrodev.filmadda.domain.repository.CollectionDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class CollectionDetailsRepositoryImpl(
    private val movieCacheDataSource: MovieCacheDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource
): CollectionDetailsRepository {

    override suspend fun getCollectionDetails(collectionId: Int): CollectionDetails? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            var collection = movieCacheDataSource.getCollectionDetailsFromCache(collectionId)
            if (collection == null) {
                val response = movieRemoteDataSource.getCollectionDetails(collectionId)
                collection = response.body()
                movieCacheDataSource.saveCollectionDetailsToCache(collectionId, collection)
            }
            return@async collection
        }
        return deferred.await()
    }
}