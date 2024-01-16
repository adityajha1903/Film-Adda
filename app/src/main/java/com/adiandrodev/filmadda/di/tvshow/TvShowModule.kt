package com.adiandrodev.filmadda.di.tvshow

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.AccountActionsRepository
import com.adiandrodev.filmadda.domain.repository.AccountStatesRepository
import com.adiandrodev.filmadda.domain.repository.TvShowDetailsRepository
import com.adiandrodev.filmadda.presentation.viewmodel.TvShowViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class TvShowModule {

    @ActivityScope
    @Provides
    fun provideTvShowViewModelFactory(
        accountActionRepository: AccountActionsRepository,
        accountStatesRepository: AccountStatesRepository,
        tvShowDetailsRepository: TvShowDetailsRepository
    ): TvShowViewModelFactory {
        return TvShowViewModelFactory(
            accountActionRepository,
            accountStatesRepository,
            tvShowDetailsRepository
        )
    }
}