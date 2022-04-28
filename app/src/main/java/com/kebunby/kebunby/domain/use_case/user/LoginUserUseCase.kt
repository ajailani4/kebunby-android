package com.kebunby.kebunby.domain.use_case.user

import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.repository.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    private fun login(loginRequest: LoginRequest) =
        userRepository.login(loginRequest)

    operator fun invoke(loginRequest: LoginRequest) =
        login(loginRequest)
}