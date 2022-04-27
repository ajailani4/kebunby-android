package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class AddPlantActivityUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun addPlantActivity(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        plantActRequest: PlantActRequest
    ) = plantRepository.addPlantActivity(
        username = username,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited,
        plantActRequest = plantActRequest
    )

    operator fun invoke(
        username: String,
        isPlanting: Boolean? = null,
        isPlanted: Boolean? = null,
        isFavorited: Boolean? = null,
        plantActRequest: PlantActRequest
    ) = addPlantActivity(
        username = username,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited,
        plantActRequest = plantActRequest
    )
}