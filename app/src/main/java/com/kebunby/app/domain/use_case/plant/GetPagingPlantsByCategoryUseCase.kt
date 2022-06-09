package com.kebunby.app.domain.use_case.plant

import com.kebunby.app.data.repository.PlantRepository
import javax.inject.Inject

class GetPagingPlantsByCategoryUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(categoryId: Int) =
        plantRepository.getPagingPlantsByCategory(
            categoryId = categoryId
        )
}