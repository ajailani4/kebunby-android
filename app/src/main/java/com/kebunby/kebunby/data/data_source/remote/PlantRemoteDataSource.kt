package com.kebunby.kebunby.data.data_source.remote

import com.kebunby.kebunby.data.api.PlantService
import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.data.model.response.BaseResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        val namePart = RequestBody.create(MediaType.parse("multipart/form-data"), name)
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            image.name,
            RequestBody.create(MediaType.parse("image/*"), image)
        )
        val categoryPart = RequestBody.create(MediaType.parse("multipart/form-data"), category)
        val wateringFreqPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), wateringFreq)
        val growthEstPart = RequestBody.create(MediaType.parse("multipart/form-data"), growthEst)
        val descPart = RequestBody.create(MediaType.parse("multipart/form-data"), desc)
        val toolsPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), tools.joinToString(", "))
        val materialsPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), materials.joinToString(", "))
        val stepsPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), steps.joinToString(", "))
        val authorPart = RequestBody.create(MediaType.parse("multipart/form-data"), author)

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
        val namePart = RequestBody.create(MediaType.parse("multipart/form-data"), name)
        val imagePart = if (image != null) {
            MultipartBody.Part.createFormData(
                "image",
                image.name,
                RequestBody.create(MediaType.parse("image/*"), image)
            )
        } else {
            null
        }
        val categoryPart = RequestBody.create(MediaType.parse("multipart/form-data"), category)
        val wateringFreqPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), wateringFreq)
        val growthEstPart = RequestBody.create(MediaType.parse("multipart/form-data"), growthEst)
        val descPart = RequestBody.create(MediaType.parse("multipart/form-data"), desc)
        val toolsPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), tools.joinToString(", "))
        val materialsPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), materials.joinToString(", "))
        val stepsPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), steps.joinToString(", "))
        val authorPart = RequestBody.create(MediaType.parse("multipart/form-data"), author)
        val popularityPart = RequestBody.create(MediaType.parse("multipart/form-data"), popularity)
        val publishedOnPart =
            RequestBody.create(MediaType.parse("multipart/form-data"), publishedOn)

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