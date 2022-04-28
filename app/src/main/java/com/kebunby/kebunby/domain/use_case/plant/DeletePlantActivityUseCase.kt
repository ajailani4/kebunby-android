package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class DeletePlantActivityUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun deletePlantActivity(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ) = plantRepository.deletePlantActivity(
        username = username,
        plantId = plantId,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited
    )

    operator fun invoke(
        username: String,
        plantId: Int,
        isPlanting: Boolean? = null,
        isPlanted: Boolean? = null,
        isFavorited: Boolean? = null
    ) = deletePlantActivity(
        username = username,
        plantId = plantId,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited
    )
}