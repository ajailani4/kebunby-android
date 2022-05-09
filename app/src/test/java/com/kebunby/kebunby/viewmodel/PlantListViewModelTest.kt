package com.kebunby.kebunby.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kebunby.kebunby.data.data_source.remote.PlantRemoteDataSource
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsByCategoryUseCase
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsUseCase
import com.kebunby.kebunby.ui.feature.plant_list.PlantListViewModel
import com.kebunby.kebunby.util.DiffCallback
import com.kebunby.kebunby.util.ListCallback
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generatePlants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlantListViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    // Dependency
    @Mock
    private lateinit var plantRemoteDataSource: PlantRemoteDataSource

    @Mock
    private lateinit var getPagingPlantsUseCase: GetPagingPlantsUseCase

    @Mock
    private lateinit var getPagingPlantsByCategoryUseCase: GetPagingPlantsByCategoryUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    // SUT
    private lateinit var plantListViewModel: PlantListViewModel

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle().apply {
            set("isTrending", true)
            set("forBeginner", false)
            set("searchQuery", null)
            set("categoryId", 0)
            set("category", "Tanaman Hias")
        }

        plantListViewModel = PlantListViewModel(
            savedStateHandle,
            getPagingPlantsUseCase,
            getPagingPlantsByCategoryUseCase
        )
    }

    @Test
    fun getPagingPlants_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            doReturn(
                flow { emit(PagingData.from(generatePlants())) }
            ).`when`(getPagingPlantsUseCase).invoke(
                isTrending = anyBoolean(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )

            // Act
            val pagingPlants = plantListViewModel.getPagingPlants().first()
            val differ = AsyncPagingDataDiffer(
                diffCallback = DiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingPlants)

            // Assert
            assertEquals(generatePlants(), differ.snapshot().items)

            // Verify
            verify(getPagingPlantsUseCase).invoke(
                isTrending = anyBoolean(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun getPagingPlantsByCategory_ShouldReturnSuccess() {
        testCoroutineRule.runBlockingTest {
            // Arrange
            doReturn(
                flow { emit(PagingData.from(generatePlants())) }
            ).`when`(getPagingPlantsByCategoryUseCase).invoke(anyInt())

            // Act
            val pagingPlants = plantListViewModel.getPagingPlantsByCategory().first()
            val differ = AsyncPagingDataDiffer(
                diffCallback = DiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingPlants)

            // Assert
            assertEquals(generatePlants(), differ.snapshot().items)

            // Verify
            verify(getPagingPlantsByCategoryUseCase).invoke(anyInt())
        }
    }
}