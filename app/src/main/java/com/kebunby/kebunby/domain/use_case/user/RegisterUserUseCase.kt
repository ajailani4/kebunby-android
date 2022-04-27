package com.kebunby.kebunby.domain.use_case.user

import com.kebunby.kebunby.data.model.request.RegisterRequest
import com.kebunby.kebunby.data.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    private fun register(registerRequest: RegisterRequest) =
        userRepository.register(registerRequest)

    operator fun invoke(registerRequest: RegisterRequest) =
        register(registerRequest)
}