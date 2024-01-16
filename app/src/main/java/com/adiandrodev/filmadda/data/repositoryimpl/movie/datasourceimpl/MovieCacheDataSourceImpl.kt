package com.adiandrodev.filmadda.data.repositoryimpl.movie.datasourceimpl

import com.adiandrodev.filmadda.data.model.*
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieCacheDataSource

class MovieCacheDataSourceImpl: MovieCacheDataSource {

    private var nowPlayingRegion: String? = null
    private var nowPlayingMovies: MediaList? = null
    override suspend fun getNowPlayingMoviesFromCache(region: String): MediaList? {
        return if (nowPlayingRegion == region) {
            nowPlayingMovies
        } else {
            null
        }
    }
    override suspend fun saveNowPlayingMoviesToCache(region: String, movies: MediaList?) {
        nowPlayingMovies = movies
        nowPlayingRegion = region
    }

    private var popularMoviesRegion: String? = null
    private var popularMovies: MediaList? = null
    override suspend fun getPopularMoviesFromCache(region: String): MediaList? {
        return if (popularMoviesRegion == region) {
            popularMovies
        } else {
            null
        }
    }
    override suspend fun savePopularMoviesToCache(region: String, movies: MediaList?) {
        popularMovies = movies
        popularMoviesRegion = region
    }

    private var topRatedMoviesRegion: String? = null
    private var topRatedMovies: MediaList? = null
    override suspend fun getTopRatedMoviesFromCache(region: String): MediaList? {
        return if (topRatedMoviesRegion == region) {
            topRatedMovies
        } else {
            null
        }
    }
    override suspend fun saveTopRatedMoviesToCache(region: String, movies: MediaList?) {
        topRatedMovies = movies
        topRatedMoviesRegion = region
    }

    private var upcomingMoviesRegion: String? = null
    private var upcomingMovies: MediaList? = null
    override suspend fun getUpcomingMoviesFromCache(region: String): MediaList? {
        return if (upcomingMoviesRegion == region) {
            upcomingMovies
        } else {
            null
        }
    }
    override suspend fun saveUpcomingMoviesToCache(region: String, movies: MediaList?) {
        upcomingMovies = movies
        upcomingMoviesRegion = region
    }

    private var detailsMovieId: Int? = null
    private var _movie: Media? = null
    override suspend fun getMovieDetailsFromCache(movieId: Int): Media? {
        return if (detailsMovieId == movieId) {
            _movie
        } else {
            null
        }
    }
    override suspend fun saveMovieDetailsToCache(movieId: Int, movie: Media) {
        _movie = movie
        detailsMovieId = movieId
    }
    override suspend fun clearMovieDetailsFromCache(movieId: Int) {
        if (detailsMovieId == movieId) {
            _movie = null
            detailsMovieId = null
        }
    }

    private var watchProviderMovieId: Int? = null
    private var _watchProvider: WatchProviders? = null
    override suspend fun getMovieWatchProvidersFromCache(movieId: Int): WatchProviders? {
        return if (watchProviderMovieId == movieId) {
            _watchProvider
        } else {
            null
        }
    }
    override suspend fun saveMovieWatchProvidersToCache(
        movieId: Int,
        watchProviders: WatchProviders?
    ) {
        _watchProvider = watchProviders
        watchProviderMovieId = movieId
    }

    private var imagesMovieId: Int? = null
    private var movieImages: Images? = null
    override suspend fun getMovieImagesFromCache(movieId: Int): Images? {
        return if (imagesMovieId == movieId) {
            movieImages
        } else {
            null
        }
    }
    override suspend fun saveMovieImagesToCache(movieId: Int, images: Images?) {
        movieImages = images
        imagesMovieId = movieId
    }

    private var videosMovieId: Int? = null
    private var _videos: Videos? = null
    override suspend fun getMovieVideosFromCache(movieId: Int): Videos? {
        return if (movieId == videosMovieId) {
            _videos
        } else {
            null
        }
    }
    override suspend fun saveMovieVideosToCache(movieId: Int, videos: Videos?) {
        videosMovieId = movieId
        _videos = videos
    }

    private var _collectionId: Int? = null
    private var _collection: CollectionDetails? = null
    override suspend fun getCollectionDetailsFromCache(collectionId: Int): CollectionDetails? {
        return if (_collectionId == collectionId) {
            _collection
        } else {
            null
        }
    }
    override suspend fun saveCollectionDetailsToCache(collectionId: Int, collection: CollectionDetails?) {
        _collection = collection
        _collectionId = collectionId
    }
}