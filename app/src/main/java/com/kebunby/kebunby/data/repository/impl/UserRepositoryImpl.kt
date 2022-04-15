package com.kebunby.kebunby.data.repository.impl

import android.content.Context
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.data_source.remote.UserRemoteDataSource
import com.kebunby.kebunby.data.model.UserCredential
import com.kebunby.kebunby.data.model.request.LoginRequest
import com.kebunby.kebunby.data.model.request.RegisterRequest
import com.kebunby.kebunby.data.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    @ApplicationContext private val context: Context
) : UserRepository {
    override suspend fun login(loginRequest: LoginRequest) =
        flow {
            val response = userRemoteDataSource.login(loginRequest)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                401 -> emit(Resource.Error(context.resources.getString(R.string.incorrect_username_or_pass)))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override suspend fun register(registerRequest: RegisterRequest) =
        flow {
            val response = userRemoteDataSource.register(registerRequest)

            when (response.code()) {
                201 -> emit(Resource.Success(response.body()?.data))

                409 -> emit(Resource.Error(context.resources.getString(R.string.username_already_exists)))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }
}