package com.kebunby.kebunby.data.repository

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.request.UserActPlantRequest
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) : Flow<Resource<List<PlantItem>>>

    suspend fun getPlantCategories(): Flow<Resource<List<PlantCategory>>>

    suspend fun addUserPlantAct(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        userActPlantRequest: UserActPlantRequest
    ): Flow<Resource<Any>>

    suspend fun deleteUserPlantAct(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ): Flow<Resource<Any>>
}