package com.shuham.ganga.di

import com.shuham.ganga.data.local.AppDatabase
import org.koin.dsl.module


val databaseModule = module {
    // Provide the MovieDao by getting the AppDatabase instance
    single { get<AppDatabase>().cartDao() }
}