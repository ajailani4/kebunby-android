package com.kebunby.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.kebunby.app.data.Resource
import com.kebunby.app.data.model.PlantCategory
import com.kebunby.app.domain.use_case.plant.EditPlantUseCase
import com.kebunby.app.domain.use_case.plant.GetPlantCategoriesUseCase
import com.kebunby.app.domain.use_case.plant.GetPlantDetailUseCase
import com.kebunby.app.domain.use_case.plant.UploadPlantUseCase
import com.kebunby.app.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.app.ui.common.UIState
import com.kebunby.app.ui.feature.upload_edit_plant.UploadEditPlantEvent
import com.kebunby.app.ui.feature.upload_edit_plant.UploadEditPlantViewModel
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generatePlantCategories
import com.kebunby.app.util.generatePlantCategory
import com.kebunby.app.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UploadEditPlantViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Mock
    private lateinit var getPlantDetailUseCase: GetPlantDetailUseCase

    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Mock
    private lateinit var getPlantCategoriesUseCase: GetPlantCategoriesUseCase

    @Mock
    private lateinit var uploadPlantUseCase: UploadPlantUseCase

    @Mock
    private lateinit var editPlantUseCase: EditPlantUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var uploadEditPlantViewModel: UploadEditPlantViewModel

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle().apply { set("plantId", 0) }

        uploadEditPlantViewModel = UploadEditPlantViewModel(
            savedStateHandle,
            getPlantDetailUseCase,
            getUserCredentialUseCase,
            getPlantCategoriesUseCase,
            uploadPlantUseCase,
            editPlantUseCase
        )
    }

    @Test
    fun `Get plant categories should return success`() {
        testCoroutineRule.runTest {
            savedStateHandle.set("plantId", 0)
            val resource = flow {
                emit(Resource.Success(generatePlantCategories()))
            }

            doReturn(resource).`when`(getPlantCategoriesUseCase)()

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.LoadPlantCategories)

            val plantCategoriesPlants =
                when (val plantCategoriesState =
                    uploadEditPlantViewModel.plantCategoriesState.value) {
                    is UIState.Success -> plantCategoriesState.data

                    else -> listOf()
                }

            assertEquals("Plant categories size should be 5", 5, plantCategoriesPlants?.size)

            verify(getPlantCategoriesUseCase, times(2))()
        }
    }

    @Test
    fun `Get plant categories should return fail`() {
        testCoroutineRule.runTest {
            savedStateHandle.set("plantId", 0)
            val resource = flow {
                emit(Resource.Error<List<PlantCategory>>())
            }

            doReturn(resource).`when`(getPlantCategoriesUseCase)()

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.LoadPlantCategories)

            val isSuccess =
                when (val plantCategoriesState =
                    uploadEditPlantViewModel.plantCategoriesState.value) {
                    is UIState.Success -> true

                    else -> false
                }

            assertEquals("Should be fail", false, isSuccess)

            verify(getPlantCategoriesUseCase, times(2))()
        }
    }

    @Test
    fun `Upload plant should return success`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runTest {
            uploadEditPlantViewModel.apply {
                onPhotoChanged(tempFile)
                onSelectedCategoryChanged(generatePlantCategory())
            }

            val resource = flow { emit(Resource.Success(Any())) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(uploadPlantUseCase)(
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

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.UploadPlant)

            val isSuccess = when (uploadEditPlantViewModel.uploadPlantState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(uploadPlantUseCase)(
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
    fun `Upload plant should return fail`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runTest {
            uploadEditPlantViewModel.apply {
                onPhotoChanged(tempFile)
                onSelectedCategoryChanged(generatePlantCategory())
            }

            val resource = flow { emit(Resource.Error<Any>()) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(uploadPlantUseCase)(
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

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.UploadPlant)

            val isSuccess = when (uploadEditPlantViewModel.uploadPlantState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(uploadPlantUseCase)(
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
    fun `Edit plant without changing plant photo should return success`() {
        testCoroutineRule.runTest {
            uploadEditPlantViewModel.onSelectedCategoryChanged(generatePlantCategory())

            val resource = flow { emit(Resource.Success(Any())) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(editPlantUseCase)(
                id = anyInt(),
                name = anyString(),
                image = isNull(),
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

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.EditPlant)

            val isSuccess = when (uploadEditPlantViewModel.editPlantState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(editPlantUseCase)(
                id = anyInt(),
                name = anyString(),
                image = isNull(),
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
    fun `Edit plant without changing plant photo should return fail`() {
        testCoroutineRule.runTest {
            uploadEditPlantViewModel.onSelectedCategoryChanged(generatePlantCategory())

            val resource = flow { emit(Resource.Error<Any>()) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(editPlantUseCase)(
                id = anyInt(),
                name = anyString(),
                image = isNull(),
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

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.EditPlant)

            val isSuccess = when (uploadEditPlantViewModel.editPlantState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(editPlantUseCase)(
                id = anyInt(),
                name = anyString(),
                image = isNull(),
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
    fun `Edit plant with changing plant photo should return success`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runTest {
            uploadEditPlantViewModel.apply {
                onPhotoChanged(tempFile)
                onSelectedCategoryChanged(generatePlantCategory())
            }

            val resource = flow { emit(Resource.Success(Any())) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(editPlantUseCase)(
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

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.EditPlant)

            val isSuccess = when (uploadEditPlantViewModel.editPlantState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(editPlantUseCase)(
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
    fun `Edit plant with changing plant photo should return fail`() {
        val tempFile = temporaryFolder.newFile("plant.jpg")

        testCoroutineRule.runTest {
            uploadEditPlantViewModel.apply {
                onPhotoChanged(tempFile)
                onSelectedCategoryChanged(generatePlantCategory())
            }

            val resource = flow { emit(Resource.Error<Any>()) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(editPlantUseCase)(
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

            uploadEditPlantViewModel.onEvent(UploadEditPlantEvent.EditPlant)

            val isSuccess = when (uploadEditPlantViewModel.editPlantState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(editPlantUseCase)(
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