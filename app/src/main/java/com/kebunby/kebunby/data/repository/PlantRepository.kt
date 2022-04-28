package com.kebunby.kebunby.data.repository

import androidx.paging.PagingData
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.request.PlantActRequest
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ): Flow<Resource<List<PlantItem>>>

    fun getPagingPlants(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ): Flow<PagingData<PlantItem>>

    fun getPagingPlantsByCategory(categoryId: Int): Flow<PagingData<PlantItem>>

    fun getPlantCategories(): Flow<Resource<List<PlantCategory>>>

    fun addPlantActivity(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        plantActRequest: PlantActRequest
    ): Flow<Resource<Any>>

    fun deletePlantActivity(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ): Flow<Resource<Any>>
}