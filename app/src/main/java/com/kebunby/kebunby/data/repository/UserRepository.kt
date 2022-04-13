package com.kebunby.kebunby.data.repository

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.model.request.LoginRequest
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest): Flow<Resource<UserCredential>>
}