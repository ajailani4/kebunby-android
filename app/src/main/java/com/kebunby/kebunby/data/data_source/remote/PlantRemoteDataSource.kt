package com.kebunby.kebunby.data.data_source.remote

import com.kebunby.kebunby.data.api.PlantService
import com.kebunby.kebunby.data.model.request.UserActPlantRequest
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

    suspend fun addUserPlantAct(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        userActPlantRequest: UserActPlantRequest
    ) =
        plantService.addUserPlantAct(
            username = username,
            isPlanting = isPlanting,
            isPlanted = isPlanted,
            isFavorited = isFavorited,
            userActPlantRequest = userActPlantRequest
        )

    suspend fun deleteUserPlantAct(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ) =
        plantService.deleteUserPlantAct(
            username = username,
            plantId = plantId,
            isPlanting = isPlanting,
            isPlanted = isPlanted,
            isFavorited = isFavorited
        )
}