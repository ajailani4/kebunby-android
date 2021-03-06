package com.kebunby.app.domain.use_case

import com.kebunby.app.data.Resource
import com.kebunby.app.data.repository.PlantRepository
import com.kebunby.app.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeletePlantActivityUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var plantRepository: PlantRepository

    private lateinit var deletePlantActivityUseCase: DeletePlantActivityUseCase

    @Before
    fun setUp() {
        deletePlantActivityUseCase = DeletePlantActivityUseCase(plantRepository)
    }

    @Test
    fun `Delete plant activity should return success`() {
        testCoroutineRule.runTest {
            val resource = flow {
                emit(Resource.Success<Any>())
            }

            doReturn(resource).`when`(plantRepository).deletePlantActivity(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )

            val actualResource = deletePlantActivityUseCase(
                username = "george",
                plantId = 1,
                isPlanting = null,
                isPlanted = null,
                isFavorited = true
            ).first()

            assertEquals(
                "Resource should be success",
                Resource.Success<Any>(),
                actualResource
            )

            verify(plantRepository).deletePlantActivity(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )
        }
    }

    @Test
    fun `Delete plant activity should return error`() {
        testCoroutineRule.runTest {
            val resource = flow {
                emit(Resource.Error<Any>())
            }

            doReturn(resource).`when`(plantRepository).deletePlantActivity(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )

            val actualResource = deletePlantActivityUseCase(
                username = "george",
                plantId = 1,
                isPlanting = null,
                isPlanted = null,
                isFavorited = true
            ).first()

            assertEquals(
                "Resource should be error",
                Resource.Error<Any>(),
                actualResource
            )

            verify(plantRepository).deletePlantActivity(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )
        }
    }
}