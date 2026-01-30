package com.shuham.ganga.domain.repository

import com.shuham.ganga.data.remote.model.AuthResponse
import com.shuham.ganga.data.remote.model.LoginRequest
import com.shuham.ganga.data.remote.model.RegisterRequest

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<AuthResponse>
}