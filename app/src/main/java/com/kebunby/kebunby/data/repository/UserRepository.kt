package com.kebunby.kebunby.data.repository

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.model.request.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(loginRequest: LoginRequest): Flow<Resource<UserCredential>>

    fun register(registerRequest: RegisterRequest): Flow<Resource<UserCredential>>

    fun getUserProfile(username: String): Flow<Resource<User>>
}