package com.shuham.ganga.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val productId: String,
    val title: String,
    val price: Double,
    val imageUrl: String,
    val rating: Double
)