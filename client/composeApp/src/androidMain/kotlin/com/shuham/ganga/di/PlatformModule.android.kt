package com.shuham.ganga.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.shuham.ganga.data.local.AppDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module{

    single<AppDatabase> {
        // Android specific database builder
        val dbFile = androidContext().getDatabasePath("ganga.db")

        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver()) // The KMP SQLite Driver
            .fallbackToDestructiveMigration(true) // Clears DB if schema changes (good for dev)
            .build()
    }

    single<HttpClientEngine> { OkHttp.create() }

}