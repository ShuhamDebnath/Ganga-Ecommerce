package com.shuham.ganga.di

import com.shuham.ganga.data.remote.getHttpClientEngine
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        //HttpClient(getHttpClientEngine()) {
        HttpClient(get()) {

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            // 2. Logging (Napier)
//            install(Logging) {
//                logger = object : Logger {
//                    override fun log(message: String) {
//                        Napier.d(message, tag = "HTTP Client")
//                    }
//                }
//                level = LogLevel.BODY
//            }

            // 3. Default URL (We will handle platform specifics in the Repository or here)
            defaultRequest {
                url("http://localhost:8000/api/v1/")
            }
        }
    }.also {
        // Init Logging
        Napier.base(DebugAntilog())
    }
}