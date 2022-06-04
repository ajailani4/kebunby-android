package com.kebunby.kebunby.domain.use_case

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.repository.PlantRepository
import com.kebunby.kebunby.domain.use_case.plant.EditPlantUseCase
import com.kebunby.kebunby.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class EditPlantUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Mock
    private lateinit var plantRepository: PlantRepository

    private lateinit var editPlantUseCase: EditPlantUseCase

    @Before
    fun setUp() {
        editPlantUseCase = EditPlantUseCase(plantRepository)
    }

    @Test
    fun `Edit plant should return success`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Success<Any>()) }

            doReturn(resource).`when`(plantRepository).editPlant(
                id = anyInt(),
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString(),
                popularity = anyString(),
                publishedOn = anyString()
            )

            val actualResource = editPlantUseCase(
                id = 1,
                name = "Plant",
                image = tempFile,
                category = "Tanaman Hias",
                wateringFreq = "1x sehari",
                growthEst = "2 bulan",
                desc = "This is a plant",
                tools = listOf("tool 1"),
                materials = listOf("material 1"),
                steps = listOf("step 1"),
                author = "george",
                popularity = "10",
                publishedOn = "2022-06-04"
            ).first()

            assertEquals(
                "Resource should be success",
                Resource.Success<Any>(),
                actualResource
            )

            verify(plantRepository).editPlant(
                id = anyInt(),
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString(),
                popularity = anyString(),
                publishedOn = anyString()
            )
        }
    }

    @Test
    fun `Edit plant should return error`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Error<Any>()) }

            doReturn(resource).`when`(plantRepository).editPlant(
                id = anyInt(),
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString(),
                popularity = anyString(),
                publishedOn = anyString()
            )

            val actualResource = editPlantUseCase(
                id = 1,
                name = "Plant",
                image = tempFile,
                category = "Tanaman Hias",
                wateringFreq = "1x sehari",
                growthEst = "2 bulan",
                desc = "This is a plant",
                tools = listOf("tool 1"),
                materials = listOf("material 1"),
                steps = listOf("step 1"),
                author = "george",
                popularity = "10",
                publishedOn = "2022-06-04"
            ).first()

            assertEquals(
                "Resource should be error",
                Resource.Error<Any>(),
                actualResource
            )

            verify(plantRepository).editPlant(
                id = anyInt(),
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString(),
                popularity = anyString(),
                publishedOn = anyString()
            )
        }
    }
}