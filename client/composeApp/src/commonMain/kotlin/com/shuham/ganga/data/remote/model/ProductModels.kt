package com.shuham.ganga.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val success: Boolean,
    val data: List<ProductDto>,
    val pagination: PaginationDto? = null
)

@Serializable
data class SingleProductResponse(
    val success: Boolean,
    val data: ProductDto?
)


@Serializable
data class ProductDto(
    @SerialName("_id") val id: String,
    val title: String,
    val description: String,
    val price: Double,
    @SerialName("discount_price") val discountPrice: Double = 0.0,
    val images: List<String>,
    val category: String,
    val rating: RatingDto? = null
)

@Serializable
data class RatingDto(
    val average: Double = 0.0,
    val count: Int = 0
)

@Serializable
data class PaginationDto(
    val total: Int,
    val page: Int,
    val pages: Int
)