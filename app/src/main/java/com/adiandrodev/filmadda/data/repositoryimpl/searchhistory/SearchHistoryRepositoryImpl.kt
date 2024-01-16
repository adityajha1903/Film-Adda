package com.adiandrodev.filmadda.data.repositoryimpl.searchhistory

import com.adiandrodev.filmadda.data.db.SearchHistoryDatabase
import com.adiandrodev.filmadda.data.db.SearchHistoryEntity
import com.adiandrodev.filmadda.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchHistoryRepositoryImpl(
    private val searchHistoryDatabase: SearchHistoryDatabase
): SearchHistoryRepository {

    override suspend fun getSearchHistory(): List<SearchHistoryEntity> {
        val deferred = CoroutineScope(Dispatchers.IO).async {
            return@async searchHistoryDatabase.searchDao().getSearchHistory()
        }
        return deferred.await()
    }

    override suspend fun insertSearchQuery(searchEntity: SearchHistoryEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            searchHistoryDatabase.searchDao().insert(searchEntity)
        }
    }
}