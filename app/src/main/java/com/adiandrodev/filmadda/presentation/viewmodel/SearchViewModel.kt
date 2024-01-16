package com.adiandrodev.filmadda.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.adiandrodev.filmadda.data.db.SearchHistoryEntity
import com.adiandrodev.filmadda.domain.repository.SearchHistoryRepository
import com.adiandrodev.filmadda.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): ViewModel() {

    fun getSearchHistoryList() = liveData(Dispatchers.Default) {
        val history = ArrayList(searchHistoryRepository.getSearchHistory())
        emit(history)
    }

    fun insertSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchHistoryEntity = SearchHistoryEntity(searchedQuery = query)
            searchHistoryRepository.insertSearchQuery(searchHistoryEntity)
        }
    }

    fun getSearchResult(query: String, region: String) = liveData(Dispatchers.Default) {
        val data = searchRepository.getSearchResult(query, region)
        emit(data)
    }
}

class SearchViewModelFactory(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            searchRepository,
            searchHistoryRepository
        ) as T
    }
}