package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPlantCategoriesUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun getPlantCategories() =
        plantRepository.getPlantCategories()

    operator fun invoke() = getPlantCategories()
}