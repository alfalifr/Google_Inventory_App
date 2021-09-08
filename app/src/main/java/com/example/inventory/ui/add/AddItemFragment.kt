/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory.ui.add

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.R
import com.example.inventory.data.db.ItemDb
import com.example.inventory.data.model.Item
import com.example.inventory.databinding.FragmentAddItemBinding
import com.example.inventory.ui.ViewModelFactory
import com.example.inventory.ui.detail.ItemDetailFragmentArgs

/**
 * Fragment to add or update an item in the Inventory database.
 */
class AddItemFragment : Fragment() {

    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddItemViewModel by viewModels {
        ViewModelFactory(
            ItemDb[requireContext()].itemDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.apply {
            saveAction.setOnClickListener { saveItem() }
        }
        viewModel.apply {
            item.observe(viewLifecycleOwner) {
                bindData(it)
            }
            onSave.observe(viewLifecycleOwner) {
                if(it == true) {
                    findNavController().navigateUp()
                }
            }
            if(navigationArgs.itemId > 0) {
                itemId.value = navigationArgs.itemId
            }
        }
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

    private fun bindData(data: Item?) {
        binding.apply {
            data?.apply {
                itemName.setText(name)
                itemCount.setText(quantity.toString())
                itemPrice.setText(price.toString())
            } ?: run {
                itemName.text = null
                itemCount.text = null
                itemPrice.text = null
            }
        }
    }

    private fun saveItem() {
        if(isItemValid()) {
            binding.apply {
                var id = navigationArgs.itemId
                if(id == -1) {
                    id = 0
                }
                val item = Item(
                    id = id,
                    name = itemName.text.toString(),
                    quantity = itemCount.text.toString().toInt(),
                    price = itemPrice.text.toString().toDouble(),
                )
                if(id <= 0) {
                    viewModel.addItem(item)
                } else {
                    viewModel.updateItem(item)
                }
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_item_field),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun isItemValid(): Boolean = binding.run {
        if(itemName.text?.isNotBlank() != true) return false
        if(itemPrice.text?.isNotBlank() != true) return false
        if(itemCount.text?.isNotBlank() != true) return false
        true
    }
}
