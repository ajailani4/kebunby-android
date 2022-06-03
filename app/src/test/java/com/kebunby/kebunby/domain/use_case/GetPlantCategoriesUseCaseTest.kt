package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.repository.PlantRepository
import com.kebunby.kebunby.domain.use_case.plant.GetPlantCategoriesUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generatePlantCategories
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetPlantCategoriesUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var plantRepository: PlantRepository

    private lateinit var getPlantCategoriesUseCase: GetPlantCategoriesUseCase

    @Before
    fun setUp() {
        getPlantCategoriesUseCase = GetPlantCategoriesUseCase(plantRepository)
    }

    @Test
    fun `Get plant categories should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generatePlantCategories()))
            }

            doReturn(resource).`when`(plantRepository).getPlantCategories()

            val actualResource = getPlantCategoriesUseCase().first()

            Assert.assertEquals(
                "Resource should be success",
                Resource.Success(generatePlantCategories()),
                actualResource
            )

            verify(plantRepository).getPlantCategories()
        }
    }

    @Test
    fun `Get plant categories should return error`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<List<PlantCategory>>())
            }

            doReturn(resource).`when`(plantRepository).getPlantCategories()

            val actualResource = getPlantCategoriesUseCase().first()

            Assert.assertEquals(
                "Resource should be error",
                Resource.Error<List<PlantCategory>>(),
                actualResource
            )

            verify(plantRepository).getPlantCategories()
        }
    }
}