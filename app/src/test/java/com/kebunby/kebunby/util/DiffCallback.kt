package com.kebunby.kebunby.util

import androidx.recyclerview.widget.DiffUtil
import com.kebunby.kebunby.data.model.PlantItem

class DiffCallback : DiffUtil.ItemCallback<PlantItem>() {
    override fun areItemsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
        return oldItem == newItem
    }
}