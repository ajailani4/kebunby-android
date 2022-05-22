package com.kebunby.kebunby.viewmodel

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kebunby.kebunby.domain.use_case.plant.GetPlantActivitiesUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.feature.profile.planted.PlantedViewModel
import com.kebunby.kebunby.ui.feature.profile.planting.PlantingViewModel
import com.kebunby.kebunby.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.isNull
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlantedViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Dependency
    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Mock
    private lateinit var getPlantActivitiesUseCase: GetPlantActivitiesUseCase

    // SUT
    private lateinit var plantedViewModel: PlantedViewModel

    @Test
    fun getPlants_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase).invoke()
            doReturn(
                flow { emit(PagingData.from(generatePlants())) }
            ).`when`(getPlantActivitiesUseCase).invoke(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = anyBoolean()
            )

            // Act
            plantedViewModel = PlantedViewModel(
                getUserCredentialUseCase,
                getPlantActivitiesUseCase
            )

            val pagingPlants = plantedViewModel.pagingPlants.value
            val differ = AsyncPagingDataDiffer(
                diffCallback = DiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingPlants)

            // Assert
            assertEquals(generatePlants(), differ.snapshot().items)

            // Verify
            verify(getUserCredentialUseCase).invoke()
            verify(getPlantActivitiesUseCase).invoke(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = anyBoolean()
            )
        }
    }
}