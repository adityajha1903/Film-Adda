package com.adiandrodev.filmadda.di.collection

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.presentation.activity.CollectionActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [CollectionModule::class])
interface CollectionSubComponent {
    fun inject(collectionActivity: CollectionActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): CollectionSubComponent
    }
}