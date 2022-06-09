package com.kebunby.app.domain.use_case.user

import com.kebunby.app.data.model.request.LoginRequest
import com.kebunby.app.data.repository.UserRepository
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(loginRequest: LoginRequest) = userRepository.login(loginRequest)
}