package com.example.mymusic.di

import android.content.Context
import com.example.mymusic.data.local.AppDatabase
import com.example.mymusic.data.local.SearchHistoryDao
import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.data.repository.DiscoveryRepository
import com.example.mymusic.data.repository.SearchRepository
import com.example.mymusic.data.repository.TrackRepositoryImpl
import com.example.mymusic.domain.repository.TrackRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTrackRepository(impl: TrackRepositoryImpl): TrackRepository

    companion object {
        @Provides
        @Singleton
        fun provideTracksService(retrofit: Retrofit): JamendoTracksService =
            retrofit.create(JamendoTracksService::class.java)
            
        @Provides
        @Singleton
        fun provideTrackMapper(): TrackMapper = TrackMapper
        
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            AppDatabase.getDatabase(context)
            
        @Provides
        @Singleton
        fun provideSearchHistoryDao(database: AppDatabase): SearchHistoryDao =
            database.searchHistoryDao()
            
        @Provides
        @Singleton
        fun provideSearchRepository(
            service: JamendoTracksService,
            trackMapper: TrackMapper
        ): SearchRepository = SearchRepository(service, trackMapper)
        
        @Provides
        @Singleton
        fun provideDiscoveryRepository(
            service: JamendoTracksService,
            trackMapper: TrackMapper
        ): DiscoveryRepository = DiscoveryRepository(service, trackMapper)
    }
}


