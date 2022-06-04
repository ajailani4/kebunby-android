package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPagingPlantsByCategoryUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(categoryId: Int) =
        plantRepository.getPagingPlantsByCategory(
            categoryId = categoryId
        )
}