package com.kebunby.app.data.repository

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.User
import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.data.model.request.LoginRequest
import com.kebunby.app.data.model.request.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(loginRequest: LoginRequest): Flow<Resource<UserCredential>>

    fun register(registerRequest: RegisterRequest): Flow<Resource<UserCredential>>

    fun getUserProfile(username: String): Flow<Resource<User>>
}