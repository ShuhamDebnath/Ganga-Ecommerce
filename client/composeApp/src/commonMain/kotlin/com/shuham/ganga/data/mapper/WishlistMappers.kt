package com.shuham.ganga.data.mapper

import com.shuham.ganga.data.local.entity.WishlistEntity
import com.shuham.ganga.domain.model.Product

fun WishlistEntity.toDomain(): Product {
    return Product(
        id = productId,
        title = title,
        price = price,
        images = listOf(imageUrl),
        rating = rating,
        description = "",
        discountPrice = 0.0,
        category = "",
        vendorId = null,
        reviewCount = 0
    )
}

fun Product.toWishlistEntity(): WishlistEntity {
    return WishlistEntity(
        productId = id,
        title = title,
        price = price,
        imageUrl = images.firstOrNull() ?: "",
        rating = rating
    )
}