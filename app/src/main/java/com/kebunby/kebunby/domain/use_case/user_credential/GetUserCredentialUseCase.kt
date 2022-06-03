package com.kebunby.kebunby.domain.use_case.user_credential

import com.kebunby.kebunby.data.repository.UserCredentialRepository
import javax.inject.Inject

class GetUserCredentialUseCase @Inject constructor(
    private val userCredentialRepository: UserCredentialRepository
) {
    operator fun invoke() = userCredentialRepository.getUserCredential()
}