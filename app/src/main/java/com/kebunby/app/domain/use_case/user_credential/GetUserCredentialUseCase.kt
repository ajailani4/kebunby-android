package com.kebunby.app.domain.use_case.user_credential

import com.kebunby.app.data.repository.UserCredentialRepository
import javax.inject.Inject

class GetUserCredentialUseCase @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
) {
    operator fun invoke() = userCredentialRepository.getUserCredential()
}