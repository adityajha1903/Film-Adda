package com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource

import com.adiandrodev.filmadda.data.model.*

interface MovieCacheDataSource {

    suspend fun getNowPlayingMoviesFromCache(region: String): MediaList?
    suspend fun saveNowPlayingMoviesToCache(region: String, movies: MediaList?)
    suspend fun getPopularMoviesFromCache(region: String): MediaList?
    suspend fun savePopularMoviesToCache(region: String, movies: MediaList?)
    suspend fun getTopRatedMoviesFromCache(region: String): MediaList?
    suspend fun saveTopRatedMoviesToCache(region: String, movies: MediaList?)
    suspend fun getUpcomingMoviesFromCache(region: String): MediaList?
    suspend fun saveUpcomingMoviesToCache(region: String, movies: MediaList?)
    suspend fun getMovieDetailsFromCache(movieId: Int): Media?
    suspend fun saveMovieDetailsToCache(movieId: Int, movie: Media)
    suspend fun clearMovieDetailsFromCache(movieId: Int)
    suspend fun getMovieWatchProvidersFromCache(movieId: Int): WatchProviders?
    suspend fun saveMovieWatchProvidersToCache(movieId: Int, watchProviders: WatchProviders?)
    suspend fun getMovieImagesFromCache(movieId: Int): Images?
    suspend fun saveMovieImagesToCache(movieId: Int, images: Images?)
    suspend fun getMovieVideosFromCache(movieId: Int): Videos?
    suspend fun saveMovieVideosToCache(movieId: Int, videos: Videos?)
    suspend fun getCollectionDetailsFromCache(collectionId: Int): CollectionDetails?
    suspend fun saveCollectionDetailsToCache(collectionId: Int, collection: CollectionDetails?)
}