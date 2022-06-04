package com.kebunby.kebunby.viewmodel

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kebunby.kebunby.domain.use_case.plant.GetPlantActivitiesUseCase
import com.kebunby.kebunby.domain.use_case.user_credential.GetUserCredentialUseCase
import com.kebunby.kebunby.ui.feature.profile.planted.PlantedViewModel
import com.kebunby.kebunby.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
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

    @Mock
    private lateinit var getUserCredentialUseCase: GetUserCredentialUseCase

    @Mock
    private lateinit var getPlantActivitiesUseCase: GetPlantActivitiesUseCase

    private lateinit var plantedViewModel: PlantedViewModel

    @Test
    fun `Get plants should return success`() {
        testCoroutineRule.runBlockingTest {
            doReturn(
                flow {
                    emit(generateUserCredential())
                }
            ).`when`(getUserCredentialUseCase)()
            doReturn(
                flow { emit(PagingData.from(generatePlants())) }
            ).`when`(getPlantActivitiesUseCase)(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = anyBoolean()
            )

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

            assertEquals(generatePlants(), differ.snapshot().items)

            verify(getUserCredentialUseCase)()
            verify(getPlantActivitiesUseCase)(
                username = anyString(),
                isPlanting = isNull(),
                isPlanted = anyBoolean()
            )
        }
    }
}