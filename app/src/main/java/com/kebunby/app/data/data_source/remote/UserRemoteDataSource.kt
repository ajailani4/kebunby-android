package com.kebunby.app.data.data_source.remote

import com.kebunby.app.data.api.UserService
import com.kebunby.app.data.model.request.LoginRequest
import com.kebunby.app.data.model.request.RegisterRequest
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun login(loginRequest: LoginRequest) = userService.login(loginRequest)

    suspend fun register(registerRequest: RegisterRequest) = userService.register(registerRequest)

    suspend fun getUserProfile(username: String) = userService.getUserProfile(username)
}