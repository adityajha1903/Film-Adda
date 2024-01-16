package com.adiandrodev.filmadda.di.movie

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.AccountActionsRepository
import com.adiandrodev.filmadda.domain.repository.AccountStatesRepository
import com.adiandrodev.filmadda.domain.repository.MovieDetailsRepository
import com.adiandrodev.filmadda.presentation.viewmodel.MovieViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class MovieModule {

    @ActivityScope
    @Provides
    fun provideMovieViewModelFactory(
        accountActionsRepository: AccountActionsRepository,
        accountStatesRepository: AccountStatesRepository,
        movieDetailsRepository: MovieDetailsRepository
    ): MovieViewModelFactory {
        return MovieViewModelFactory(
            accountActionsRepository,
            accountStatesRepository,
            movieDetailsRepository
        )
    }
}