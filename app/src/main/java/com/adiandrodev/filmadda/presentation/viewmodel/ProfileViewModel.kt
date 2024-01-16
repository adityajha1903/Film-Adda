package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.filmadda.domain.repository.*
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(
    private val accountStatesRepository: AccountStatesRepository,
    private val accountActionsRepository: AccountActionsRepository,
    private val movieDetailsRepository: MovieDetailsRepository,
    private val tvShowDetailsRepository: TvShowDetailsRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    fun getRequestToken() = liveData(Dispatchers.Default) {
        val token = authRepository.getRequestToken()
        emit(token)
    }

    fun createSession(requestToken: String) = liveData(Dispatchers.Default) {
        val response = authRepository.createSession(requestToken)
        emit(response)
    }

    fun deleteSession(sessionId: String) = liveData(Dispatchers.Default) {
        val response = authRepository.deleteSession(sessionId)
        emit(response)
    }

    fun getAccountDetails(sessionId: String) = liveData(Dispatchers.Default) {
        val data = authRepository.getAccountDetails(sessionId)
        emit(data)
    }

    fun getAllAccountStates(accountId: Int, sessionId: String) = liveData(Dispatchers.Default) {
        val data = accountStatesRepository.getAllAccountStates(accountId, sessionId)
        emit(data)
    }

    fun removeMovieFromFav(accountId: Int, sessionId: String, mediaId: Int) = liveData(Dispatchers.Default) {
        val response = accountActionsRepository.addOrRemoveMediaToFav(accountId, sessionId, false, mediaId, MEDIA_TYPE_MOVIE)
        if (response?.success == true) {
            accountStatesRepository.clearFavMoviesFromCache()
            movieDetailsRepository.getMovieDetails(mediaId)
            tvShowDetailsRepository
        }
        emit(response)
    }

    fun removeTvShowFromFav(accountId: Int, sessionId: String, mediaId: Int) = liveData(Dispatchers.Default) {
        val response = accountActionsRepository.addOrRemoveMediaToFav(accountId, sessionId, false, mediaId, MEDIA_TYPE_TV)
        if (response?.success == true) {
            accountStatesRepository.clearFavTvShowsFromCache()
        }
        emit(response)
    }

    fun removeMovieFromWatchlist(accountId: Int, sessionId: String, mediaId: Int) = liveData(Dispatchers.Default) {
        val response = accountActionsRepository.addOrRemoveMediaToWatchList(accountId, sessionId, false, mediaId, MEDIA_TYPE_MOVIE)
        if (response?.success == true) {
            accountStatesRepository.clearWatchlistMoviesFromCache()
        }
        emit(response)
    }

    fun removeTvShowFromWatchlist(accountId: Int, sessionId: String, mediaId: Int) = liveData(Dispatchers.Default) {
        val response = accountActionsRepository.addOrRemoveMediaToWatchList(accountId, sessionId, false, mediaId, MEDIA_TYPE_TV)
        if (response?.success == true) {
            accountStatesRepository.clearWatchlistTvShowsFromCache()
        }
        emit(response)
    }

    fun removeRatingFromMovie(movieId: Int, sessionId: String) = liveData(Dispatchers.Default) {
        val response = accountActionsRepository.removeRatingFromMovie(movieId, sessionId)
        if (response?.success == true) {
            accountStatesRepository.clearRatedMoviesFromCache()
        }
        emit(response)
    }

    fun removeRatingFromTvShow(seriesId: Int, sessionId: String) = liveData(Dispatchers.Default) {
        val response = accountActionsRepository.removeRatingFromTvShow(seriesId, sessionId)
        if (response?.success == true) {
            accountStatesRepository.clearRatedTvShowsFromCache()
        }
        emit(response)
    }

    fun getAllAvailableCountries() = liveData(Dispatchers.Default) {
        val data = accountStatesRepository.getAvailableCountries()
        emit(data)
    }

    companion object {
        private const val MEDIA_TYPE_MOVIE = "movie"
        private const val MEDIA_TYPE_TV = "tv"
    }
}

class ProfileViewModelFactory(
    private val accountStatesRepository: AccountStatesRepository,
    private val accountActionsRepository: AccountActionsRepository,
    private val movieDetailsRepository: MovieDetailsRepository,
    private val tvShowDetailsRepository: TvShowDetailsRepository,
    private val authRepository: AuthRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(
            accountStatesRepository,
            accountActionsRepository,
            movieDetailsRepository,
            tvShowDetailsRepository,
            authRepository
        ) as T
    }
}