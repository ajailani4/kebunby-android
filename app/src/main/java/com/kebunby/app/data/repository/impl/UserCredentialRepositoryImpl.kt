package com.kebunby.app.data.repository.impl

import com.kebunby.app.data.data_source.local.UserCredentialLocalDataSource
import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.data.repository.UserCredentialRepository
import javax.inject.Inject

class UserCredentialRepositoryImpl @Inject constructor(
    private val userCredentialLocalDataSource: UserCredentialLocalDataSource
) : UserCredentialRepository {
    override suspend fun saveUserCredential(userCredential: UserCredential) {
        userCredentialLocalDataSource.saveUserCredential(userCredential)
    }

    override fun getUserCredential() = userCredentialLocalDataSource.getUserCredential()

    override suspend fun deleteUserCredential() {
        userCredentialLocalDataSource.deleteUserCredential()
    }
}