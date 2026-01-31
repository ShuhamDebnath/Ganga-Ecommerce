package com.shuham.ganga.data.repository


import com.shuham.ganga.data.remote.model.AuthResponse
import com.shuham.ganga.data.remote.model.LoginRequest
import com.shuham.ganga.data.remote.model.RegisterRequest
import com.shuham.ganga.domain.repository.AuthRepository
import com.shuham.ganga.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepositoryImpl(
    private val client: HttpClient
) : AuthRepository {

    private fun getBaseUrl(): String {
        return Constants.BASE_URL_ANDROID
    }

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = client.post("${getBaseUrl()}auth/login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val authResponse: AuthResponse = response.body()

            if (authResponse.success) {
                Result.success(authResponse)
            } else {
                Result.failure(Exception(authResponse.message ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = client.post("${getBaseUrl()}auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val authResponse: AuthResponse = response.body()

            if (authResponse.success) {
                Result.success(authResponse)
            } else {
                Result.failure(Exception(authResponse.message ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}