package com.shuham.ganga.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.shuham.ganga.data.local.AppDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val platformModule: Module = module {

    single<AppDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = if (os.contains("win")) {
            File(System.getenv("APPDATA"), "Ganga")
        } else {
            File(userHome, ".ganga")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }
        val dbFile = File(appDataDir, "ganga.db")
        Room.databaseBuilder<AppDatabase>(
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
    }

    single<HttpClientEngine> { OkHttp.create() }
}