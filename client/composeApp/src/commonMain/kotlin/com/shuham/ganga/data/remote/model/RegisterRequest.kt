package com.shuham.ganga.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "customer",
    val store_name: String? = null
)