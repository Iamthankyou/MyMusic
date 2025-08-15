package com.example.mymusic.di

import android.content.Context
import com.example.mymusic.data.local.AppDatabase
import com.example.mymusic.data.local.SearchHistoryDao
import com.example.mymusic.data.local.DownloadDao
import com.example.mymusic.data.mapper.AlbumMapper
import com.example.mymusic.data.mapper.ArtistMapper
import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.data.repository.DetailRepository
import com.example.mymusic.data.repository.DiscoveryRepository
import com.example.mymusic.data.repository.SearchRepository
import com.example.mymusic.data.repository.TrackRepositoryImpl
import com.example.mymusic.data.repository.DownloadRepositoryImpl
import com.example.mymusic.domain.repository.TrackRepository
import com.example.mymusic.domain.repository.DownloadRepository
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
    
    @Binds
    @Singleton
    abstract fun bindDownloadRepository(impl: DownloadRepositoryImpl): DownloadRepository

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
        fun provideArtistMapper(): ArtistMapper = ArtistMapper
        
        @Provides
        @Singleton
        fun provideAlbumMapper(): AlbumMapper = AlbumMapper
        
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
        fun provideDownloadDao(database: AppDatabase): DownloadDao =
            database.downloadDao()
            
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
        
        @Provides
        @Singleton
        fun provideDetailRepository(
            service: JamendoTracksService,
            trackMapper: TrackMapper,
            artistMapper: ArtistMapper,
            albumMapper: AlbumMapper
        ): DetailRepository = DetailRepository(service, trackMapper, artistMapper, albumMapper)
        
        @Provides
        @Singleton
        fun provideDownloadProgressMonitor(
            @ApplicationContext context: Context,
            downloadRepository: DownloadRepository
        ): com.example.mymusic.data.download.DownloadProgressMonitor =
            com.example.mymusic.data.download.DownloadProgressMonitor(context, downloadRepository)
    }
}


