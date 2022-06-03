package com.kebunby.kebunby.data.repository.impl

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kebunby.kebunby.R
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.data_source.remote.PagingDataSource
import com.kebunby.kebunby.data.data_source.remote.PlantRemoteDataSource
import com.kebunby.kebunby.data.model.request.PlantActRequest
import com.kebunby.kebunby.data.repository.PlantRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class PlantRepositoryImpl @Inject constructor(
    private val plantRemoteDataSource: PlantRemoteDataSource,
    @ApplicationContext private val context: Context
) : PlantRepository {
    override fun getPlants(
        page: Int,
        size: Int,
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = flow {
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

    override fun getPagingPlants(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 10),
        pagingSourceFactory = {
            PagingDataSource { page, size ->
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

    override fun getPagingPlantsByCategory(categoryId: Int) =
        Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 10),
            pagingSourceFactory = {
                PagingDataSource { page, size ->
                    plantRemoteDataSource.getPlantsByCategory(
                        categoryId = categoryId,
                        page = page,
                        size = size
                    )
                }
            }
        ).flow

    override fun getPlantCategories() =
        flow {
            val response = plantRemoteDataSource.getPlantCategories()

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override fun getPlantDetail(id: Int) =
        flow {
            val response = plantRemoteDataSource.getPlantDetail(id)

            when (response.code()) {
                200 -> emit(Resource.Success(response.body()?.data))

                else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
            }
        }

    override fun getPlantActivities(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?
    ) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 10),
        pagingSourceFactory = {
            PagingDataSource { page, size ->
                plantRemoteDataSource.getPlantActivities(
                    username = username,
                    page = page,
                    size = size,
                    isPlanting = isPlanting,
                    isPlanted = isPlanted
                )
            }
        }
    ).flow

    override fun uploadPlant(
        name: String,
        image: File,
        category: String,
        wateringFreq: String,
        growthEst: String,
        desc: String,
        tools: List<String>,
        materials: List<String>,
        steps: List<String>,
        author: String
    ) = flow {
        val response = plantRemoteDataSource.uploadPlant(
            name = name,
            image = image,
            category = category,
            wateringFreq = wateringFreq,
            growthEst = growthEst,
            desc = desc,
            tools = tools,
            materials = materials,
            steps = steps,
            author = author
        )

        when (response.code()) {
            201 -> emit(Resource.Success(response.body()?.data))

            413 -> emit(Resource.Error(context.resources.getString(R.string.photo_size_is_too_large)))

            else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
        }
    }

    override fun editPlant(
        id: Int,
        name: String,
        image: File?,
        category: String,
        wateringFreq: String,
        growthEst: String,
        desc: String,
        tools: List<String>,
        materials: List<String>,
        steps: List<String>,
        author: String,
        popularity: String,
        publishedOn: String
    ) = flow {
        val response = plantRemoteDataSource.editPlant(
            id = id,
            name = name,
            image = image,
            category = category,
            wateringFreq = wateringFreq,
            growthEst = growthEst,
            desc = desc,
            tools = tools,
            materials = materials,
            steps = steps,
            author = author,
            popularity = popularity,
            publishedOn = publishedOn
        )

        when (response.code()) {
            200 -> emit(Resource.Success(response.body()?.data))

            413 -> emit(Resource.Error(context.resources.getString(R.string.photo_size_is_too_large)))

            else -> emit(Resource.Error(context.resources.getString(R.string.something_wrong_happened)))
        }
    }

    override fun addPlantActivity(
        username: String,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?,
        plantActRequest: PlantActRequest
    ) = flow {
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

    override fun deletePlantActivity(
        username: String,
        plantId: Int,
        isPlanting: Boolean?,
        isPlanted: Boolean?,
        isFavorited: Boolean?
    ) = flow {
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