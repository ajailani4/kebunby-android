package com.kebunby.app.domain.use_case.user

import com.kebunby.app.data.model.request.RegisterRequest
import com.kebunby.app.data.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(registerRequest: RegisterRequest) = userRepository.register(registerRequest)
}