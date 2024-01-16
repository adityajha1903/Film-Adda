package com.adiandrodev.filmadda.di

import android.content.Context
import com.adiandrodev.filmadda.data.db.SearchHistoryDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDatabaseModule {

    @Singleton
    @Provides
    fun provideSearchHistoryDatabase(context: Context): SearchHistoryDatabase {
        return SearchHistoryDatabase.getInstance(context)
    }
}