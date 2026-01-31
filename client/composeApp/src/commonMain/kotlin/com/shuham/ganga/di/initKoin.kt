package com.shuham.ganga.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        Napier.base(DebugAntilog())
        config?.invoke(this)
        modules(sharedModules)
    }
}
