package com.kebunby.app.data.api

import com.kebunby.app.data.model.User
import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.data.model.request.LoginRequest
import com.kebunby.app.data.model.request.RegisterRequest
import com.kebunby.app.data.model.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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