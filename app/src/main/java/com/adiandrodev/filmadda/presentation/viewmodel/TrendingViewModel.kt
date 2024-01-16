package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.*
import com.adiandrodev.filmadda.domain.repository.TrendingRepository
import kotlinx.coroutines.Dispatchers

class TrendingViewModel(
    private val trendingRepository: TrendingRepository,
): ViewModel() {

    fun getTrendingData() = liveData(Dispatchers.Default) {
        val data = trendingRepository.getTrendingData()
        emit(data)
    }

    fun getTrendingMovies(timeWindow: String) = liveData(Dispatchers.Default) {
        val data = trendingRepository.getTrendingMovies(timeWindow)
        emit(data)
    }

    fun getTrendingTvShows(timeWindow: String) = liveData(Dispatchers.Default) {
        val data = trendingRepository.getTrendingTvShows(timeWindow)
        emit(data)
    }

    fun getTrendingPeople(timeWindow: String) = liveData(Dispatchers.Default) {
        val data = trendingRepository.getTrendingPeople(timeWindow)
        emit(data)
    }
}

class TrendingViewModelFactory(
    private val trendingRepository: TrendingRepository,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrendingViewModel(
            trendingRepository
        ) as T
    }
}