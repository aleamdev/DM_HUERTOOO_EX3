package com.dewis.dm_huertohogar_ex3.data.local

import androidx.room.*
import com.dewis.dm_huertohogar_ex3.data.local.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id DESC")
    fun observeAll(): Flow<List<Product>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(p: Product)
    @Query("DELETE FROM products")
    suspend fun clear()
}