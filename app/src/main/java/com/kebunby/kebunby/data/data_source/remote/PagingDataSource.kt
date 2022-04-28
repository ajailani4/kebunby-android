package com.kebunby.kebunby.data.data_source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kebunby.kebunby.data.model.response.BaseResponse
import retrofit2.Response
import java.lang.Exception

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
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<Int, T>) = state.anchorPosition
}