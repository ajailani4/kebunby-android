package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
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