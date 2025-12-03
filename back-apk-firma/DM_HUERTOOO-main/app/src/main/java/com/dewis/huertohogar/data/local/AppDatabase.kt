package com.dewis.huertohogar.data.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Product::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    companion object {
        @Volatile private var I: AppDatabase? = null
        fun get(ctx: Context) = I ?: synchronized(this) {
            I ?: Room.databaseBuilder(ctx, AppDatabase::class.java, "app.db")
                .fallbackToDestructiveMigration()
                .build().also { I = it }
        }
    }
}