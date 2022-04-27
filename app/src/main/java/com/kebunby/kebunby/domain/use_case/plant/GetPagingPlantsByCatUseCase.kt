package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPagingPlantsByCatUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun getPlantsPagingByCat(
        categoryId: Int,
        page: Int,
        size: Int
    ) = plantRepository.getPagingPlantsByCat(
        categoryId = categoryId,
        page = page,
        size = size
    )

    operator fun invoke(
        categoryId: Int,
        page: Int,
        size: Int
    ) = getPlantsPagingByCat(
        categoryId = categoryId,
        page = page,
        size = size
    )
}