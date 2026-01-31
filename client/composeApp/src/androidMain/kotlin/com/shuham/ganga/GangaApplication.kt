package com.shuham.ganga

import android.app.Application
import com.shuham.ganga.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class GangaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@GangaApplication) // Crucial for Android
        }
    }
}