package com.kebunby.app.data.api

import com.kebunby.app.data.model.Plant
import com.kebunby.app.data.model.PlantCategory
import com.kebunby.app.data.model.PlantItem
import com.kebunby.app.data.model.request.PlantActRequest
import com.kebunby.app.data.model.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @GET("plants/{id}")
    suspend fun getPlantDetail(
        @Path("id") id: Int
    ): Response<BaseResponse<Plant>>

    @GET("users/{username}/plants")
    suspend fun getPlantActivities(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("isPlanting") isPlanting: Boolean?,
        @Query("isPlanted") isPlanted: Boolean?
    ): Response<BaseResponse<List<PlantItem>>>

    @Multipart
    @POST("plants")
    suspend fun uploadPlant(
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("category") category: RequestBody,
        @Part("wateringFreq") wateringFreq: RequestBody,
        @Part("growthEst") growthEst: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part tools: List<MultipartBody.Part>,
        @Part materials: List<MultipartBody.Part>,
        @Part steps: List<MultipartBody.Part>,
        @Part("author") author: RequestBody
    ): Response<BaseResponse<Any>>

    @POST("users/{username}/plants")
    suspend fun addPlantActivity(
        @Path("username") username: String,
        @Query("isPlanting") isPlanting: Boolean?,
        @Query("isPlanted") isPlanted: Boolean?,
        @Query("isFavorited") isFavorited: Boolean?,
        @Body plantActRequest: PlantActRequest
    ): Response<BaseResponse<Any>>

    @Multipart
    @PUT("plants/{id}")
    suspend fun editPlant(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("category") category: RequestBody,
        @Part("wateringFreq") wateringFreq: RequestBody,
        @Part("growthEst") growthEst: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part tools: List<MultipartBody.Part>,
        @Part materials: List<MultipartBody.Part>,
        @Part steps: List<MultipartBody.Part>,
        @Part("author") author: RequestBody,
        @Part("popularity") popularity: RequestBody,
        @Part("publishedOn") publishedOn: RequestBody
    ): Response<BaseResponse<Any>>

    @DELETE("users/{username}/plants/{plantId}")
    suspend fun deletePlantActivity(
        @Path("username") username: String,
        @Path("plantId") plantId: Int,
        @Query("isPlanting") isPlanting: Boolean?,
        @Query("isPlanted") isPlanted: Boolean?,
        @Query("isFavorited") isFavorited: Boolean?
    ): Response<BaseResponse<Any>>

    @DELETE("plants/{id}")
    suspend fun deletePlant(
        @Path("id") id: Int
    ): Response<BaseResponse<Any>>
}