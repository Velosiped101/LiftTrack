package com.velosiped.notes.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.velosiped.notes.ui.theme.CustomColors.GraphColors
import com.velosiped.notes.ui.theme.CustomColors.ListItemColors
import com.velosiped.notes.ui.theme.CustomColors.MainCardColors
import com.velosiped.notes.ui.theme.CustomColors.NutrientColors
import com.velosiped.notes.ui.theme.CustomColors.OutlinedButtonColors
import com.velosiped.notes.ui.theme.CustomColors.ProgressColors
import com.velosiped.notes.ui.theme.CustomColors.SearchBarColors
import com.velosiped.notes.ui.theme.CustomColors.TextFieldColors

val SelectedOptionDark = Color(0xFF66BB6A)
val SelectedOptionLight = Color(0xFF2E7D32)

val ReadOnlyFieldColorDark = Color(0xFF313131)
val ReadOnlyFieldColorLight = Color(0xFFC4C4C4)

val VolumeColorLight = Color(0xFF4A90E2)
val OneRepMaxColorLight = Color(0xFFE63946)
val AvgWeightColorLight = Color(0xFFF4A261)
val AvgRepsColorLight = Color(0xFF2A9D8F)
val AvgPlannedRepsColorLight = Color(0xFFE9C46A)

val VolumeColorDark = Color(0xFF5DADE2)
val OneRepMaxColorDark = Color(0xFFEC7063)
val AvgWeightColorDark = Color(0xFFF39C12)
val AvgRepsColorDark = Color(0xFF1ABC9C)
val AvgPlannedRepsColorDark = Color(0xFFF7DC6F)

val lightColors = CustomColors(
    selectedOptionColor = SelectedOptionLight,
    volumeColor = VolumeColorLight,
    oneRepMaxColor = OneRepMaxColorLight,
    avgRepsDoneColor = AvgRepsColorLight,
    avgRepsPlannedColor = AvgPlannedRepsColorLight,
    avgWeightDoneColor = AvgWeightColorLight,
    readOnlyFieldColor = ReadOnlyFieldColorLight,
    popUpWindowBackgroundColor = Color(0xFFFFFFFF),
    progressColors = ProgressColors(
        notAchievedColor = Color(0xFFD72638),
        littleAchievedColor = Color(0xFFF46036),
        almostAchievedColor = Color(0xFFA3B18A),
        achievedColor = Color(0xFF6A994E)
    ),
    graphColors = GraphColors(
        lineColor = Color(0xFF000000)
    ),
    searchBarColors = SearchBarColors(
        focusedOutlineColor = Color(0xFF000000),
        unfocusedOutlineColor = Color(0xFF000000)
    ),
    listItemColors = ListItemColors(
        borderColor = Color(0xFF000000),
        containerColor = Color(0xFFFFFFFF),
        markedForDeleteColor = Color(0x4DFF0000)
    ),
    textFieldColors = TextFieldColors(
        textColor = Color(0xFF000000),
        cursorColor = Color(0xFF000000),
        tearDropColor = Color(0xFF000000)
    ),
    nutrientColors = NutrientColors(
        proteinColor = Color(0xFF7B61FF),
        fatColor = Color(0xFFFFD580),
        carbsColor = Color(0xFFA3D977)
    ),
    mainCardColors = MainCardColors(
        containerColor = Color(0xFFFFFFFF),
        borderColor = Color(0xFF000000)
    ),
    outlinedButtonColors = OutlinedButtonColors(
        containerColor = Color(0x00FFFFFF),
        contentColor = Color(0xFF000000),
        disabledContainerColor = Color(0x00FFFFFF),
        disabledContentColor = Color(0xFF3F3F3F)
    ),
    dividerColor = Color(0xFF000000),
    iconsTintColor = Color(0xFF000000),
    mainBackgroundColor = Color(0xFFEEEEEE),
    circularProgressIndicatorColor = Color(0xFF000000),
    floatingButtonColor = Color(0xFF000000),
    topBarBackgroundColor = Color(0xFFFFFFFF),
)

val darkColors = CustomColors(
    selectedOptionColor = SelectedOptionDark,
    volumeColor = VolumeColorDark,
    oneRepMaxColor = OneRepMaxColorDark,
    avgRepsDoneColor = AvgRepsColorDark,
    avgRepsPlannedColor = AvgPlannedRepsColorDark,
    avgWeightDoneColor = AvgWeightColorDark,
    readOnlyFieldColor = ReadOnlyFieldColorDark,
    popUpWindowBackgroundColor = Color(0xFFFFFFFF),
    progressColors = ProgressColors(
        notAchievedColor = Color(0xFFB22222),
        littleAchievedColor = Color(0xFFCC5500),
        almostAchievedColor = Color(0xFF6B8E23),
        achievedColor = Color(0xFF3B7A57)
    ),
    graphColors = GraphColors(
        lineColor = Color(0xFFFFFFFF)
    ),
    searchBarColors = SearchBarColors(
        focusedOutlineColor = Color(0xFFFFFFFF),
        unfocusedOutlineColor = Color(0xFFFFFFFF)
    ),
    listItemColors = ListItemColors(
        borderColor = Color(0xFFFFFFFF),
        containerColor = Color(0xFF000000),
        markedForDeleteColor = Color(0x4D650000)
    ),
    textFieldColors = TextFieldColors(
        textColor = Color(0xFFFFFFFF),
        cursorColor = Color(0xFFFFFFFF),
        tearDropColor = Color(0xFFFFFFFF)
    ),
    nutrientColors = NutrientColors(
        proteinColor = Color(0xFF80BFFF),
        fatColor = Color(0xFFFFE082),
        carbsColor = Color(0xFFB2FF59)
    ),
    mainCardColors = MainCardColors(
        containerColor = Color(0xFF000000),
        borderColor = Color(0xFFFFFFFF)
    ),
    outlinedButtonColors = OutlinedButtonColors(
        containerColor = Color(0x00FFFFFF),
        contentColor = Color(0xFFFFFFFF),
        disabledContainerColor = Color(0x00FFFFFF),
        disabledContentColor = Color(0xFFC0C0C0)
    ),
    dividerColor = Color(0xFFFFFFFF),
    iconsTintColor = Color(0xFFFFFFFF),
    mainBackgroundColor = Color(0xFF000000),
    circularProgressIndicatorColor = Color(0xFFFFFFFF),
    floatingButtonColor = Color(0xFFFFFFFF),
    topBarBackgroundColor = Color(0xFF000000),
)

@Immutable
data class CustomColors(
    val selectedOptionColor: Color,
    val volumeColor: Color,
    val oneRepMaxColor: Color,
    val avgRepsDoneColor: Color,
    val avgRepsPlannedColor: Color,
    val avgWeightDoneColor: Color,
    val readOnlyFieldColor: Color,
    val popUpWindowBackgroundColor: Color,


    val progressColors: ProgressColors,
    val graphColors: GraphColors,
    val searchBarColors: SearchBarColors,
    val listItemColors: ListItemColors,
    val textFieldColors: TextFieldColors,
    val nutrientColors: NutrientColors,
    val mainCardColors: MainCardColors,
    val outlinedButtonColors: OutlinedButtonColors,


    val dividerColor: Color,
    val iconsTintColor: Color,
    val mainBackgroundColor: Color,
    val circularProgressIndicatorColor: Color,
    val floatingButtonColor: Color,
    val topBarBackgroundColor: Color
) {
    @Immutable
    data class ProgressColors(
        val notAchievedColor: Color,
        val littleAchievedColor: Color,
        val almostAchievedColor: Color,
        val achievedColor: Color
    )
    @Immutable
    data class GraphColors(
        val lineColor: Color
    )
    @Immutable
    data class SearchBarColors(
        val focusedOutlineColor: Color,
        val unfocusedOutlineColor: Color
    )
    @Immutable
    data class ListItemColors(
        val borderColor: Color,
        val containerColor: Color,
        val markedForDeleteColor: Color
    )
    @Immutable
    data class TextFieldColors(
        val textColor: Color,
        val cursorColor: Color,
        val tearDropColor: Color
    )
    @Immutable
    data class NutrientColors(
        val proteinColor: Color,
        val fatColor: Color,
        val carbsColor: Color
    )
    @Immutable
    data class MainCardColors(
        val containerColor: Color,
        val borderColor: Color
    )
    @Immutable
    data class OutlinedButtonColors(
        val containerColor: Color,
        val contentColor: Color,
        val disabledContainerColor: Color,
        val disabledContentColor: Color
    )
}