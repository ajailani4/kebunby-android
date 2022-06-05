package com.kebunby.app.data.data_source.remote

import com.kebunby.app.data.api.PlantService
import com.kebunby.app.data.model.request.PlantActRequest
import com.kebunby.app.data.model.response.BaseResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class PlantRemoteDataSource @Inject constructor(
    private val plantService: PlantService
) {
    suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = plantService.getPlants(
        page = page,
        size = size,
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )

    suspend fun getPlantsByCategory(
        categoryId: Int,
        page: Int,
        size: Int
    ) = plantService.getPlantsByCategory(
        categoryId = categoryId,
        page = page,
        size = size
    )

    suspend fun getPlantCategories() = plantService.getPlantCategories()

    suspend fun getPlantDetail(id: Int) = plantService.getPlantDetail(id)

    suspend fun getPlantActivities(
        username: String,
        page: Int,
        size: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?
    ) = plantService.getPlantActivities(
        username = username,
        page = page,
        size = size,
        isPlanting = isPlanting,
        isPlanted = isPlanted
    )

    suspend fun uploadPlant(
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
    ): Response<BaseResponse<Any>> {
        val namePart = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            image.name,
            image.asRequestBody("image/*".toMediaTypeOrNull())
        )
        val categoryPart = category.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val wateringFreqPart =
            wateringFreq.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val growthEstPart = growthEst.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val descPart = desc.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val toolsPart =
            tools.joinToString(", ").toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val materialsPart =
            materials.joinToString(", ").toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val stepsPart =
            steps.joinToString(", ").toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val authorPart = author.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        return plantService.uploadPlant(
            name = namePart,
            image = imagePart,
            category = categoryPart,
            wateringFreq = wateringFreqPart,
            growthEst = growthEstPart,
            desc = descPart,
            tools = toolsPart,
            materials = materialsPart,
            steps = stepsPart,
            author = authorPart
        )
    }

    suspend fun editPlant(
        id: Int,
        name: String,
        image: File?,
        category: String,
        wateringFreq: String,
        growthEst: String,
        desc: String,
        tools: List<String>,
        materials: List<String>,
        steps: List<String>,
        author: String,
        popularity: String,
        publishedOn: String
    ): Response<BaseResponse<Any>> {
        val namePart = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imagePart = if (image != null) {
            MultipartBody.Part.createFormData(
                "image",
                image.name,
                image.asRequestBody("image/*".toMediaTypeOrNull())
            )
        } else {
            null
        }
        val categoryPart = category.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val wateringFreqPart =
            wateringFreq.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val growthEstPart = growthEst.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val descPart = desc.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val toolsPart =
            tools.joinToString(", ").toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val materialsPart =
            materials.joinToString(", ").toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val stepsPart =
            steps.joinToString(", ").toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val authorPart = author.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val popularityPart = popularity.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val publishedOnPart =
            publishedOn.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        return plantService.editPlant(
            id = id,
            name = namePart,
            image = imagePart,
            category = categoryPart,
            wateringFreq = wateringFreqPart,
            growthEst = growthEstPart,
            desc = descPart,
            tools = toolsPart,
            materials = materialsPart,
            steps = stepsPart,
            author = authorPart,
            popularity = popularityPart,
            publishedOn = publishedOnPart
        )
    }

    suspend fun addPlantActivity(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        plantActRequest: PlantActRequest
    ) = plantService.addPlantActivity(
        username = username,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited,
        plantActRequest = plantActRequest
    )

    suspend fun deletePlantActivity(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ) = plantService.deletePlantActivity(
        username = username,
        plantId = plantId,
        isPlanting = isPlanting,
        isPlanted = isPlanted,
        isFavorited = isFavorited
    )

    suspend fun deletePlant(id: Int) = plantService.deletePlant(id)
}