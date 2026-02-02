package com.shuham.ganga.domain.usecase

import com.shuham.ganga.data.remote.model.AuthResponse
import com.shuham.ganga.data.remote.model.LoginRequest
import com.shuham.ganga.domain.repository.AuthRepository
import com.shuham.ganga.utils.NetworkResult

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: LoginRequest): NetworkResult<AuthResponse> {
        // Here you could add validation logic before calling repo
        return repository.login(request)
    }
}