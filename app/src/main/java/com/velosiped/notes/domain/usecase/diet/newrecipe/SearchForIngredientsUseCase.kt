package com.velosiped.notes.domain.usecase.diet.newrecipe

import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.domain.repository.DietRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SearchForIngredientsUseCase @Inject constructor(
    private val dietRepository: DietRepository
){
    operator fun invoke(name: String): Flow<List<Ingredient>> {
        return if (name.length >= 3) dietRepository.getIngredient(name) else flowOf(emptyList())
    }
}