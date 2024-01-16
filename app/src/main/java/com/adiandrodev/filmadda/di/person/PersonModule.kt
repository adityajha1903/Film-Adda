package com.adiandrodev.filmadda.di.person

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.PersonDetailsRepository
import com.adiandrodev.filmadda.presentation.viewmodel.PersonViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PersonModule {

    @ActivityScope
    @Provides
    fun providePersonViewModelFactory(
        personDetailsRepository: PersonDetailsRepository
    ): PersonViewModelFactory {
        return PersonViewModelFactory(personDetailsRepository)
    }
}