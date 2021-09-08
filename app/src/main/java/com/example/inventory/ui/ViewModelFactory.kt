package com.example.inventory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inventory.data.db.ItemDao
import com.example.inventory.ui.add.AddItemViewModel
import com.example.inventory.ui.detail.DetailViewModel
import com.example.inventory.ui.list.InventoryListViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(
  private val itemDao: ItemDao,
): ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
    modelClass.isAssignableFrom(InventoryListViewModel::class.java) -> {
      InventoryListViewModel(itemDao)
    }
    modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
      DetailViewModel(itemDao)
    }
    modelClass.isAssignableFrom(AddItemViewModel::class.java) -> {
      AddItemViewModel(itemDao)
    }
    else -> throw IllegalArgumentException(
      "No such view model class `modelClass` of '$modelClass'"
    )
  } as T
}