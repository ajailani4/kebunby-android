package com.kebunby.kebunby.viewmodel

import com.kebunby.kebunby.data.Resource
import com.kebunby.kebunby.data.model.PlantCategory
import com.kebunby.kebunby.data.model.PlantItem
import com.kebunby.kebunby.data.model.User
import com.kebunby.kebunby.domain.use_case.plant.AddPlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.DeletePlantActivityUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantCategoriesUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPlantsUseCase
import com.kebunby.kebunby.domain.use_case.user.GetUserProfileUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.common.UIState
import com.kebunby.kebunby.ui.feature.home.HomeEvent
import com.kebunby.kebunby.ui.feature.home.HomeViewModel
import com.kebunby.kebunby.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
class HomeViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Mock
    private lateinit var getPlantsUseCase: GetPlantsUseCase

    @Mock
    private lateinit var getPlantCategoriesUseCase: GetPlantCategoriesUseCase

    @Mock
    private lateinit var addPlantActivityUseCase: AddPlantActivityUseCase

    @Mock
    private lateinit var deletePlantActivityUseCase: DeletePlantActivityUseCase

    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `Get profile should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generateUser()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getUserProfileUseCase)(anyString())

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val userProfile = when (val userProfileState = homeViewModel.userProfileState.value) {
                is UIState.Success -> userProfileState.data
                else -> null
            }

            assertNotNull(userProfile)
            assertEquals("Username should be 'george'", "george", userProfile?.username)
            assertEquals("Email should be 'george@email.com'", "george@email.com", userProfile?.email)

            verify(getUserCredentialUseCase)()
            verify(getUserProfileUseCase)(anyString())
        }
    }

    @Test
    fun `Get profile should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<User>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getUserProfileUseCase)(anyString())

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.userProfileState.value) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(getUserProfileUseCase)(anyString())
        }
    }

    @Test
    fun `Get trending plants should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generatePlants()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val trendingPlants =
                when (val trendingPlantsState = homeViewModel.trendingPlantsState.value) {
                    is UIState.Success -> trendingPlantsState.data

                    else -> listOf()
                }

            assertEquals("Plants size should be 5", 5, trendingPlants?.size)

            verify(getUserCredentialUseCase)()
            verify(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun `Get trending plants should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<List<PlantItem>>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.trendingPlantsState.value) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun `Get for beginner plants should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generatePlants()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val forBeginnerPlants =
                when (val forBeginnerPlantsState = homeViewModel.forBeginnerPlantsState.value) {
                    is UIState.Success -> forBeginnerPlantsState.data

                    else -> listOf()
                }

            assertEquals("Plants size should be 5", 5, forBeginnerPlants?.size)

            verify(getUserCredentialUseCase)()
            verify(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun `Get for beginner plants should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<List<PlantItem>>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(resource).`when`(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.forBeginnerPlantsState.value) {
                is UIState.Success -> true

                is UIState.Fail -> false

                is UIState.Error -> false

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getUserCredentialUseCase)()
            verify(getPlantsUseCase)(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun `Get plant categories should return success`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Success(generatePlantCategories()))
            }

            doReturn(resource).`when`(getPlantCategoriesUseCase)()

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val plantCategoriesPlants =
                when (val plantCategoriesState = homeViewModel.plantCategoriesState.value) {
                    is UIState.Success -> plantCategoriesState.data

                    else -> listOf()
                }

            assertEquals("Plant categories size should be 5", 5, plantCategoriesPlants?.size)

            verify(getPlantCategoriesUseCase)()
        }
    }

    @Test
    fun `Get plant categories should return fail`() {
        testCoroutineRule.runBlockingTest {
            val resource = flow {
                emit(Resource.Error<List<PlantCategory>>())
            }

            doReturn(resource).`when`(getPlantCategoriesUseCase)()

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.plantCategoriesState.value) {
                is UIState.Success -> true

                else -> false
            }

            assertEquals("Should be fail", false, isSuccess)

            verify(getPlantCategoriesUseCase)()
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

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            homeViewModel.onEvent(HomeEvent.AddFavoritePlant)

            val isSuccess = when (homeViewModel.addFavPlantState.value) {
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
            val resource = flow {
                emit(Resource.Success(Any()))
            }

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

            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            homeViewModel.onEvent(HomeEvent.DeleteFavoritePlant)

            val isSuccess = when (homeViewModel.deleteFavPlantState.value) {
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
}