package com.shuham.ganga.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val success: Boolean,
    val accessToken: String? = null,
    val message: String? = null
)