package com.adiandrodev.filmadda.di.search

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.SearchHistoryRepository
import com.adiandrodev.filmadda.domain.repository.SearchRepository
import com.adiandrodev.filmadda.presentation.viewmodel.SearchViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SearchModule {

    @ActivityScope
    @Provides
    fun provideSearchViewModelFactory(
        searchRepository: SearchRepository,
        searchHistoryRepository: SearchHistoryRepository
    ): SearchViewModelFactory {
        return SearchViewModelFactory(
            searchRepository,
            searchHistoryRepository
        )
    }
}