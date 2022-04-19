package com.kebunby.kebunby.data.data_source.remote

import com.kebunby.kebunby.data.api.UserService
import javax.inject.Inject

class PlantRemoteDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) =
        userService.getPlants(
            page = page,
            size = size,
            isTrending = isTrending,
            forBeginner = forBeginner,
            searchQuery = searchQuery
        )

    suspend fun getPlantCategories() = userService.getPlantCategories()
}