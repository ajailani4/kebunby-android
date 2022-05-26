package com.kebunby.kebunby.viewmodel

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kebunby.kebunby.domain.use_case.plant.GetPagingPlantsUseCase
import com.kebunby.kebunby.ui.feature.explore.ExploreEvent
import com.kebunby.kebunby.ui.feature.explore.ExploreViewModel
import com.kebunby.kebunby.util.DiffCallback
import com.kebunby.kebunby.util.ListCallback
import com.kebunby.kebunby.util.TestCoroutineRule
import com.kebunby.kebunby.util.generatePlants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ExploreViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getPagingPlantsUseCase: GetPagingPlantsUseCase

    private lateinit var exploreViewModel: ExploreViewModel

    @Before
    fun setUp() {
        exploreViewModel = ExploreViewModel(getPagingPlantsUseCase)
    }

    @Test
    fun `Get paging plants by search query should return success`() {
        testCoroutineRule.runBlockingTest {
            exploreViewModel.onSearchQueryChanged("plant")
            doReturn(
                flow { emit(PagingData.from(generatePlants())) }
            ).`when`(getPagingPlantsUseCase).invoke(
                isTrending = null,
                forBeginner = null,
                searchQuery = exploreViewModel.searchQuery
            )

            exploreViewModel.onEvent(ExploreEvent.LoadPlants)

            val pagingPlants = exploreViewModel.pagingPlants.value
            val differ = AsyncPagingDataDiffer(
                diffCallback = DiffCallback(),
                updateCallback = ListCallback(),
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingPlants)

            assertEquals(generatePlants(), differ.snapshot().items)

            verify(getPagingPlantsUseCase).invoke(
                isTrending = null,
                forBeginner = null,
                searchQuery = exploreViewModel.searchQuery
            )
        }
    }
}