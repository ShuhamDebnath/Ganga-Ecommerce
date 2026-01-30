package com.shuham.ganga.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val success: Boolean,
    val data: UserData? = null,
    val message: String? = null
)