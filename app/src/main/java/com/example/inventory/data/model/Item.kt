package com.example.inventory.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity
data class Item(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val name: String,
  val quantity: Int,
  val price: Double,
)

fun Item.getPriceStr(): String = NumberFormat
  .getCurrencyInstance()
  .format(price)