package com.adiandrodev.filmadda.di

import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasource.AccountStatesCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.accountstates.datasourceimpl.AccountStatesCacheDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasource.MovieCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.movie.datasourceimpl.MovieCacheDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasource.PeopleCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.people.datasourceimpl.PeopleCacheDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasource.SearchCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.search.datasourceimpl.SearchCacheDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasource.TrendingCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.trending.datasourceimpl.TrendingCacheDataSourceImpl
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasource.TvShowCacheDataSource
import com.adiandrodev.filmadda.data.repositoryimpl.tv.datasourceimpl.TvShowCacheDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheDataModule {

    @Singleton
    @Provides
    fun provideTrendingCacheDataSource(): TrendingCacheDataSource {
        return TrendingCacheDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideMovieCacheDataSource(): MovieCacheDataSource {
        return MovieCacheDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideTvShowCacheDataSource(): TvShowCacheDataSource {
        return TvShowCacheDataSourceImpl()
    }

    @Singleton
    @Provides
    fun providePeopleCacheDataSource(): PeopleCacheDataSource {
        return PeopleCacheDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideAccountStatesCacheDataSource(): AccountStatesCacheDataSource {
        return AccountStatesCacheDataSourceImpl()
    }

    @Singleton
    @Provides
    fun provideSearchCacheDataSource(): SearchCacheDataSource {
        return SearchCacheDataSourceImpl()
    }
}
