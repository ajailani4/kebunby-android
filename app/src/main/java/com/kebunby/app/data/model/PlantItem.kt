package com.kebunby.app.data.model

data class PlantItem(
    val id: Int,
    val name: String,
    val image: String?,
    val category: PlantCategory,
    val growthEst: String,
    val wateringFreq: String,
    val popularity: Int,
    var isFavorited: Boolean
)
