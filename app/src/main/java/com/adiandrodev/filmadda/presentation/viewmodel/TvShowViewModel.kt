package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.adiandrodev.filmadda.MyApplication
import com.adiandrodev.filmadda.data.model.AccountStates
import com.adiandrodev.filmadda.data.model.Rated
import com.adiandrodev.filmadda.domain.model.CombinedAccountStates
import com.adiandrodev.filmadda.domain.model.TvDetails
import com.adiandrodev.filmadda.domain.repository.AccountActionsRepository
import com.adiandrodev.filmadda.domain.repository.AccountStatesRepository
import com.adiandrodev.filmadda.domain.repository.TvShowDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TvShowViewModel(
    private val accountActionRepository: AccountActionsRepository,
    private val accountStatesRepository: AccountStatesRepository,
    private val tvShowDetailsRepository: TvShowDetailsRepository,
): ViewModel() {

    fun getTvShowDetails(tvShowId: Int, accountId: Int, sessionId: String) = liveData(Dispatchers.Default) {
        val deferredTvShowDetails = viewModelScope.async(Dispatchers.IO) {
            return@async tvShowDetailsRepository.getTvShowDetails(tvShowId)
        }
        if (accountId != -1 && sessionId.isNotEmpty()) {
            val deferredTvShowStates = viewModelScope.async(Dispatchers.IO) {
                return@async accountStatesRepository.getAllAccountStates(accountId, sessionId)
            }
            val res = getTvShowDetailsWithAccStates(deferredTvShowStates.await(), deferredTvShowDetails.await())
            emit(res)
        } else {
            val res = deferredTvShowDetails.await()
            emit(res)
        }
    }

    private fun getTvShowDetailsWithAccStates(
        accStates: CombinedAccountStates,
        tvDetails: TvDetails?
    ): TvDetails? {
        val accountStates = AccountStates(false, null, false)
        accStates.favouriteTvShows?.results?.let {
            for (media in it) {
                if (tvDetails?.tvShow?.id == media?.id) {
                    accountStates.favorite = true
                    break
                }
            }
        }
        accStates.watchlistTvShows?.results?.let {
            for (media in it) {
                if (tvDetails?.tvShow?.id == media?.id) {
                    accountStates.watchlist = true
                    break
                }
            }
        }
        accStates.ratedTvShows?.results?.let {
            for (media in it) {
                if (tvDetails?.tvShow?.id == media?.id) {
                    accountStates.rated = Rated(media?.rating?.toInt())
                    break
                }
            }
        }
        tvDetails?.tvShow?.account_states = accountStates
        return tvDetails
    }

    fun addOrRemoveFromFavList(accountId: Int, sessionId: String, favourite: Boolean, tvShowId: Int) = liveData(
        Dispatchers.Default) {
        val response = accountActionRepository.addOrRemoveMediaToFav(accountId, sessionId, favourite, tvShowId, MyApplication.MEDIA_TYPE_TV)
        if (response?.success == true) {
            accountStatesRepository.clearFavTvShowsFromCache()
            tvShowDetailsRepository.clearTvShowFromCache(tvShowId)
        }
        emit(response)
    }

    fun addOrRemoveFromWatchList(accountId: Int, sessionId: String, watchlist: Boolean, tvShowId: Int) = liveData(
        Dispatchers.Default) {
        val response = accountActionRepository.addOrRemoveMediaToWatchList(accountId, sessionId, watchlist, tvShowId, MyApplication.MEDIA_TYPE_TV)
        if (response?.success == true) {
            accountStatesRepository.clearWatchlistTvShowsFromCache()
            tvShowDetailsRepository.clearTvShowFromCache(tvShowId)
        }
        emit(response)
    }

    fun rateTvShow(tvShowId: Int, sessionId: String, rating: Double) = liveData(Dispatchers.Default) {
        val response = accountActionRepository.rateTvShow(tvShowId, sessionId, rating)
        if (response?.success == true) {
            accountStatesRepository.clearRatedTvShowsFromCache()
            tvShowDetailsRepository.clearTvShowFromCache(tvShowId)
        }
        emit(response)
    }

    fun deleteRating(tvShowId: Int, sessionId: String) = liveData(Dispatchers.Default) {
        val response = accountActionRepository.removeRatingFromTvShow(tvShowId, sessionId)
        if (response?.success == true) {
            accountStatesRepository.clearRatedTvShowsFromCache()
            tvShowDetailsRepository.clearTvShowFromCache(tvShowId)
        }
        emit(response)
    }
}

class TvShowViewModelFactory(
    private val accountActionRepository: AccountActionsRepository,
    private val accountStatesRepository: AccountStatesRepository,
    private val tvShowDetailsRepository: TvShowDetailsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TvShowViewModel(
            accountActionRepository, accountStatesRepository, tvShowDetailsRepository
        ) as T
    }
}