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

package com.example.inventory.ui.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.R
import com.example.inventory.data.db.ItemDb
import com.example.inventory.data.model.Item
import com.example.inventory.data.model.getPriceStr
import com.example.inventory.databinding.FragmentItemDetailBinding
import com.example.inventory.ui.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * [ItemDetailFragment] displays the details of the selected item.
 */
class ItemDetailFragment : Fragment() {
    private val navigationArgs: ItemDetailFragmentArgs by navArgs()

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory(
            ItemDb[requireContext()].itemDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.apply {
            editItem.setOnClickListener { editItem() }
            deleteItem.setOnClickListener { deleteItem() }
            sellItem.setOnClickListener { sellItem() }
        }
        viewModel.apply {
            item.observe(viewLifecycleOwner) {
                if(it != null) {
                    bindData(it)
                }
            }
            onDelete.observe(viewLifecycleOwner) {
                if(it == true) {
                    findNavController().navigateUp()
                }
            }
            itemId.value = navigationArgs.itemId
        }
    }

    private fun bindData(data: Item) {
        binding.apply {
            data.apply {
                itemName.text = name
                itemCount.text = quantity.toString()
                itemPrice.text = getPriceStr()
            }
        }
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteItem()
            }
            .show()
    }

    /**
     * Deletes the current item and navigates to the list fragment.
     */
    private fun deleteItem() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(android.R.string.dialog_alert_title)
            .setMessage(R.string.delete_question)
            .setPositiveButton(R.string.yes) { _, _ ->
                viewModel.deleteCurrentItem()
            }.setNegativeButton(R.string.no) { _, _ -> }
            .show()
    }

    private fun sellItem() {
        if(checkItemAvailability()) {
            viewModel.sellCurrentItem()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.insufficient_stock),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkItemAvailability(): Boolean =
        viewModel.item.value?.quantity?.compareTo(0) == 1

    private fun editItem() {
        val action = ItemDetailFragmentDirections.actionItemDetailFragmentToAddItemFragment(
            getString(R.string.edit_fragment_title),
            navigationArgs.itemId,
        )
        findNavController().navigate(action)
    }

    /**
     * Called when fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
