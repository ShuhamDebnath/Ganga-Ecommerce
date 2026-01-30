package com.shuham.ganga.di

import com.shuham.ganga.data.repository.AuthRepositoryImpl
import com.shuham.ganga.domain.repository.AuthRepository
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

//expect fun platformModule(): Module

val appModule = module {
    // 1. Repository
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

}

