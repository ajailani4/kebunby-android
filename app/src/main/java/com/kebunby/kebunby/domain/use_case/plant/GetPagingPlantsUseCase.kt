package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPagingPlantsUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun getPagingPlants(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = plantRepository.getPagingPlants(
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )

    operator fun invoke(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = getPagingPlants(
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )
}