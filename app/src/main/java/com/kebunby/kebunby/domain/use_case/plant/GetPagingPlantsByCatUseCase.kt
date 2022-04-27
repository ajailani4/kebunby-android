package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPagingPlantsByCatUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun getPlantsPagingByCat(categoryId: Int) =
        plantRepository.getPagingPlantsByCat(
            categoryId = categoryId
        )

    operator fun invoke(categoryId: Int) =
        getPlantsPagingByCat(
            categoryId = categoryId
        )
}