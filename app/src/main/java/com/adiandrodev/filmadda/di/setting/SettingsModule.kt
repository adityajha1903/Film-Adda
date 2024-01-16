package com.adiandrodev.filmadda.di.setting

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.AccountStatesRepository
import com.adiandrodev.filmadda.presentation.viewmodel.SettingsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @ActivityScope
    @Provides
    fun provideSettingsViewModelFactory(
        accountStatesRepository: AccountStatesRepository
    ): SettingsViewModelFactory {
        return SettingsViewModelFactory(accountStatesRepository)
    }
}