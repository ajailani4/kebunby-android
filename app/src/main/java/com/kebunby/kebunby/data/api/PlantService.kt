package com.kebunby.kebunby.data.api

import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.*

interface PlantService {
    @GET("plants")
    suspend fun getPlants(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("isTrending") isTrending: Boolean?,
        @Query("forBeginner") forBeginner: Boolean?,
        @Query("searchQuery") searchQuery: String?
    ): Response<BaseResponse<List<PlantItem>>>

    @GET("categories/{categoryId}/plants")
    suspend fun getPlantsByCategory(
        @Path("categoryId") categoryId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<BaseResponse<List<PlantItem>>>

    @GET("categories")
    suspend fun getPlantCategories(): Response<BaseResponse<List<PlantCategory>>>

    @POST("users/{username}/plants")
    suspend fun addPlantActivity(
        @Path("username") username: String,
        @Query("isPlanting") isPlanting: Boolean?,
        @Query("isPlanted") isPlanted: Boolean?,
        @Query("isFavorited") isFavorited: Boolean?,
        @Body plantActRequest: PlantActRequest
    ): Response<BaseResponse<Any>>

    @DELETE("users/{username}/plants/{plantId}")
    suspend fun deletePlantActivity(
        @Path("username") username: String,
        @Path("plantId") plantId: Int,
        @Query("isPlanting") isPlanting: Boolean?,
        @Query("isPlanted") isPlanted: Boolean?,
        @Query("isFavorited") isFavorited: Boolean?
    ): Response<BaseResponse<Any>>
}