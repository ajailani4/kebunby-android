package com.kebunby.kebunby.data.repository

import androidx.paging.PagingData
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.Plant
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.request.PlantActRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

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

    fun getPlantDetail(id: Int): Flow<Resource<Plant>>

    fun getPlantActivities(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?
    ): Flow<PagingData<PlantItem>>

    fun uploadPlant(
        name: String,
        image: File,
        category: String,
        wateringFreq: String,
        growthEst: String,
        desc: String,
        tools: List<String>,
        materials: List<String>,
        steps: List<String>,
        author: String
    ): Flow<Resource<Any>>

    fun editPlant(
        id: Int,
        name: String,
        image: File?,
        category: String,
        wateringFreq: String,
        growthEst: String,
        desc: String,
        tools: List<String>,
        materials: List<String>,
        steps: List<String>,
        author: String,
        popularity: String,
        publishedOn: String
    ): Flow<Resource<Any>>

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