package com.kebunby.kebunby.data.repository.impl

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.data_source.remote.PlantRemoteDataSource
import com.kebunby.kebunby.data.data_source.remote.PlantsPagingDataSource
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.data.repository.PlantRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
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

    override fun getPlantsByPaging(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) =
        Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = {
                PlantsPagingDataSource { page, size ->
                    plantRemoteDataSource.getPlants(
                        page = page,
                        size = size,
                        isTrending = isTrending,
                        forBeginner = forBeginner,
                        searchQuery = searchQuery
                    )
                }
            }
        ).flow

    override suspend fun getPlantCategories() =
        flow {
            val response = plantRemoteDataSource.getPlantCategories()

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override suspend fun addPlantActivity(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        plantActRequest: PlantActRequest
    ) =
        flow {
            val response = plantRemoteDataSource.addPlantActivity(
                username = username,
                isPlanting = isPlanting,
                isPlanted = isPlanted,
                isFavorited = isFavorited,
                plantActRequest = plantActRequest
            )

            when (response.code()) {
                201 -> emit(Resource.Success(response.body()?.data))
            }
        }

    override suspend fun deletePlantActivity(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ) =
        flow {
            val response = plantRemoteDataSource.deletePlantActivity(
                username = username,
                plantId = plantId,
                isPlanting = isPlanting,
                isPlanted = isPlanted,
                isFavorited = isFavorited
            )

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))
            }
        }
}