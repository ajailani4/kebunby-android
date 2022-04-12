package com.kebunby.kebunby.data.repository.impl

import com.kebunby.kebunby.data.data_source.local.UserCredentialLocalDataSource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.repository.UserCredentialRepository
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