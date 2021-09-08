package com.example.inventory.ui.add

import androidx.lifecycle.*
import com.example.inventory.data.db.ItemDao
import com.example.inventory.data.model.Item
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddItemViewModel(
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

  private val _onSave = MutableLiveData<Boolean>()
  val onSave: LiveData<Boolean>
    get() = _onSave


  fun addItem(item: Item) {
    viewModelScope.launch {
      _onSave.value = dao.insertItem(item) >= 0
    }
  }
  fun updateItem(item: Item) {
    viewModelScope.launch {
      _onSave.value = dao.updateItem(item) > 0
    }
  }
}