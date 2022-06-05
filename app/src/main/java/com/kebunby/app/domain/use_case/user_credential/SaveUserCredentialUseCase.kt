package com.kebunby.app.domain.use_case.user_credential

import com.kebunby.app.data.model.UserCredential
import com.kebunby.app.data.repository.UserCredentialRepository
import javax.inject.Inject

class SaveUserCredentialUseCase @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
) {
    suspend operator fun invoke(userCredential: UserCredential) {
        userCredentialRepository.saveUserCredential(userCredential)
    }
}