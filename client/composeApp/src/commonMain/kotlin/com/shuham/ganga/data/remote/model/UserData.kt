package com.shuham.ganga.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val _id: String,
    val name: String,
    val email: String,
    val role: String,
    val accessToken: String,
    val refreshToken: String
)