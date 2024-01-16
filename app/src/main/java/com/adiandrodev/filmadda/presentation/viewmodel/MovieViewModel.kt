package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.adiandrodev.filmadda.MyApplication.Companion.MEDIA_TYPE_MOVIE
import com.adiandrodev.filmadda.data.model.AccountStates
import com.adiandrodev.filmadda.data.model.Rated
import com.adiandrodev.filmadda.domain.model.CombinedAccountStates
import com.adiandrodev.filmadda.domain.model.MovieDetails
import com.adiandrodev.filmadda.domain.repository.AccountActionsRepository
import com.adiandrodev.filmadda.domain.repository.AccountStatesRepository
import com.adiandrodev.filmadda.domain.repository.MovieDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MovieViewModel(
    private val accountActionRepository: AccountActionsRepository,
    private val accountStatesRepository: AccountStatesRepository,
    private val movieDetailsRepository: MovieDetailsRepository
): ViewModel() {

    fun getMovieDetails(movieId: Int, accountId: Int, sessionId: String) = liveData(Dispatchers.Default) {
        val deferredMovieDetails = viewModelScope.async(Dispatchers.IO) {
            return@async movieDetailsRepository.getMovieDetails(movieId)
        }
        if (accountId != -1 && sessionId.isNotEmpty()) {
            val deferredMovieStates = viewModelScope.async(Dispatchers.IO) {
                return@async accountStatesRepository.getAllAccountStates(accountId, sessionId)
            }
            val res = getMovieDetailsWithAccStates(deferredMovieStates.await(), deferredMovieDetails.await())
            emit(res)
        } else {
            val res = deferredMovieDetails.await()
            emit(res)
        }
    }

    private fun getMovieDetailsWithAccStates(
        accStates: CombinedAccountStates,
        movieDetails: MovieDetails?
    ): MovieDetails? {
        val accountStates = AccountStates(false, null, false)
        accStates.favouriteMovies?.results?.let {
            for (media in it) {
                if (movieDetails?.movie?.id == media?.id) {
                    accountStates.favorite = true
                    break
                }
            }
        }
        accStates.watchlistMovies?.results?.let {
            for (media in it) {
                if (movieDetails?.movie?.id == media?.id) {
                    accountStates.watchlist = true
                    break
                }
            }
        }
        accStates.ratedMovies?.results?.let {
            for (media in it) {
                if (movieDetails?.movie?.id == media?.id) {
                    accountStates.rated = Rated(media?.rating?.toInt())
                    break
                }
            }
        }
        movieDetails?.movie?.account_states = accountStates
        return movieDetails
    }

    fun addOrRemoveFromFavList(accountId: Int, sessionId: String, favourite: Boolean, movieId: Int) = liveData(Dispatchers.Default) {
        val response = accountActionRepository.addOrRemoveMediaToFav(accountId, sessionId, favourite, movieId, MEDIA_TYPE_MOVIE)
        if (response?.success == true) {
            accountStatesRepository.clearFavMoviesFromCache()
            movieDetailsRepository.clearMovieDetailsFromCache(movieId)
        }
        emit(response)
    }

    fun addOrRemoveFromWatchList(accountId: Int, sessionId: String, watchlist: Boolean, movieId: Int) = liveData(Dispatchers.Default) {
        val response = accountActionRepository.addOrRemoveMediaToWatchList(accountId, sessionId, watchlist, movieId, MEDIA_TYPE_MOVIE)
        if (response?.success == true) {
            accountStatesRepository.clearWatchlistMoviesFromCache()
            movieDetailsRepository.clearMovieDetailsFromCache(movieId)
        }
        emit(response)
    }

    fun rateMovie(movieId: Int, sessionId: String, rating: Double) = liveData(Dispatchers.Default) {
        val response = accountActionRepository.rateMovie(movieId, sessionId, rating)
        if (response?.success == true) {
            accountStatesRepository.clearRatedMoviesFromCache()
            movieDetailsRepository.clearMovieDetailsFromCache(movieId)
        }
        emit(response)
    }

    fun deleteRating(movieId: Int, sessionId: String) = liveData(Dispatchers.Default) {
        val response = accountActionRepository.removeRatingFromMovie(movieId, sessionId)
        if (response?.success == true) {
            accountStatesRepository.clearRatedMoviesFromCache()
            movieDetailsRepository.clearMovieDetailsFromCache(movieId)
        }
        emit(response)
    }
}

class MovieViewModelFactory(
    private val accountActionRepository: AccountActionsRepository,
    private val accountStatesRepository: AccountStatesRepository,
    private val movieDetailsRepository: MovieDetailsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(
            accountActionRepository,
            accountStatesRepository,
            movieDetailsRepository
        ) as T
    }
}