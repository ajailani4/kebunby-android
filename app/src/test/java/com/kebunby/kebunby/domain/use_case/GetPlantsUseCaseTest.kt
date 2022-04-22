package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.repository.PlantRepository
import com.kebunby.kebunby.domain.use_case.plant.GetPlantsUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generatePlants
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
class GetPlantsUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Dependency
    @Mock
    private lateinit var plantRepository: PlantRepository

    // SUT
    private lateinit var getPlantsUseCase: GetPlantsUseCase

    @Before
    fun setUp() {
        getPlantsUseCase = GetPlantsUseCase(plantRepository)
    }

    @Test
    fun getPlants_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generatePlants()))
            }

            doReturn(resource).`when`(plantRepository).getPlants(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )

            // Actual
            val actualResource = getPlantsUseCase.invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            ).first()

            // Assert
            assertEquals(
                "Resource should be success",
                Resource.Success(generatePlants()),
                actualResource
            )

            // Verify
            verify(plantRepository).getPlants(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun getPlants_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<List<PlantItem>>())
            }

            doReturn(resource).`when`(plantRepository).getPlants(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )

            // Actual
            val actualResource = getPlantsUseCase.invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            ).first()

            // Assert
            assertEquals(
                "Resource should be error",
                Resource.Error<List<PlantItem>>(),
                actualResource
            )

            // Verify
            verify(plantRepository).getPlants(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )
        }
    }
}