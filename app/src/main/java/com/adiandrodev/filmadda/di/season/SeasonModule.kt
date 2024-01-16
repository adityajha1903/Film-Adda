package com.adiandrodev.filmadda.di.season

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.TvShowSeasonDetailsRepository
import com.adiandrodev.filmadda.presentation.viewmodel.SeasonViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SeasonModule {

    @ActivityScope
    @Provides
    fun provideSeasonViewModelFactory(
        seasonDetailsRepository: TvShowSeasonDetailsRepository
    ): SeasonViewModelFactory {
        return SeasonViewModelFactory(seasonDetailsRepository)
    }
}