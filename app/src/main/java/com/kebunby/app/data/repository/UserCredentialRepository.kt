package com.kebunby.app.data.repository

import com.kebunby.app.data.model.UserCredential
import kotlinx.coroutines.flow.Flow

interface UserCredentialRepository {
    suspend fun saveUserCredential(userCredential: UserCredential)

    fun getUserCredential(): Flow<UserCredential>

    suspend fun deleteUserCredential()
}