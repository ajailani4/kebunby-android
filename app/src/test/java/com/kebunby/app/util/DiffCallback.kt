package com.kebunby.app.util

import androidx.recyclerview.widget.DiffUtil
import com.kebunby.app.data.model.PlantItem

class DiffCallback : DiffUtil.ItemCallback<PlantItem>() {
    override fun areItemsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlantItem, newItem: PlantItem): Boolean {
        return oldItem == newItem
    }
}