package com.kebunby.kebunby.domain.use_case.user_credential

import com.kebunby.kebunby.data.repository.UserCredentialRepository
import javax.inject.Inject

class DeleteUserCredentialUseCase @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
) {
    private suspend fun deleteUserCredential() {
        userCredentialRepository.deleteUserCredential()
    }

    suspend operator fun invoke() {
        deleteUserCredential()
    }
}