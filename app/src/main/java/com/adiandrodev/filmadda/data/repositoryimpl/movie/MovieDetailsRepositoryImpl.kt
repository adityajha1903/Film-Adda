package com.adiandrodev.filmadda.data.repositoryimpl.movie

import com.adiandrodev.filmadda.data.model.Images
import com.adiandrodev.filmadda.data.model.Videos
import com.adiandrodev.filmadda.data.model.WatchProviders
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieRemoteDataSource
import com.adiandrodev.filmadda.domain.model.MovieDetails
import com.adiandrodev.filmadda.domain.repository.MovieDetailsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MovieDetailsRepositoryImpl(
    private val movieCacheDataSource: MovieCacheDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource
): MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            val deferredMovie = CoroutineScope(Dispatchers.IO).async {
                var movie = movieCacheDataSource.getMovieDetailsFromCache(movieId)
                if (movie == null) {
                    val response = movieRemoteDataSource.getMovieDetails(movieId)
                    movie = response.body()
                    movie?.let {
                        movieCacheDataSource.saveMovieDetailsToCache(movieId, movie)
                    }
                }
                movie
            }
            val deferredImages = CoroutineScope(Dispatchers.IO).async {
                getMovieImages(movieId)
            }
            val deferredVideos = CoroutineScope(Dispatchers.IO).async {
                getMovieVideos(movieId)
            }
            val deferredWatchProviders = CoroutineScope(Dispatchers.IO).async {
                getMovieWatchProviders(movieId)
            }
            return@async MovieDetails(
                deferredMovie.await(),
                deferredImages.await(),
                deferredVideos.await(),
                deferredWatchProviders.await()
            )
        }
        return deferred.await()
    }

    private suspend fun getMovieWatchProviders(movieId: Int) : WatchProviders? {
        var watchProviders = movieCacheDataSource.getMovieWatchProvidersFromCache(movieId)
        if (watchProviders == null) {
            val response = movieRemoteDataSource.getMovieWatchProviders(movieId)
            watchProviders = response.body()
            watchProviders?.let {
                movieCacheDataSource.saveMovieWatchProvidersToCache(movieId, watchProviders)
            }
        }
        return watchProviders
    }

    override suspend fun clearMovieDetailsFromCache(movieId: Int) {
        movieCacheDataSource.clearMovieDetailsFromCache(movieId)
    }

    private suspend fun getMovieImages(movieId: Int): Images? {
        var images = movieCacheDataSource.getMovieImagesFromCache(movieId)
        if (images == null) {
            val response = movieRemoteDataSource.getMovieImages(movieId)
            images = response.body()
            images?.let {
                movieCacheDataSource.saveMovieImagesToCache(movieId, images)
            }
        }
        return images
    }

    private suspend fun getMovieVideos(movieId: Int): Videos? {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            var videos = movieCacheDataSource.getMovieVideosFromCache(movieId)
            if (videos == null) {
                val response = movieRemoteDataSource.getMovieVideos(movieId)
                videos = response.body()
                videos?.let {
                    movieCacheDataSource.saveMovieVideosToCache(movieId, videos)
                }
            }
            return@async videos
        }
        return deferred.await()
    }
}