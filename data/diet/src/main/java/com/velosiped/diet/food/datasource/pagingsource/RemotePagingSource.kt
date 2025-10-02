package com.velosiped.diet.food.datasource.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.velosiped.diet.food.datasource.api.foodApi.FoodApiService
import com.velosiped.diet.food.datasource.api.foodApi.toFoodList
import com.velosiped.diet.food.repository.Food
import retrofit2.HttpException
import java.io.IOException

internal class RemotePagingSource(
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