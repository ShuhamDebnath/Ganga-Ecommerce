package com.shuham.ganga.di

import com.shuham.ganga.data.local.TokenManager
import com.shuham.ganga.utils.PlatformConstants
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import com.shuham.ganga.data.remote.model.RefreshTokenRequest
import com.shuham.ganga.data.remote.model.RefreshTokenResponse

val networkModule = module {
    single {

        val tokenManager: TokenManager = get()
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

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenManager.getAccessToken()
                        val refreshToken = tokenManager.getRefreshToken()
                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else null
                    }

                    refreshTokens {
                        val refreshToken = tokenManager.getRefreshToken() ?: return@refreshTokens null

                        try {
                            // Use a separate client or manual request to avoid loops
                            val refreshResponse: RefreshTokenResponse = client.post("${PlatformConstants.BASE_URL}auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshTokenRequest(refreshToken))
                                markAsRefreshTokenRequest()
                            }.body()

                            if (refreshResponse.success && refreshResponse.accessToken != null) {
                                // Save new token
                                tokenManager.saveAuthData(
                                    accessToken = refreshResponse.accessToken,
                                    refreshToken = refreshToken, // Keep old refresh token
                                    userName = tokenManager.getUserName() ?: ""
                                )
                                BearerTokens(refreshResponse.accessToken, refreshToken)
                            } else {
                                null // Refresh failed -> Log out
                            }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }
        }
    }.also {
        // Init Logging
        Napier.base(DebugAntilog())
    }
}