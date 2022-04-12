package com.kebunby.kebunby.data.api

import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.model.response.BaseResponse
import com.kebunby.kebunby.data.model.response.CredentialResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<BaseResponse<CredentialResponse>>
}