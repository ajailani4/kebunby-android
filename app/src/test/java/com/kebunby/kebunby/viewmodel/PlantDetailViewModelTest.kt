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
    fun getPlantDetail_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generatePlant()))
            }

            doReturn(resource).`when`(getPlantDetailUseCase).invoke(anyInt())

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val plant = when (val plantDetailState = plantDetailViewModel.plantDetailState) {
                is UIState.Success -> plantDetailState.data
                else -> null
            }

            assertNotNull("Plant should not null", plant)

            verify(getPlantDetailUseCase).invoke(anyInt())
        }
    }

    @Test
    fun getPlantDetail_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<Plant>())
            }

            doReturn(resource).`when`(getPlantDetailUseCase).invoke(anyInt())

            plantDetailViewModel = PlantDetailViewModel(
                savedStateHandle,
                getUserCredentialUseCase,
                getPlantDetailUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (plantDetailViewModel.plantDetailState) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getPlantDetailUseCase).invoke(anyInt())
        }
    }

    @Test
    fun addFavoritePlant_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(Any()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(addPlantActivityUseCase).invoke(
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

            val isSuccess = when (plantDetailViewModel.addFavPlantState) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(1)).invoke()
            verify(addPlantActivityUseCase).invoke(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                plantActRequest = any()
            )
        }
    }

    @Test
    fun deleteFavoritePlant_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(Any()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(deletePlantActivityUseCase).invoke(
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

            val isSuccess = when (plantDetailViewModel.deleteFavPlantState) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(1)).invoke()
            verify(deletePlantActivityUseCase).invoke(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )
        }
    }

    @Test
    fun addPlantingPlant_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(Any()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(addPlantActivityUseCase).invoke(
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

            val isSuccess = when (plantDetailViewModel.addPlantingPlantState) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(1)).invoke()
            verify(addPlantActivityUseCase).invoke(
                username = anyString(),
                isPlanting = anyBoolean(),
                isPlanted = isNull(),
                isFavorited = isNull(),
                plantActRequest = any()
            )
        }
    }

    @Test
    fun addPlantedPlant_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(Any()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(addPlantActivityUseCase).invoke(
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

            val isSuccess = when (plantDetailViewModel.addPlantedPlantState) {
                is UIState.Error -> false

                else -> true
            }

            assertEquals("Should be success", true, isSuccess)

            verify(getUserCredentialUseCase, times(1)).invoke()
            verify(addPlantActivityUseCase).invoke(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = anyBoolean(),
                isFavorited = isNull(),
                plantActRequest = any()
            )
        }
    }
}