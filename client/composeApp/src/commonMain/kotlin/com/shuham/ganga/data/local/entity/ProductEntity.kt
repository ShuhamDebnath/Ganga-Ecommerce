package com.shuham.ganga.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val vendorId: String?,
    val title: String,
    val description: String,
    val price: Double,
    val discountPrice: Double,
    val images: List<String>,
    val category: String,
    val rating: Double,
    val reviewCount: Int,
    val lastUpdated: Long = 0
)