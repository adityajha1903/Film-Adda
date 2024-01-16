package com.adiandrodev.filmadda.di.season

import com.adiandrodev.filmadda.di.ActivityScope
import com.adiandrodev.filmadda.presentation.activity.SeasonActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [SeasonModule::class])
interface SeasonSubComponent {
    fun inject(seasonActivity: SeasonActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SeasonSubComponent
    }
}