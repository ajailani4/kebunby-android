package com.kebunby.kebunby.data.api

import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface PlantCategoryService {
    @GET("categories")
    suspend fun getPlantCategories(): Response<BaseResponse<List<PlantCategory>>>
}