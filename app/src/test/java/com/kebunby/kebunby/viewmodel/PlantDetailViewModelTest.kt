package com.kebunby.kebunby.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.Plant
import com.kebunby.kebunby.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantDetailUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.feature.plant_detail.PlantDetailEvent
import com.kebunby.kebunby.ui.feature.plant_detail.PlantDetailViewModel
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generatePlant
import com.kebunby.kebunby.util.generateUserCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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
class PlantDetailViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Mock
    private lateinit var getPlantDetailUseCase: GetPlantDetailUseCase

    @Mock
    private lateinit var addPlantActivityUseCase: AddPlantActivityUseCase

    @Mock
    private lateinit var deletePlantActivityUseCase: DeletePlantActivityUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var plantDetailViewModel: PlantDetailViewModel

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle().apply { set("plantId", 1) }
    }

    @Test
    fun `Get plant detail should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generatePlant()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getPlantDetailUseCase)(anyInt())

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val plant = when (val plantDetailState = plantDetailViewModel.plantDetailState.value) {
                is UIState.Success -> plantDetailState.data
                else -> null
            }

            assertNotNull("Plant should not null", plant)

            verify(getUserCredentialUseCase)()
            verify(getPlantDetailUseCase)(anyInt())
        }
    }

    @Test
    fun `Get plant detail should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<Plant>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getPlantDetailUseCase)(anyInt())

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (plantDetailViewModel.plantDetailState.value) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(getPlantDetailUseCase)(anyInt())
        }
    }

    @Test
    fun `Add favorite plant should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Success(Any())) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(addPlantActivityUseCase)(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                plantActRequest = any()
            )

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            plantDetailViewModel.onEvent(PlantDetailEvent.AddFavoritePlant)

            val isSuccess = when (plantDetailViewModel.addFavPlantState.value) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(2))()
            verify(addPlantActivityUseCase)(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                plantActRequest = any()
            )
        }
    }

    @Test
    fun `Delete favorite plant should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Success(Any())) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(deletePlantActivityUseCase)(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            plantDetailViewModel.onEvent(PlantDetailEvent.DeleteFavoritePlant)

            val isSuccess = when (plantDetailViewModel.deleteFavPlantState.value) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(2))()
            verify(deletePlantActivityUseCase)(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )
        }
    }

    @Test
    fun `Add planting plant should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Success(Any())) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(addPlantActivityUseCase)(
                username = anyString(),
                isPlanting = anyBoolean(),
                isPlanted = isNull(),
                isFavorited = isNull(),
                plantActRequest = any()
            )

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            plantDetailViewModel.onEvent(PlantDetailEvent.AddPlantingPlant)

            val isSuccess = when (plantDetailViewModel.addPlantingPlantState.value) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(2))()
            verify(addPlantActivityUseCase)(
                username = anyString(),
                isPlanting = anyBoolean(),
                isPlanted = isNull(),
                isFavorited = isNull(),
                plantActRequest = any()
            )
        }
    }

    @Test
    fun `Add planted plant should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow { emit(Resource.Success(Any())) }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(addPlantActivityUseCase)(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = anyBoolean(),
                isFavorited = isNull(),
                plantActRequest = any()
            )

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            plantDetailViewModel.onEvent(PlantDetailEvent.AddPlantedPlant)

            val isSuccess = when (plantDetailViewModel.addPlantedPlantState.value) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(2))()
            verify(addPlantActivityUseCase)(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = anyBoolean(),
                isFavorited = isNull(),
                plantActRequest = any()
            )
        }
    }
}