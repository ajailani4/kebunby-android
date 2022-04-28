package com.kebunby.kebunby.domain.use_case.user

import com.kebunby.kebunby.data.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    private fun getUserProfile(username: String) =
        userRepository.getUserProfile(username)

    operator fun invoke(username: String) =
        getUserProfile(username)
}