package com.kebunby.kebunby.data.data_source.remote

import com.kebunby.kebunby.data.api.PlantService
import com.kebunby.kebunby.data.api.UserService
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
}