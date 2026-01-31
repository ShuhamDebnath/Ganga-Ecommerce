package com.shuham.ganga.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "customer",
    @SerialName("store_name")
    val storeName: String? = null
)