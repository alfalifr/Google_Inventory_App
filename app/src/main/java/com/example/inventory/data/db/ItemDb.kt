package com.example.inventory.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inventory.data.model.Item

@Database(
  entities = [Item::class],
  version = 1,
  exportSchema = false,
)
abstract class ItemDb: RoomDatabase() {
  abstract fun itemDao(): ItemDao

  companion object {
    @Volatile
    private var INSTANCE: ItemDb? = null

    operator fun get(context: Context): ItemDb =
      INSTANCE ?: synchronized(this) {
        val instance: ItemDb = Room
          .databaseBuilder(
            context,
            ItemDb::class.java,
            "app_db"
          )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
  }
}