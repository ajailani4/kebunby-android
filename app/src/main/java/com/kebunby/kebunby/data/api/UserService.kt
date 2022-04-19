package com.kebunby.kebunby.data.api

import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.model.request.RegisterRequest
import com.kebunby.kebunby.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<BaseResponse<UserCredential>>

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<BaseResponse<UserCredential>>

    @GET("users/{username}")
    suspend fun getUserProfile(
        @Path("username") username: String
    ): Response<BaseResponse<User>>
}