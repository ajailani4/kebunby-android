package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.repository.PlantRepository
import com.kebunby.kebunby.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generatePlantActRequest
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddPlantActivityUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var plantRepository: PlantRepository

    private lateinit var addPlantActivityUseCase: AddPlantActivityUseCase

    @Before
    fun setUp() {
        addPlantActivityUseCase = AddPlantActivityUseCase(plantRepository)
    }

    @Test
    fun `Add plant activity should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success<Any>())
            }

            doReturn(resource).`when`(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                plantActRequest = any()
            )

            val actualResource = addPlantActivityUseCase.invoke(
                username = "george",
                isPlanting = null,
                isPlanted = null,
                isFavorited = true,
                plantActRequest = generatePlantActRequest()
            ).first()

            assertEquals(
                "Resource should be success",
                Resource.Success<Any>(),
                actualResource
            )

            verify(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                plantActRequest = any()
            )
        }
    }

    @Test
    fun `Add plant activity should return error`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<Any>())
            }

            doReturn(resource).`when`(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                plantActRequest = any()
            )

            val actualResource = addPlantActivityUseCase.invoke(
                username = "george",
                isPlanting = null,
                isPlanted = null,
                isFavorited = true,
                plantActRequest = generatePlantActRequest()
            ).first()

            assertEquals(
                "Resource should be error",
                Resource.Error<Any>(),
                actualResource
            )

            verify(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                plantActRequest = any()
            )
        }
    }
}