package com.kebunby.app.domain.use_case.plant

import com.kebunby.app.data.repository.PlantRepository
import javax.inject.Inject

class DeletePlantUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(id: Int) = plantRepository.deletePlant(id)
}