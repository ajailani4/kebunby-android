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
import com.kebunby.kebunby.ui.feature.home.HomeEvent
import com.kebunby.kebunby.ui.feature.home.HomeState
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

    // Dependency
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

    // SUT
    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun getProfile_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generateUser()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getUserProfileUseCase).invoke(anyString())

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val userProfile: User? = when (val userProfileState = homeViewModel.userProfileState) {
                is HomeState.UserProfile -> userProfileState.user
                else -> null
            }

            // Assert
            assertNotNull("Username should be 'george'", userProfile?.username)
            assertNotNull("Email should be 'george@email.com'", userProfile?.email)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getUserProfileUseCase).invoke(anyString())
        }
    }

    @Test
    fun getProfile_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<User>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getUserProfileUseCase).invoke(anyString())

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.userProfileState) {
                is HomeState.UserProfile -> true

                is HomeState.FailUserProfile -> false

                is HomeState.ErrorUserProfile -> false

                else -> false
            }

            // Assert
            assertEquals("Should be fail", false, isSuccess)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getUserProfileUseCase).invoke(anyString())
        }
    }

    @Test
    fun getTrendingPlants_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generatePlants()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val trendingPlants =
                when (val trendingPlantsState = homeViewModel.trendingPlantsState) {
                    is HomeState.TrendingPlants -> trendingPlantsState.plants

                    else -> listOf()
                }

            // Assert
            assertEquals("Plants size should be 5", 5, trendingPlants?.size)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun getTrendingPlants_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<List<PlantItem>>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.trendingPlantsState) {
                is HomeState.TrendingPlants -> true

                is HomeState.FailTrendingPlants -> false

                is HomeState.ErrorTrendingPlants -> false

                else -> false
            }

            // Assert
            assertEquals("Should be fail", false, isSuccess)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = anyBoolean(),
                forBeginner = isNull(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun getForBeginnerPlants_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generatePlants()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val forBeginnerPlants =
                when (val forBeginnerPlantsState = homeViewModel.forBeginnerPlantsState) {
                    is HomeState.ForBeginnerPlants -> forBeginnerPlantsState.plants

                    else -> listOf()
                }

            // Assert
            assertEquals("Plants size should be 5", 5, forBeginnerPlants?.size)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun getForBeginnerPlants_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<List<PlantItem>>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.forBeginnerPlantsState) {
                is HomeState.ForBeginnerPlants -> true

                is HomeState.FailForBeginnerPlants -> false

                is HomeState.ErrorForBeginnerPlants -> false

                else -> false
            }

            // Assert
            assertEquals("Should be fail", false, isSuccess)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getPlantsUseCase).invoke(
                page = anyInt(),
                size = anyInt(),
                isTrending = isNull(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun getPlantCategories_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Success(generatePlantCategories()))
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getPlantCategoriesUseCase).invoke()

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val plantCategoriesPlants =
                when (val plantCategoriesState = homeViewModel.plantCategoriesState) {
                    is HomeState.PlantCategories -> plantCategoriesState.plantCategories

                    else -> listOf()
                }

            // Assert
            assertEquals("Plant categories size should be 5", 5, plantCategoriesPlants?.size)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getPlantCategoriesUseCase).invoke()
        }
    }

    @Test
    fun getPlantCategories_ShouldReturnFail() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            val resource = flow {
                emit(Resource.Error<List<PlantCategory>>())
            }

            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(resource).`when`(getPlantCategoriesUseCase).invoke()

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            val isSuccess = when (homeViewModel.plantCategoriesState) {
                is HomeState.PlantCategories -> true

                is HomeState.FailPlantCategories -> false

                is HomeState.ErrorPlantCategories -> false

                else -> false
            }

            // Assert
            assertEquals("Should be fail", false, isSuccess)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getPlantCategoriesUseCase).invoke()
        }
    }

    @Test
    fun addUserFavPlant_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
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
                userPlantActRequest = any()
            )

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            homeViewModel.onEvent(HomeEvent.AddFavoritePlant)

            val isSuccess = when (homeViewModel.addFavPlantState) {
                is HomeState.ErrorAddFavoritePlant -> false

                else -> true
            }

            // Assert
            assertEquals("Should be success", true, isSuccess)

            // Verify
            verify(getUserCredentialUseCase, times(2)).invoke()
            verify(addPlantActivityUseCase).invoke(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean(),
                userPlantActRequest = any()
            )
        }
    }

    @Test
    fun deleteUserFavPlant_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
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

            // Act
            homeViewModel = HomeViewModel(
                getUserProfileUseCase,
                getUserCredentialUseCase,
                getPlantsUseCase,
                getPlantCategoriesUseCase,
                addPlantActivityUseCase,
                deletePlantActivityUseCase
            )

            homeViewModel.onEvent(HomeEvent.DeleteFavoritePlant)

            val isSuccess = when (homeViewModel.deleteFavPlantState) {
                is HomeState.ErrorDeleteFavoritePlant -> false

                else -> true
            }

            // Assert
            assertEquals("Should be success", true, isSuccess)

            // Verify
            verify(getUserCredentialUseCase, times(2)).invoke()
            verify(deletePlantActivityUseCase).invoke(
                username = anyString(),
                plantId = anyInt(),
                isPlanting = isNull(),
                isPlanted = isNull(),
                isFavorited = anyBoolean()
            )
        }
    }
}