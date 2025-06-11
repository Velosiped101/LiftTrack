package com.velosiped.notes.domain.usecase.diet.addmeal

import androidx.paging.PagingData
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.utils.SearchMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class SearchFoodUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    operator fun invoke(name: String, searchMode: SearchMode): Flow<PagingData<Food>> {
        val searchQuery = name.trim()
        if (searchQuery.isBlank()) return emptyFlow()
        return dietRepository
            .getFoodPage(searchQuery, searchMode)
            .distinctUntilChanged()
    }
}