package com.example.inventory.ui.detail

import androidx.lifecycle.*
import com.example.inventory.data.db.ItemDao
import com.example.inventory.data.model.Item
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailViewModel(
  private val dao: ItemDao,
): ViewModel() {
  val itemId = MutableLiveData<Int>()

  private val _item = MediatorLiveData<Item>().apply {
    addSource(itemId) {
      if(it != null) {
        viewModelScope.launch {
          dao.getItemById(it).collect { item ->
            value = item
          }
        }
      } else {
        value = null
      }
    }
  }
  val item: LiveData<Item>
    get() = _item


  private val _onDelete = MutableLiveData<Boolean>()
  val onDelete: LiveData<Boolean>
    get() = _onDelete


  fun deleteCurrentItem() {
    val item = _item.value
    if(item == null) {
      _onDelete.value = false
      return
    }
    viewModelScope.launch {
      _onDelete.value = dao.deleteItem(item) > 0
    }
  }

  fun sellCurrentItem() {
    val item = _item.value ?: return
    viewModelScope.launch {
      val newItem = item.copy(quantity = item.quantity-1)
      dao.updateItem(newItem)
    }
  }
}