package com.adiandrodev.filmadda.di.profile

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.*
import com.adiandrodev.filmadda.presentation.viewmodel.ProfileViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @ActivityScope
    @Provides
    fun provideProfileViewModelFactory(
        accountStatesRepository: AccountStatesRepository,
        accountActionsRepository: AccountActionsRepository,
        movieDetailsRepository: MovieDetailsRepository,
        tvShowDetailsRepository: TvShowDetailsRepository,
        authRepository: AuthRepository
    ): ProfileViewModelFactory {
        return ProfileViewModelFactory(
            accountStatesRepository,
            accountActionsRepository,
            movieDetailsRepository,
            tvShowDetailsRepository,
            authRepository
        )
    }
}