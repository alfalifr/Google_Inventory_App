package com.example.inventory.data.db

import androidx.room.*
import com.example.inventory.data.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
  @Query("SELECT * FROM item")
  fun getAllInventory(): Flow<List<Item>>

  @Query("SELECT * FROM item WHERE id = :id")
  fun getItemById(id: Int): Flow<Item>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertItem(newItem: Item): Long

  @Update
  suspend fun updateItem(newItem: Item): Int

  @Delete
  suspend fun deleteItem(item: Item): Int
}