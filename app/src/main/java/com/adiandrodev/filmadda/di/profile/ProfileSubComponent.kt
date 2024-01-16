package com.adiandrodev.filmadda.di.profile

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.presentation.activity.ProfileActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileSubComponent {
    fun inject(activity: ProfileActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileSubComponent
    }
}