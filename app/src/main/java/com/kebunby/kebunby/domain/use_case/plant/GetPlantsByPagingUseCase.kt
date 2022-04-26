package com.kebunby.kebunby.domain.use_case.plant

import com.kebunby.kebunby.data.repository.PlantRepository
import javax.inject.Inject

class GetPlantsByPagingUseCase @Inject constructor(
    private val plantRepository: PlantRepository
) {
    private fun getPlantsByPaging(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = plantRepository.getPlantsByPaging(
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )

    operator fun invoke(
        isTrending: Boolean?,
        forBeginner: Boolean?,
        searchQuery: String?
    ) = getPlantsByPaging(
        isTrending = isTrending,
        forBeginner = forBeginner,
        searchQuery = searchQuery
    )
}