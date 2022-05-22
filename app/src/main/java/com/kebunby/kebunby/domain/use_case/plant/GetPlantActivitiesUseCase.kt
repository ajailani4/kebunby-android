package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPlantActivitiesUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun getPlantActivities(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?
    ) = plantRepository.getPlantActivities(
        username = username,
        isPlanting = isPlanting,
        isPlanted = isPlanted
    )

    operator fun invoke(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?
    ) = getPlantActivities(
        username = username,
        isPlanting = isPlanting,
        isPlanted = isPlanted
    )
}