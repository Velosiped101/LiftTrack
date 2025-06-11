package com.velosiped.notes.domain.usecase.diet

import com.velosiped.notes.domain.usecase.diet.addmeal.ConfirmMealAdditionUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.ManagePickedFoodListUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.ObserveTotalNutrientsUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.SearchFoodUseCase
import com.velosiped.notes.domain.usecase.diet.addmeal.ValidateMassUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.AddFoodToDbUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.CreateImageFileUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.DeleteFoodFromDbUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.DeleteImageFileUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.FoodClickedInDeleteModeUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.GetFoodInformationUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.ObserveFoodListUseCase
import com.velosiped.notes.domain.usecase.diet.foodmanager.ValidateFoodInputUseCase
import com.velosiped.notes.domain.usecase.diet.newrecipe.CreateNewFoodUseCase
import com.velosiped.notes.domain.usecase.diet.newrecipe.CreateNewIngredientsUseCase
import com.velosiped.notes.domain.usecase.diet.newrecipe.SearchForIngredientsUseCase

data class DietUseCase(
    val searchFoodUseCase: SearchFoodUseCase,
    val confirmMealAdditionUseCase: ConfirmMealAdditionUseCase,
    val managePickedFoodListUseCase: ManagePickedFoodListUseCase,
    val observeTotalNutrientsUseCase: ObserveTotalNutrientsUseCase,
    val validateMassUseCase: ValidateMassUseCase,
    val observeFoodListUseCase: ObserveFoodListUseCase,
    val getFoodInformationUseCase: GetFoodInformationUseCase,
    val deleteFoodFromDbUseCase: DeleteFoodFromDbUseCase,
    val foodClickedInDeleteModeUseCase: FoodClickedInDeleteModeUseCase,
    val validateFoodInputUseCase: ValidateFoodInputUseCase,
    val addFoodToDbUseCase: AddFoodToDbUseCase,
    val deleteImageFileUseCase: DeleteImageFileUseCase,
    val createImageFileUseCase: CreateImageFileUseCase,
    val createNewFoodUseCase: CreateNewFoodUseCase,
    val createNewIngredientsUseCase: CreateNewIngredientsUseCase,
    val searchForIngredientsUseCase: SearchForIngredientsUseCase
)