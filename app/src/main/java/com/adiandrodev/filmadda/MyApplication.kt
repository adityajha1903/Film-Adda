package com.adiandrodev.filmadda

import android.app.Application
import com.adiandrodev.filmadda.di.*
import com.adiandrodev.filmadda.di.collection.CollectionSubComponent
import com.adiandrodev.filmadda.di.medialists.MediaListsSubComponent
import com.adiandrodev.filmadda.di.movie.MovieSubComponent
import com.adiandrodev.filmadda.di.person.PersonSubComponent
import com.adiandrodev.filmadda.di.poppeople.PopPeopleSubComponent
import com.adiandrodev.filmadda.di.trending.TrendingSubComponent
import com.adiandrodev.filmadda.di.profile.ProfileSubComponent
import com.adiandrodev.filmadda.di.search.SearchSubComponent
import com.adiandrodev.filmadda.di.season.SeasonSubComponent
import com.adiandrodev.filmadda.di.setting.SettingsSubComponent
import com.adiandrodev.filmadda.di.tvshow.TvShowSubComponent

class MyApplication: Application(), Injector {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .netModule(NetModule(BuildConfig.BASE_URL))
            .remoteDataModule(RemoteDataModule(BuildConfig.API_KEY))
            .build()
        super.onCreate()
    }

    override fun createProfileSubComponent(): ProfileSubComponent {
        return appComponent.profileSubComponent().create()
    }

    override fun createTrendingSubComponent(): TrendingSubComponent {
        return appComponent.trendingSubComponent().create()
    }

    override fun createPopPeopleSubComponent(): PopPeopleSubComponent {
        return appComponent.popPeopleSubComponent().create()
    }

    override fun createMediaListsSubComponent(): MediaListsSubComponent {
        return appComponent.mediaListsSubComponent().create()
    }

    override fun createSettingsSubComponent(): SettingsSubComponent {
        return appComponent.settingsSubComponent().create()
    }

    override fun createSearchSubComponent(): SearchSubComponent {
        return appComponent.searchSubComponent().create()
    }

    override fun createMovieSubComponent(): MovieSubComponent {
        return appComponent.movieSubComponent().create()
    }

    override fun createTvShowSubComponent(): TvShowSubComponent {
        return appComponent.tvShowSubComponent().create()
    }

    override fun createCollectionSubComponent(): CollectionSubComponent {
        return appComponent.collectionSubComponent().create()
    }

    override fun createSeasonSubComponent(): SeasonSubComponent {
        return appComponent.seasonSubComponent().create()
    }

    override fun createPersonSubComponent(): PersonSubComponent {
        return appComponent.personSubComponent().create()
    }

    companion object {
        const val APPLICATION_PREFERENCES = "film_adda_preferences"
        const val COUNTRY_KEY = "iso_3166_1"
        const val COUNTRY_NAME = "country_name"
        const val ACCOUNT_ID_KEY = "account_id"
        const val SESSION_ID_KEY = "session_id"
        const val USERNAME_KEY = "username"
        const val GRAVATAR_HASH_KEY = "gravatar_hash"
        const val AVATAR_PATH_KEY = "avatar_path"
        const val MEDIA_TYPE_MOVIE = "movie"
        const val MEDIA_TYPE_TV = "tv"
        const val MEDIA_ID_KEY = "media_id_key"
        const val SEASON_NO_KEY = "season_no_key"
        const val PERSON_ID_KEY = "person_id_key"
        const val COLLECTION_ID_KEY = "collection_id_key"
        const val IMAGE_PATH_KEY = "image_path_key"
    }
}