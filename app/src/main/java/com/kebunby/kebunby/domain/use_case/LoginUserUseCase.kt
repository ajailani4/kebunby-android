package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.repository.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    private suspend fun login(loginRequest: LoginRequest) =
        userRepository.login(loginRequest)

    suspend operator fun invoke(loginRequest: LoginRequest) = login(loginRequest)
}