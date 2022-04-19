package com.kebunby.kebunby.data.repository.impl

import android.content.Context
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.data_source.remote.PlantRemoteDataSource
import com.kebunby.kebunby.data.model.request.UserActPlantRequest
import com.kebunby.kebunby.data.repository.PlantRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantRemoteDataSource: PlantRemoteDataSource,
    @ApplicationContext private val context: Context
) : PlantRepository {
    override suspend fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) =
        flow {
            val response = plantRemoteDataSource.getPlants(
                page = page,
                size = size,
                isTrending = isTrending,
                forBeginner = forBeginner,
                searchQuery = searchQuery
            )

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override suspend fun getPlantCategories() =
        flow {
            val response = plantRemoteDataSource.getPlantCategories()

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override suspend fun addUserPlantAct(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        userActPlantRequest: UserActPlantRequest
    ) =
        flow {
            val response = plantRemoteDataSource.addUserPlantAct(
                username = username,
                isPlanting = isPlanting,
                isPlanted = isPlanted,
                isFavorited = isFavorited,
                userActPlantRequest = userActPlantRequest
            )

            when (response.code()) {
                201 -> emit(Resource.Success(response.body()?.data))

                409 -> emit(Resource.Error(context.getString(R.string.user_act_already_exists)))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }
}