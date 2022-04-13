package com.kebunby.kebunby.domain.use_case.user_credential

import com.kebunby.kebunby.data.repository.UserCredentialRepository
import javax.inject.Inject

class GetUserCredentialUseCase @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
) {
    private fun getUserCredential() = userCredentialRepository.getUserCredential()

    operator fun invoke() = getUserCredential()
}