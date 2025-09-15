package com.velosiped.notes.presentation.screens.diet.foodManagerScreen

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.net.toUri
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.components.ImageOperationsDialog
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodManagerUiState.FoodInput
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components.FoodManagerTopBar
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components.editpage.FoodManagerEditPage
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components.mainpage.FoodManagerMainPage
import com.velosiped.notes.presentation.screens.utils.getTakePhotoLauncher
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.ONE
import com.velosiped.notes.utils.TWO
import com.velosiped.notes.utils.ZERO
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun FoodManagerScreen(
    tempFileUri: Uri?,
    foodInput: FoodInput,
    foodList: List<Food>,
    inDeleteMode: Boolean,
    markedForDeleteList: List<Food>,
    loadingFinished: SharedFlow<Unit>,
    setInitialFoodInputState: (Food?) -> Unit,
    onPhotoTaken: () -> Unit,
    onDeleteCurrentPhoto: () -> Unit,
    exitDeleteMode: () -> Unit,
    onDeleteMarkedFood: () -> Unit,
    onSaveChanges: () -> Unit,
    onNameChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
    markFoodForDelete: (Food) -> Unit,
    onNavigateBack: () -> Unit
) {
    var finishedLoading by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        loadingFinished.collect {
            finishedLoading = true
        }
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val focusManager: FocusManager = LocalFocusManager.current

    val mainPage = Int.ZERO
    val editPage = Int.ONE
    val pageCount = Int.TWO
    val pagerState = rememberPagerState(initialPage = mainPage, pageCount = { pageCount })

    val launchCamera = getTakePhotoLauncher(
        uri = tempFileUri,
        context = context,
        onSuccess = onPhotoTaken
    )

    var dialogIsActive by remember { mutableStateOf(false) }

    fun handleBackPress() {
        focusManager.clearFocus()
        if (pagerState.currentPage == editPage) {
            scope.launch { pagerState.animateScrollToPage(mainPage) }
        } else {
            if (inDeleteMode) exitDeleteMode()
            else onNavigateBack()
        }
    }

    BackHandler { handleBackPress() }

    Scaffold(
        topBar = {
            FoodManagerTopBar(
                inDeleteMode = inDeleteMode,
                onMainPage = pagerState.currentPage == mainPage,
                onNavigateBack = { handleBackPress() },
                onAdd = {
                    setInitialFoodInputState(null)
                    scope.launch { pagerState.animateScrollToPage(editPage) }
                },
                onDelete = onDeleteMarkedFood,
                onSave = {
                    focusManager.clearFocus()
                    if (foodInput.allParametersAreFilled) {
                        onSaveChanges()
                        scope.launch { pagerState.animateScrollToPage(mainPage) }
                    }
                }
            )
        },
        containerColor = CustomTheme.colors.mainBackgroundColor
    ) { innerPadding ->
        AnimatedVisibility(
            visible = finishedLoading,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    mainPage -> FoodManagerMainPage(
                        foodList = foodList,
                        markedForDeleteList = markedForDeleteList,
                        onFoodClicked = {
                            if (!inDeleteMode) {
                                setInitialFoodInputState(it)
                                scope.launch { pagerState.animateScrollToPage(editPage) }
                            } else {
                                markFoodForDelete(it)
                            }
                        },
                        onLongClick = {
                            if (!inDeleteMode) { markFoodForDelete(it) }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    editPage -> FoodManagerEditPage(
                        food = foodInput,
                        onNameChange = onNameChange,
                        onProteinChange = onProteinChange,
                        onFatChange = onFatChange,
                        onCarbsChange = onCarbsChange,
                        onImageClick = {
                            if (foodInput.imageUri != null) {
                                dialogIsActive = true
                            } else { launchCamera() }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        if (dialogIsActive) {
            ImageOperationsDialog(
                onTakeNewPhoto = {
                    launchCamera()
                    dialogIsActive = false
                },
                onDeleteCurrentPhoto = {
                    onDeleteCurrentPhoto()
                    dialogIsActive = false
                },
                onDismiss = { dialogIsActive = false },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}