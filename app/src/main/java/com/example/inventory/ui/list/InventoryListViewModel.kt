package com.example.inventory.ui.list

import androidx.lifecycle.*
import com.example.inventory.data.db.ItemDao
import com.example.inventory.data.model.Item

class InventoryListViewModel(
  private val dao: ItemDao
): ViewModel() {

  //private val _inventories = MutableLiveData<List<Item>>()
  val inventories: LiveData<List<Item>> by lazy {
    dao.getAllInventory().asLiveData()
  }
/*
  private var flowJob: Job? = null

  fun getInventories() {
    flowJob?.cancel()
    flowJob = viewModelScope.launch {
      dao.getAllInventory().collect {
        _inventories.value = it
      }
    }
  }
 */
}