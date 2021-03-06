package com.kebunby.app.domain.use_case

import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.Plant
import com.kebunby.app.data.repository.PlantRepository
import com.kebunby.app.domain.use_case.plant.GetPlantDetailUseCase
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generatePlant
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetPlantDetailUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var plantRepository: PlantRepository

    private lateinit var getPlantDetailUseCase: GetPlantDetailUseCase

    @Before
    fun setUp() {
        getPlantDetailUseCase = GetPlantDetailUseCase(plantRepository)
    }

    @Test
    fun `Get plant detail should return success`() {
        testCoroutineRule.runTest {
            val resource = flow {
                emit(Resource.Success(generatePlant()))
            }

            doReturn(resource).`when`(plantRepository).getPlantDetail(anyInt())

            val actualResource = getPlantDetailUseCase(anyInt()).first()

            assertEquals(
                "Resource should be success",
                Resource.Success(generatePlant()),
                actualResource
            )

            verify(plantRepository).getPlantDetail(anyInt())
        }
    }

    @Test
    fun `Get plant detail should return fail`() {
        testCoroutineRule.runTest {
            val resource = flow {
                emit(Resource.Error<Plant>())
            }

            doReturn(resource).`when`(plantRepository).getPlantDetail(anyInt())

            val actualResource = getPlantDetailUseCase(anyInt()).first()

            assertEquals(
                "Resource should be error",
                Resource.Error<Plant>(),
                actualResource
            )

            verify(plantRepository).getPlantDetail(anyInt())
        }
    }
}