package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class DeletePlantUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(id: Int) = plantRepository.deletePlant(id)
}