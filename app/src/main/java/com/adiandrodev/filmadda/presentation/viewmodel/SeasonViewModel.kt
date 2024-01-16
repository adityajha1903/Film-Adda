package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.filmadda.domain.repository.TvShowSeasonDetailsRepository
import kotlinx.coroutines.Dispatchers

class SeasonViewModel(
    private val seasonDetailsRepository: TvShowSeasonDetailsRepository
): ViewModel() {

    fun getSeasonDetails(seriesId: Int, seasonNo: Int) = liveData(Dispatchers.IO) {
        val data = seasonDetailsRepository.getTvShowSeasonDetails(seriesId, seasonNo)
        emit(data)
    }
}

class SeasonViewModelFactory(
    private val seasonDetailsRepository: TvShowSeasonDetailsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SeasonViewModel(
            seasonDetailsRepository
        ) as T
    }
}