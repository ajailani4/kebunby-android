package com.kebunby.kebunby.data.data_source.remote

import com.kebunby.kebunby.data.api.PlantService
import com.kebunby.kebunby.data.model.request.PlantActRequest
import javax.inject.Inject

class PlantRemoteDataSource @Inject constructor(
    private val plantService: PlantService
) {
    suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) =
        plantService.getPlants(
            page = page,
            size = size,
            isTrending = isTrending,
            forBeginner = forBeginner,
            searchQuery = searchQuery
        )

    suspend fun getPlantCategories() = plantService.getPlantCategories()

    suspend fun addPlantActivity(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        plantActRequest: PlantActRequest
    ) =
        plantService.addPlantActivity(
            username = username,
            isPlanting = isPlanting,
            isPlanted = isPlanted,
            isFavorited = isFavorited,
            plantActRequest = plantActRequest
        )

    suspend fun deletePlantActivity(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ) =
        plantService.deletePlantActivity(
            username = username,
            plantId = plantId,
            isPlanting = isPlanting,
            isPlanted = isPlanted,
            isFavorited = isFavorited
        )
}