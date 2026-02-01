package com.shuham.ganga.domain.repository

import com.shuham.ganga.data.remote.model.AuthResponse
import com.shuham.ganga.data.remote.model.LoginRequest
import com.shuham.ganga.data.remote.model.RegisterRequest
import com.shuham.ganga.utils.NetworkResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): NetworkResult<AuthResponse>
    suspend fun register(request: RegisterRequest): NetworkResult<AuthResponse>
}