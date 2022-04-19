package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.model.request.UserActPlantRequest
import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class AddUserPlantActUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private suspend fun addUserPlantAct(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        userActPlantRequest: UserActPlantRequest
    ) =
        plantRepository.addUserPlantAct(
            username = username,
            isPlanting = isPlanting,
            isPlanted = isPlanted,
            isFavorited = isFavorited,
            userActPlantRequest = userActPlantRequest
        )

    suspend operator fun invoke(
        username: String,
        isPlanting: Boolean? = null,
        isPlanted: Boolean? = null,
        isFavorited: Boolean? = null,
        userActPlantRequest: UserActPlantRequest
    ) =
        addUserPlantAct(
            username = username,
            isPlanting = isPlanting,
            isPlanted = isPlanted,
            isFavorited = isFavorited,
            userActPlantRequest = userActPlantRequest
        )
}