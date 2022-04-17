package com.kebunby.kebunby.domain.use_case.user

import com.kebunby.kebunby.data.data_source.remote.UserRemoteDataSource
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) {
    private suspend fun getUserProfile(username: String) =
        userRemoteDataSource.getUserProfile(username)

    suspend operator fun invoke(username: String) =
        getUserProfile(username)
}