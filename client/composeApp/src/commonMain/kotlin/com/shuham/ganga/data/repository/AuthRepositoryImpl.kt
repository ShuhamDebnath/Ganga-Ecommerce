package com.shuham.ganga.data.repository


import com.shuham.ganga.data.local.TokenManager
import com.shuham.ganga.data.remote.model.AuthResponse
import com.shuham.ganga.data.remote.model.LoginRequest
import com.shuham.ganga.data.remote.model.RegisterRequest
import com.shuham.ganga.domain.repository.AuthRepository
import com.shuham.ganga.utils.Constants
import com.shuham.ganga.utils.NetworkResult
import com.shuham.ganga.utils.PlatformConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val tokenManager: TokenManager
) : AuthRepository {

    private fun getBaseUrl(): String = PlatformConstants.BASE_URL

    override suspend fun login(request: LoginRequest): NetworkResult<AuthResponse> {
        return try {
            val response = client.post("${getBaseUrl()}auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val authResponse: AuthResponse = response.body()

            if (authResponse.success) {
                // Save tokens locally
                tokenManager.saveAuthData(
                    accessToken = authResponse.data?.accessToken ?: "",
                    refreshToken = authResponse.data?.refreshToken ?: "",
                    userName = authResponse.data?.name ?: "User"
                )
                NetworkResult.Success(authResponse)
            } else {
                NetworkResult.Error(authResponse.message ?: "Unknown Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.message ?: "Network Error")
        }
    }

    override suspend fun register(request: RegisterRequest): NetworkResult<AuthResponse> {
        // Similar logic for register... (omitted for brevity, use same pattern)
        return try {
            val response = client.post("${getBaseUrl()}auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val authResponse: AuthResponse = response.body()

            if (authResponse.success) {
                tokenManager.saveAuthData(
                    accessToken = authResponse.data?.accessToken ?: "",
                    refreshToken = authResponse.data?.refreshToken ?: "",
                    userName = authResponse.data?.name ?: "User"
                )
                NetworkResult.Success(authResponse)
            } else {
                NetworkResult.Error(authResponse.message ?: "Unknown Error")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.message ?: "Network Error")
        }
    }
}