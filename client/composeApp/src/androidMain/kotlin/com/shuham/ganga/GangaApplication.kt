package com.shuham.ganga

import android.app.Application
import com.shuham.ganga.di.initKoin
import org.koin.android.ext.koin.androidContext

class GangaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@GangaApplication) // Crucial for Android
        }
    }
}