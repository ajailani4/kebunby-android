package com.kebunby.kebunby.data.repository

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantItem
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) : Flow<Resource<List<PlantItem>>>
}