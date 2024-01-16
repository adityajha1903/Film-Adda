package com.adiandrodev.filmadda.di.medialists

import com.adiandrodev.filmadda.di.FragmentScope
import com.adiandrodev.filmadda.presentation.fragment.MediaListsFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [MediaListsModule::class])
interface MediaListsSubComponent {
    fun inject(mediaListsFragment: MediaListsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MediaListsSubComponent
    }
}