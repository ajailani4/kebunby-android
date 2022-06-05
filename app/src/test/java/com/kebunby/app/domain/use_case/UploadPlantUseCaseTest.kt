package com.kebunby.app.domain.use_case

import com.kebunby.app.data.Resource
import com.kebunby.app.data.repository.PlantRepository
import com.kebunby.app.domain.use_case.plant.UploadPlantUseCase
import com.kebunby.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UploadPlantUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Mock
    private lateinit var plantRepository: PlantRepository

    private lateinit var uploadPlantUseCase: UploadPlantUseCase

    @Before
    fun setUp() {
        uploadPlantUseCase = UploadPlantUseCase(plantRepository)
    }

    @Test
    fun `Upload plant should return success`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Success<Any>()) }

            doReturn(resource).`when`(plantRepository).uploadPlant(
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString()
            )

            val actualResource = uploadPlantUseCase(
                name = "Plant",
                image = tempFile,
                category = "Tanaman Hias",
                wateringFreq = "1x sehari",
                growthEst = "2 bulan",
                desc = "This is a plant",
                tools = listOf("tool 1"),
                materials = listOf("material 1"),
                steps = listOf("step 1"),
                author = "george"
            ).first()

            assertEquals(
                "Resource should be success",
                Resource.Success<Any>(),
                actualResource
            )

            verify(plantRepository).uploadPlant(
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString()
            )
        }
    }

    @Test
    fun `Upload plant should return error`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Error<Any>()) }

            doReturn(resource).`when`(plantRepository).uploadPlant(
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString()
            )

            val actualResource = uploadPlantUseCase(
                name = "Plant",
                image = tempFile,
                category = "Tanaman Hias",
                wateringFreq = "1x sehari",
                growthEst = "2 bulan",
                desc = "This is a plant",
                tools = listOf("tool 1"),
                materials = listOf("material 1"),
                steps = listOf("step 1"),
                author = "george"
            ).first()

            assertEquals(
                "Resource should be error",
                Resource.Error<Any>(),
                actualResource
            )

            verify(plantRepository).uploadPlant(
                name = anyString(),
                image = any(),
                category = anyString(),
                wateringFreq = anyString(),
                growthEst = anyString(),
                desc = anyString(),
                tools = anyList(),
                materials = anyList(),
                steps = anyList(),
                author = anyString()
            )
        }
    }
}