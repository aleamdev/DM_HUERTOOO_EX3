package com.dewis.huertohogar.data.local


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sku: String,
    val name: String,
    val imageUrl: String,
    val priceText: String,
    val stockText: String? = null,
    val description: String? = null,
    val isFeatured: Boolean = false,
    val oldPriceText: String? = null
)