package com.kebunby.kebunby.data.data_source.remote

import com.kebunby.kebunby.data.api.ApiService
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.model.request.RegisterRequest
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)

    suspend fun register(registerRequest: RegisterRequest) = apiService.register(registerRequest)
}