package com.kebunby.app.domain.use_case.user

import com.kebunby.app.data.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(username: String) = userRepository.getUserProfile(username)
}