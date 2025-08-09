package com.example.mymusic.di

import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.data.repository.TrackRepositoryImpl
import com.example.mymusic.domain.repository.TrackRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    }
}


