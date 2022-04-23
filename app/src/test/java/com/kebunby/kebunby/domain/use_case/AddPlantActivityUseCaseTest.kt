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
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddPlantActivityUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Dependency
    @Mock
    private lateinit var plantRepository: PlantRepository

    // SUT
    private lateinit var addPlantActivityUseCase: AddPlantActivityUseCase

    @Before
    fun setUp() {
        addPlantActivityUseCase = AddPlantActivityUseCase(plantRepository)
    }

    @Test
    fun addPlantActivity_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success<Any>())
            }

            doReturn(resource).`when`(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                userPlantActRequest = any()
            )

            // Act
            val actualResource = addPlantActivityUseCase.invoke(
                username = "george",
                isPlanting = null,
                isPlanted = null,
                isFavorited = true,
                userPlantActRequest = generatePlantActRequest()
            ).first()

            // Assert
            assertEquals(
                "Resource should be success",
                Resource.Success<Any>(),
                actualResource
            )

            // Verify
            verify(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                userPlantActRequest = any()
            )
        }
    }

    @Test
    fun addPlantActivity_ShouldReturnError() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<Any>())
            }

            doReturn(resource).`when`(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                userPlantActRequest = any()
            )

            // Act
            val actualResource = addPlantActivityUseCase.invoke(
                username = "george",
                isPlanting = null,
                isPlanted = null,
                isFavorited = true,
                userPlantActRequest = generatePlantActRequest()
            ).first()

            // Assert
            assertEquals(
                "Resource should be error",
                Resource.Error<Any>(),
                actualResource
            )

            // Verify
            verify(plantRepository).addPlantActivity(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                userPlantActRequest = any()
            )
        }
    }
}