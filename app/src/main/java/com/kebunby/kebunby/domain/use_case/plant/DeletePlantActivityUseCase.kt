package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class DeletePlantActivityUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(
        username: String,
        plantId: Int,
        isPlanting: Boolean? = null,
        isPlanted: Boolean? = null,
        isFavorited: Boolean? = null
    ) = plantRepository.deletePlantActivity(
        username = username,
        plantId = plantId,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited
    )
}