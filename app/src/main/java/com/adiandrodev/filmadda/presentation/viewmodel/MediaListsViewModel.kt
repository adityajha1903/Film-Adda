package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.adiandrodev.filmadda.domain.repository.MoviesListsRepository
import com.adiandrodev.filmadda.domain.repository.TvShowsListsRepository
import kotlinx.coroutines.Dispatchers

class MediaListsViewModel(
    private val moviesListsRepository: MoviesListsRepository,
    private val tvShowsListsRepository: TvShowsListsRepository
) : ViewModel() {

    fun getMoviesLists(region: String) = liveData(Dispatchers.Default) {
        val data = moviesListsRepository.getMoviesLists(region)
        emit(data)
    }

    fun getTvShowsLists() = liveData(Dispatchers.Default) {
        val data = tvShowsListsRepository.getTvShowsLists()
        emit(data)
    }
}

class MediaListsViewModelFactory(
    private val moviesListsRepository: MoviesListsRepository,
    private val tvShowsListsRepository: TvShowsListsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MediaListsViewModel(moviesListsRepository, tvShowsListsRepository) as T
    }
}