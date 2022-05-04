package com.kebunby.kebunby.data.model

data class Plant(
    val id: Int,
    val name: String,
    val latinName: String,
    val image: String,
    val category: String,
    val wateringFreq: String,
    val growthEst: String,
    val tools: List<String>,
    val materials: List<String>,
    val steps: List<String>,
    val popularity: Int,
    val author: String,
    val publishedOn: String
)
