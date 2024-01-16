package com.adiandrodev.filmadda.di

import android.content.Context
import com.adiandrodev.filmadda.di.collection.CollectionSubComponent
import com.adiandrodev.filmadda.di.medialists.MediaListsSubComponent
import com.adiandrodev.filmadda.di.movie.MovieSubComponent
import com.adiandrodev.filmadda.di.person.PersonSubComponent
import com.adiandrodev.filmadda.di.poppeople.PopPeopleSubComponent
import com.adiandrodev.filmadda.di.profile.ProfileSubComponent
import com.adiandrodev.filmadda.di.search.SearchSubComponent
import com.adiandrodev.filmadda.di.season.SeasonSubComponent
import com.adiandrodev.filmadda.di.setting.SettingsSubComponent
import com.adiandrodev.filmadda.di.trending.TrendingSubComponent
import com.adiandrodev.filmadda.di.tvshow.TvShowSubComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [
    ProfileSubComponent::class,
    TrendingSubComponent::class,
    MediaListsSubComponent::class,
    PopPeopleSubComponent::class,
    SearchSubComponent::class,
    SettingsSubComponent::class,
    MovieSubComponent::class,
    TvShowSubComponent::class,
    CollectionSubComponent::class,
    SeasonSubComponent::class,
    PersonSubComponent::class
])
class ApplicationModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

}
