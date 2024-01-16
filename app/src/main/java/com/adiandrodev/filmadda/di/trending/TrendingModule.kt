package com.adiandrodev.filmadda.di.trending

import com.adiandrodev.filmadda.di.FragmentScope
import com.adiandrodev.filmadda.domain.repository.*
import com.adiandrodev.filmadda.presentation.viewmodel.TrendingViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class TrendingModule {

    @FragmentScope
    @Provides
    fun provideMainViewModelFactory(
        trendingRepository: TrendingRepository
    ): TrendingViewModelFactory {
        return TrendingViewModelFactory(
            trendingRepository
        )
    }
}