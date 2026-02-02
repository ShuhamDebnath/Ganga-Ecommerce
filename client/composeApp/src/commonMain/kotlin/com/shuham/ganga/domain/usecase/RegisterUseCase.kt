package com.shuham.ganga.domain.usecase


import com.shuham.ganga.data.remote.model.AuthResponse
import com.shuham.ganga.data.remote.model.RegisterRequest
import com.shuham.ganga.domain.repository.AuthRepository
import com.shuham.ganga.utils.NetworkResult

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: RegisterRequest): NetworkResult<AuthResponse> {
        return repository.register(request)
    }
}