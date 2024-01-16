package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.data.db.SearchHistoryEntity

interface SearchHistoryRepository {

    suspend fun getSearchHistory(): List<SearchHistoryEntity>
    suspend fun insertSearchQuery(searchEntity: SearchHistoryEntity)
}