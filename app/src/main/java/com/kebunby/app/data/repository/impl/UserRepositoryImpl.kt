package com.kebunby.app.data.repository.impl

import android.content.Context
import com.kebunby.app.R
import com.kebunby.app.data.Resource
import com.kebunby.app.data.data_source.remote.UserRemoteDataSource
import com.kebunby.app.data.model.request.LoginRequest
import com.kebunby.app.data.model.request.RegisterRequest
import com.kebunby.app.data.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    @ApplicationContext private val context: Context
) : UserRepository {
    override fun login(loginRequest: LoginRequest) =
        flow {
            val response = userRemoteDataSource.login(loginRequest)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                401 -> emit(Resource.Error(context.resources.getString(R.string.incorrect_username_or_pass)))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override fun register(registerRequest: RegisterRequest) =
        flow {
            val response = userRemoteDataSource.register(registerRequest)

            when (response.code()) {
                201 -> emit(Resource.Success(response.body()?.data))

                409 -> emit(Resource.Error(context.resources.getString(R.string.username_already_exists)))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override fun getUserProfile(username: String) =
        flow {
            val response = userRemoteDataSource.getUserProfile(username)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }
}