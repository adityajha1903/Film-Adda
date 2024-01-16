package com.adiandrodev.filmadda.di.medialists

import com.adiandrodev.filmadda.di.FragmentScope
import com.adiandrodev.filmadda.domain.repository.MoviesListsRepository
import com.adiandrodev.filmadda.domain.repository.TvShowsListsRepository
import com.adiandrodev.filmadda.presentation.viewmodel.MediaListsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MediaListsModule {

    @FragmentScope
    @Provides
    fun provideMediaListsViewModelFactory(
        moviesListsRepository: MoviesListsRepository,
        tvShowsListsRepository: TvShowsListsRepository
    ): MediaListsViewModelFactory {
        return MediaListsViewModelFactory(
            moviesListsRepository,
            tvShowsListsRepository
        )
    }
}