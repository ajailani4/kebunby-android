package com.kebunby.kebunby.data.data_source.remote

import com.kebunby.kebunby.data.api.ApiService
import javax.inject.Inject

class PlantRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) =
        apiService.getPlants(
            page = page,
            size = size,
            isTrending = isTrending,
            forBeginner = forBeginner,
            searchQuery = searchQuery
        )
}