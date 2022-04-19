package com.kebunby.kebunby.data.api

import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlantService {
    @GET("plants")
    suspend fun getPlants(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("isTrending") isTrending: Boolean?,
        @Query("forBeginner") forBeginner: Boolean?,
        @Query("searchQuery") searchQuery: String?
    ): Response<BaseResponse<List<PlantItem>>>
}