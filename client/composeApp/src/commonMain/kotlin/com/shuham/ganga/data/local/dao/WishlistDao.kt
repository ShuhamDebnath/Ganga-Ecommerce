package com.shuham.ganga.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shuham.ganga.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist")
    fun getWishlist(): Flow<List<WishlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistEntity)

    @Delete
    suspend fun removeFromWishlist(item: WishlistEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE productId = :id)")
    fun isProductInWishlist(id: String): Flow<Boolean>
}