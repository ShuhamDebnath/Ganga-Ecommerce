package com.shuham.ganga.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("_id")
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val accessToken: String,
    val refreshToken: String
)