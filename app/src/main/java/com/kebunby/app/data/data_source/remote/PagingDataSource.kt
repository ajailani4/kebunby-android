package com.kebunby.app.data.data_source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kebunby.app.data.model.response.BaseResponse
import retrofit2.Response

class PagingDataSource<T : Any>(
    private inline val serviceMethod: suspend (page: Int, size: Int) -> Response<BaseResponse<List<T>>>
) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>) =
        try {
            val currentPage = params.key ?: 1
            val response = serviceMethod(currentPage, 10)
            val data = response.body()?.data ?: emptyList<T>()

            val prevKey = if (currentPage == 1) null else currentPage.minus(1)

            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = if (data.isNotEmpty()) currentPage.plus(1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, T>) = state.anchorPosition
}