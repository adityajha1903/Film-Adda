package com.adiandrodev.filmadda.data.repositoryimpl.movie.datasourceimpl

import com.adiandrodev.filmadda.data.api.MovieService
import com.adiandrodev.filmadda.data.model.*
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieRemoteDataSource
import retrofit2.Response

class MovieRemoteDataSourceImpl(
    private val movieService: MovieService,
    private val apiKey: String
): MovieRemoteDataSource {

    override suspend fun getNowPlayingMovies(region: String): Response<MediaList> {
        return movieService.getNowPlayingMovies(region = region, apiKey = apiKey)
    }

    override suspend fun getPopularMovies(region: String): Response<MediaList> {
        return movieService.getPopularMovies(region = region, apiKey = apiKey)
    }

    override suspend fun getTopRatedMovies(region: String): Response<MediaList> {
        return movieService.getTopRatedMovies(region = region, apiKey = apiKey)
    }

    override suspend fun getUpcomingMovies(region: String): Response<MediaList> {
        return movieService.getUpcomingMovies(region = region, apiKey = apiKey)
    }

    override suspend fun getMovieDetails(movieId: Int): Response<Media> {
        return movieService.getMovieDetails(movieId = movieId, apiKey = apiKey)
    }

    override suspend fun getMovieWatchProviders(movieId: Int): Response<WatchProviders> {
        return movieService.getMovieWatchProviders(movieId, apiKey)
    }

    override suspend fun getMovieImages(movieId: Int): Response<Images> {
        return movieService.getMovieImages(movieId = movieId, apiKey = apiKey)
    }

    override suspend fun getMovieVideos(movieId: Int): Response<Videos> {
        return movieService.getMovieVideos(movieId = movieId, apiKey = apiKey)
    }

    override suspend fun getCollectionDetails(collectionId: Int): Response<CollectionDetails> {
        return movieService.getCollectionDetails(collectionId = collectionId, apiKey = apiKey)
    }

}