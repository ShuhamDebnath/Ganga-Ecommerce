package com.shuham.ganga.domain.model

data class Product(
    val id: String,
    val vendorId: String?,
    val title: String,
    val description: String,
    val price: Double,
    val discountPrice: Double,
    val images: List<String>, // Changed from imageUrl: String to images: List<String>
    val category: String,
    val rating: Double,
    val reviewCount: Int
)