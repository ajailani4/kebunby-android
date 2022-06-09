package com.kebunby.app.domain.use_case.plant

import com.kebunby.app.data.repository.PlantRepository
import javax.inject.Inject

class GetPagingPlantsUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = plantRepository.getPagingPlants(
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )
}