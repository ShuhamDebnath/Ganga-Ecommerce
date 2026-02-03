package com.shuham.ganga.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.shuham.ganga.data.local.dao.CartDao
import com.shuham.ganga.data.local.dao.ProductDao
import com.shuham.ganga.data.local.dao.WishlistDao
import com.shuham.ganga.data.local.entity.CartEntity
import com.shuham.ganga.data.local.entity.ProductEntity
import com.shuham.ganga.data.local.entity.WishlistEntity

@Database(entities = [CartEntity::class, ProductEntity::class, WishlistEntity::class], version = 3)
@TypeConverters(Converters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao
    abstract fun wishlistDao(): WishlistDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}