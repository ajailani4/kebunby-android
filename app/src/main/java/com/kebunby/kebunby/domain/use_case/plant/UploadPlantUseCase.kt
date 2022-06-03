package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import java.io.File
import javax.inject.Inject

class UploadPlantUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    operator fun invoke(
        name: String,
        image: File,
        category: String,
        wateringFreq: String,
        growthEst: String,
        desc: String,
        tools: List<String>,
        materials: List<String>,
        steps: List<String>,
        author: String
    ) = plantRepository.uploadPlant(
        name = name,
        image = image,
        category = category,
        wateringFreq = wateringFreq,
        growthEst = growthEst,
        desc = desc,
        tools = tools,
        materials = materials,
        steps = steps,
        author = author
    )
}