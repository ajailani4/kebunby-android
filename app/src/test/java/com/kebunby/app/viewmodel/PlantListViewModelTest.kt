package com.kebunby.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kebunby.app.domain.use_case.plant.GetPagingPlantsByCategoryUseCase
import com.kebunby.app.domain.use_case.plant.GetPagingPlantsUseCase
import com.kebunby.app.ui.feature.plant_list.PlantListEvent
import com.kebunby.app.ui.feature.plant_list.PlantListViewModel
import com.kebunby.app.util.DiffCallback
import com.kebunby.app.util.ListCallback
import com.kebunby.app.util.TestCoroutineRule
import com.kebunby.app.util.generatePlants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Mock
    private lateinit var getPagingPlantsUseCase: GetPagingPlantsUseCase

    @Mock
    private lateinit var getPagingPlantsByCategoryUseCase: GetPagingPlantsByCategoryUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var plantListViewModel: PlantListViewModel

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle().apply {
            set("isTrending", true)
            set("forBeginner", false)
            set("searchQuery", null)
            set("categoryId", 0)
            set("name", "Tanaman Hias")
        }
    }

    @Test
    fun `Get plants should return success`() {
        testCoroutineRule.runTest {
            doReturn(
                flow { emit(PagingData.from(generatePlants())) }
            ).`when`(getPagingPlantsUseCase)(
                isTrending = anyBoolean(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )

            plantListViewModel = PlantListViewModel(
                savedStateHandle,
                getPagingPlantsUseCase,
                getPagingPlantsByCategoryUseCase
            )

            val pagingPlants = plantListViewModel.pagingPlants.value
            val differ = AsyncPagingDataDiffer(
                diffCallback = DiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingPlants)

            assertEquals(generatePlants(), differ.snapshot().items)

            verify(getPagingPlantsUseCase)(
                isTrending = anyBoolean(),
                forBeginner = anyBoolean(),
                searchQuery = isNull()
            )
        }
    }

    @Test
    fun getPagingPlantsByCategory_ShouldReturnSuccess() {
        testCoroutineRule.runTest {
            doReturn(
                flow { emit(PagingData.from(generatePlants())) }
            ).`when`(getPagingPlantsByCategoryUseCase)(anyInt())

            plantListViewModel = PlantListViewModel(
                savedStateHandle,
                getPagingPlantsUseCase,
                getPagingPlantsByCategoryUseCase
            )
            plantListViewModel.onEvent(PlantListEvent.LoadPlantsByCategory)

            val pagingPlants = plantListViewModel.pagingPlants.value
            val differ = AsyncPagingDataDiffer(
                diffCallback = DiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingPlants)

            assertEquals(generatePlants(), differ.snapshot().items)

            verify(getPagingPlantsByCategoryUseCase)(anyInt())
        }
    }
}