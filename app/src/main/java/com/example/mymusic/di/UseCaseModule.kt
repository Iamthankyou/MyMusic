package com.example.mymusic.di

import com.example.mymusic.data.local.SearchHistoryDao
import com.example.mymusic.data.repository.DetailRepository
import com.example.mymusic.data.repository.SearchRepository
import com.example.mymusic.domain.usecase.DetailUseCase
import com.example.mymusic.domain.usecase.SearchHistoryUseCase
import com.example.mymusic.domain.usecase.SearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    @Singleton
    fun provideSearchUseCase(searchRepository: SearchRepository): SearchUseCase {
        return SearchUseCase(searchRepository)
    }
    
    @Provides
    @Singleton
    fun provideSearchHistoryUseCase(searchHistoryDao: SearchHistoryDao): SearchHistoryUseCase {
        return SearchHistoryUseCase(searchHistoryDao)
    }
    
    @Provides
    @Singleton
    fun provideDetailUseCase(detailRepository: DetailRepository): DetailUseCase {
        return DetailUseCase(detailRepository)
    }
}
