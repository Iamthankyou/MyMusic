package com.example.mymusic

import android.app.Application
import com.example.mymusic.data.download.DownloadProgressMonitor
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyMusicApp : Application() {
    
    @Inject
    lateinit var downloadProgressMonitor: DownloadProgressMonitor
    
    override fun onCreate() {
        super.onCreate()
        downloadProgressMonitor.startMonitoring()
    }
}


