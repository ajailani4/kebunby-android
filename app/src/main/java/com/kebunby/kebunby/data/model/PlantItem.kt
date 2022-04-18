package com.kebunby.kebunby.data.model

data class PlantItem(
    val id: Int,
    val name: String,
    val image: String?,
    val category: String,
    val wateringFreq: String,
    val popularity: Int,
    val isFavorited: Boolean
)
