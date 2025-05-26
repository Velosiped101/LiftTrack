package com.example.notes.data.repository.diet

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.notes.data.api.foodApi.FoodApiService
import com.example.notes.data.api.foodApi.toFoodList
import com.example.notes.data.database.food.Food
import retrofit2.HttpException
import java.io.IOException

class RemotePagingSource(
    private val apiService: FoodApiService,
    private val searchTerms: String
): PagingSource<Int, Food>() {
    override fun getRefreshKey(state: PagingState<Int, Food>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Food> {
        try {
            val pageNumber = params.key ?: 1
            val response = apiService.getFood(searchTerms = searchTerms, page = pageNumber)
            val data = response.products.toFoodList()
            val nextKey = if (data.isEmpty()) null else response.page + 1
            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}