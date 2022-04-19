package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPlantsUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = plantRepository.getPlants(
        page = page,
        size = size,
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )

    suspend operator fun invoke(
        page: Int,
        size: Int,
        isTrending: Boolean? = null,
        forBeginner: Boolean? = null,
        searchQuery: String? = null
    ) = getPlants(
        page = page,
        size = size,
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )
}