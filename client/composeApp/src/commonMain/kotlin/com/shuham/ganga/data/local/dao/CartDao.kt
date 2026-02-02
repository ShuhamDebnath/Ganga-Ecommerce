package com.shuham.ganga.data.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shuham.ganga.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY addedAt DESC")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemById(productId: String): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartEntity)

    @Delete
    suspend fun deleteCartItem(item: CartEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: String, quantity: Int)

    @Query("SELECT SUM(price * quantity) FROM cart_items")
    fun getTotalPrice(): Flow<Double?>
}