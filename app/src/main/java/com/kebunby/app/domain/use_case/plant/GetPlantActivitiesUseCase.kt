package com.kebunby.app.domain.use_case.plant

import com.kebunby.app.data.repository.PlantRepository
import javax.inject.Inject

class GetPlantActivitiesUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?
    ) = plantRepository.getPlantActivities(
        username = username,
        isPlanting = isPlanting,
        isPlanted = isPlanted
    )
}