package com.kebunby.kebunby.data.repository

import androidx.paging.PagingData
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.request.PlantActRequest
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ): Flow<Resource<List<PlantItem>>>

    fun getPlantsByPaging(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ): Flow<PagingData<PlantItem>>

    suspend fun getPlantCategories(): Flow<Resource<List<PlantCategory>>>

    suspend fun addPlantActivity(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        plantActRequest: PlantActRequest
    ): Flow<Resource<Any>>

    suspend fun deletePlantActivity(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ): Flow<Resource<Any>>
}