package com.velosiped.foodmanager.presentation

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
import com.velosiped.diet.food.repository.Food
import com.velosiped.foodmanager.presentation.components.FoodManagerTopBar
import com.velosiped.foodmanager.presentation.components.editpage.FoodManagerEditPage
import com.velosiped.foodmanager.presentation.components.mainpage.FoodManagerMainPage
import com.velosiped.foodmanager.presentation.utils.FoodInputState
import com.velosiped.ui.components.image.ImageOperationsDialog
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.utils.getTakePhotoLauncher
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.TWO
import com.velosiped.utility.extensions.ZERO
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Composable
fun FoodManagerScreen(
    tempFileUri: Uri?,
    foodList: List<Food>,
    foodInputState: FoodInputState,
    markedForDeleteList: List<Food>,
    loadingFinished: SharedFlow<Unit>,
    onFoodClick: (Food?) -> Unit,
    onFoodLongClick: (Food) -> Unit,
    onPhotoTaken: () -> Unit,
    onDeleteCurrentPhoto: () -> Unit,
    onExitDeleteMode: () -> Unit,
    onDeleteMarkedFood: () -> Unit,
    onSaveChanges: () -> Unit,
    onNameChange: (String) -> Unit,
    onProteinChange: (String) -> Unit,
    onFatChange: (String) -> Unit,
    onCarbsChange: (String) -> Unit,
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

    val inDeleteMode = markedForDeleteList.isNotEmpty()

    fun handleBackPress() {
        focusManager.clearFocus()
        if (pagerState.currentPage == editPage) {
            scope.launch { pagerState.animateScrollToPage(mainPage) }
        } else {
            if (inDeleteMode) onExitDeleteMode()
            else onNavigateBack()
        }
    }

    BackHandler { handleBackPress() }

    Scaffold(
        topBar = {
            FoodManagerTopBar(
                inDeleteMode = inDeleteMode,
                onMainPage = pagerState.currentPage == mainPage,
                confirmButtonIsVisible = foodInputState.noEmptyFields,
                onNavigateBack = { handleBackPress() },
                onAdd = {
                    onFoodClick(null)
                    scope.launch { pagerState.animateScrollToPage(editPage) }
                },
                onDelete = onDeleteMarkedFood,
                onSave = {
                    focusManager.clearFocus()
                    if (foodInputState.noEmptyFields) {
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
                            onFoodClick(it)
                            if (!inDeleteMode) { scope.launch { pagerState.animateScrollToPage(editPage) } }
                        },
                        onLongClick = { onFoodLongClick(it) },
                        modifier = Modifier.fillMaxSize()
                    )
                    editPage -> FoodManagerEditPage(
                        foodInputState = foodInputState,
                        onNameChange = onNameChange,
                        onProteinChange = onProteinChange,
                        onFatChange = onFatChange,
                        onCarbsChange = onCarbsChange,
                        onImageClick = {
                            if (foodInputState.imageUri != null) {
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