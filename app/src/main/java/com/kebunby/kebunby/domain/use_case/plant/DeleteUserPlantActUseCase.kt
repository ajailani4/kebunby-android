package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class DeleteUserPlantActUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private suspend fun deleteUserPlantAct(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ) = plantRepository.deleteUserPlantAct(
        username = username,
        plantId = plantId,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited
    )

    suspend operator fun invoke(
        username: String,
        plantId: Int,
        isPlanting: Boolean? = null,
        isPlanted: Boolean? = null,
        isFavorited: Boolean? = null
    ) = deleteUserPlantAct(
        username = username,
        plantId = plantId,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited
    )
}