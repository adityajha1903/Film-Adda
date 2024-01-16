package com.adiandrodev.filmadda.domain.repository

import com.adiandrodev.filmadda.domain.model.SearchResult

interface SearchRepository {

    suspend fun getSearchResult(query: String, region: String): SearchResult
}