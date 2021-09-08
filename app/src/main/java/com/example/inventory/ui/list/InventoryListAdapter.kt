package com.example.inventory.ui.list

import android.icu.number.NumberFormatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.model.Item
import com.example.inventory.databinding.ItemListItemBinding
import java.text.NumberFormat

class InventoryListAdapter(
  private val onItemClick: (Item) -> Unit,
) : ListAdapter<Item, InventoryListAdapter.ViewHolder>(
    ItemDiffUtil
  ) {
  inner class ViewHolder(val binding: ItemListItemBinding)
    : RecyclerView.ViewHolder(binding.root)

  companion object ItemDiffUtil: DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(
      oldItem: Item,
      newItem: Item
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
      oldItem: Item,
      newItem: Item
    ): Boolean = oldItem == newItem
  }

  private val currencyFormatter: NumberFormat by lazy {
    NumberFormat.getCurrencyInstance()
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder = ViewHolder(
    ItemListItemBinding.inflate(
      LayoutInflater.from(parent.context)
    )
  )

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    holder.binding.apply {
      val data = getItem(position)
      itemName.text = data.name
      itemPrice.text = currencyFormatter.format(data.price)
      itemQuantity.text = data.quantity.toString()
      root.setOnClickListener {
        onItemClick(data)
      }
    }
  }
}