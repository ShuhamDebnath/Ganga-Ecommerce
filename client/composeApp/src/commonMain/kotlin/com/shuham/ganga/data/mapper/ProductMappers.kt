package com.shuham.ganga.data.mapper

import com.shuham.ganga.data.local.entity.ProductEntity
import com.shuham.ganga.data.remote.model.ProductDto
import com.shuham.ganga.domain.model.Product

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        vendorId = vendorId,
        title = title,
        description = description,
        price = price,
        discountPrice = discountPrice,
        images = images, // Direct mapping
        category = category,
        rating = rating?.average ?: 0.0,
        reviewCount = rating?.count ?: 0
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        vendorId = vendorId,
        title = title,
        description = description,
        price = price,
        discountPrice = discountPrice,
        images = images, // Direct mapping
        category = category,
        rating = rating,
        reviewCount = reviewCount
    )
}

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        vendorId = vendorId,
        title = title,
        description = description,
        price = price,
        discountPrice = discountPrice,
        images = images,
        category = category,
        rating = rating?.average ?: 0.0,
        reviewCount = rating?.count ?: 0
    )
}