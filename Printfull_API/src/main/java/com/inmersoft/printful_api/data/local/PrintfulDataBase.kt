package com.inmersoft.printful_api.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inmersoft.printful_api.data.local.dao.FavoriteProductsDao
import com.inmersoft.printful_api.data.local.dao.ProductInCartDao
import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.data.local.model.ProductInCart

@Database(
    entities = [ProductInCart::class, FavoriteProduct::class],
    version = 1,
    exportSchema = false
)
abstract class PrintfulDataBase : RoomDatabase() {

    abstract fun productsInCartDao(): ProductInCartDao
    abstract fun favoriteProductsDao(): FavoriteProductsDao

    companion object {
        @Volatile
        private var INSTANCE: PrintfulDataBase? = null

        fun getDatabase(context: Context): PrintfulDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): PrintfulDataBase {
            return Room.databaseBuilder(context, PrintfulDataBase::class.java, "ecommerce-chart.db")
                .build()
        }
    }

}