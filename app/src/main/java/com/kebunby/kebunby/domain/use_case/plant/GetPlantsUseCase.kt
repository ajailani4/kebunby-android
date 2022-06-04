package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPlantsUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(
        page: Int,
        size: Int,
        isTrending: Boolean? = null,
        forBeginner: Boolean? = null,
        searchQuery: String? = null
    ) = plantRepository.getPlants(
        page = page,
        size = size,
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )
}