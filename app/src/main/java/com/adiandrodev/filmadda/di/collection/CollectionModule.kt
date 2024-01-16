package com.adiandrodev.filmadda.di.collection

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.domain.repository.CollectionDetailsRepository
import com.adiandrodev.filmadda.presentation.viewmodel.CollectionViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class CollectionModule {

    @ActivityScope
    @Provides
    fun provideCollectionViewModelFactory(
        collectionDetailsRepository: CollectionDetailsRepository
    ): CollectionViewModelFactory {
        return CollectionViewModelFactory(collectionDetailsRepository)
    }
}