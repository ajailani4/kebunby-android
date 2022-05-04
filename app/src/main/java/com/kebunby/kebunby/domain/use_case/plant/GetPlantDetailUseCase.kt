package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPlantDetailUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun getPlantDetail(id: Int) = plantRepository.getPlantDetail(id)

    operator fun invoke(id: Int) = getPlantDetail(id)
}