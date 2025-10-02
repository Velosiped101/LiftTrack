package com.velosiped.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val lightColors = CustomColors(
    selectedOptionColor = Color(0xFF2E7D32),
    graphLineColor = Color(0xFF4A90E2),
    pagingIndicatorColor = Color(0xFFC4C4C4),
    primaryTextColor = Color(0xFF000000),
    linearProgressIndicatorColor = Color(0xFF000000),
    popUpWindowBackgroundColor = Color(0xFFFFFFFF),
    progressColors = CustomColors.ProgressColors(
        notAchievedColor = Color(0xFFD72638),
        littleAchievedColor = Color(0xFFF46036),
        almostAchievedColor = Color(0xFFA3B18A),
        achievedColor = Color(0xFF6A994E)
    ),
    graphColors = CustomColors.GraphColors(
        lineColor = Color(0xFF000000)
    ),
    searchBarColors = CustomColors.SearchBarColors(
        focusedOutlineColor = Color(0xFF000000),
        unfocusedOutlineColor = Color(0xFF000000),
        focusedTextColor = Color(0xFF000000),
        unfocusedTextColor = Color(0xFF000000),
    ),
    boxCardColors = CustomColors.BoxCardColors(
        borderColor = Color(0xFF000000),
        containerColor = Color(0xFFFFFFFF),
        markedForDeleteColor = Color(0x4DFF0000)
    ),
    textFieldColors = CustomColors.TextFieldColors(
        textColor = Color(0xFF000000),
        cursorColor = Color(0xFF000000),
        tearDropColor = Color(0xFF000000),
        readOnlyColor = Color(0xFFE1E1E1)
    ),
    nutrientColors = CustomColors.NutrientColors(
        proteinColor = Color(0xFF7B61FF),
        fatColor = Color(0xFFFFD580),
        carbsColor = Color(0xFFA3D977)
    ),
    mainCardColors = CustomColors.MainCardColors(
        containerColor = Color(0xFFFFFFFF),
        borderColor = Color(0xFF000000)
    ),
    outlinedButtonColors = CustomColors.OutlinedButtonColors(
        containerColor = Color(0x00FFFFFF),
        contentColor = Color(0xFF000000),
        disabledContainerColor = Color(0x00FFFFFF),
        disabledContentColor = Color(0xFFA1A1A1),
        borderColor = Color(0xFF000000)
    ),
    dividerColor = Color(0xFF000000),
    iconsTintColor = Color(0xFF000000),
    mainBackgroundColor = Color(0xFFF3F3F3),
    circularProgressIndicatorColor = Color(0xFF000000),
    floatingButtonColor = Color(0xFFC7C7C7),
    topBarBackgroundColor = Color(0xFFF3F3F3),
    radioButtonColors = CustomColors.RadioButtonColors(
        selectedColor = Color(0xFF000000),
        unselectedColor = Color(0xFF505050)
    ),
    segmentedButtonColors = CustomColors.SegmentedButtonColors(
        activeBorderColor = Color(0xFF000000),
        activeContainerColor = Color(0xFFFFFFFF),
        activeContentColor = Color(0xFF000000),
        inactiveBorderColor = Color(0xFF000000),
        inactiveContainerColor = Color(0xFFFFFFFF),
        inactiveContentColor = Color(0xFF000000)
    ),
    sliderColors = CustomColors.SliderColors(
        activeTrackColor = Color(0xFF000000),
        inactiveTrackColor = Color(0xFFA4A4A4),
        thumbColor = Color(0xFF000000)
    )
)

val darkColors = CustomColors(
    selectedOptionColor = Color(0xFF66BB6A),
    graphLineColor = Color(0xFF5DADE2),
    pagingIndicatorColor = Color(0xFF313131),
    primaryTextColor = Color(0xFFFFFFFF),
    linearProgressIndicatorColor = Color(0xFFC9C9C9),
    popUpWindowBackgroundColor = Color(0xFF1A1A1A),
    progressColors = CustomColors.ProgressColors(
        notAchievedColor = Color(0xFFB22222),
        littleAchievedColor = Color(0xFFCC5500),
        almostAchievedColor = Color(0xFF6B8E23),
        achievedColor = Color(0xFF3B7A57)
    ),
    graphColors = CustomColors.GraphColors(
        lineColor = Color(0xFFFFFFFF)
    ),
    searchBarColors = CustomColors.SearchBarColors(
        focusedOutlineColor = Color(0xFFFFFFFF),
        unfocusedOutlineColor = Color(0xFFFFFFFF),
        focusedTextColor = Color(0xFFFFFFFF),
        unfocusedTextColor = Color(0xFFFFFFFF)
    ),
    boxCardColors = CustomColors.BoxCardColors(
        borderColor = Color(0xFFFFFFFF),
        containerColor = Color(0xFF1A1A1A),
        markedForDeleteColor = Color(0x4DCE0000)
    ),
    textFieldColors = CustomColors.TextFieldColors(
        textColor = Color(0xFFFFFFFF),
        cursorColor = Color(0xFFFFFFFF),
        tearDropColor = Color(0xFFFFFFFF),
        readOnlyColor = Color(0xFF171717)
    ),
    nutrientColors = CustomColors.NutrientColors(
        proteinColor = Color(0xFF80BFFF),
        fatColor = Color(0xFFFFE082),
        carbsColor = Color(0xFFB2FF59)
    ),
    mainCardColors = CustomColors.MainCardColors(
        containerColor = Color(0xFF1A1A1A),
        borderColor = Color(0xFFFFFFFF)
    ),
    outlinedButtonColors = CustomColors.OutlinedButtonColors(
        containerColor = Color(0x00FFFFFF),
        contentColor = Color(0xFFFFFFFF),
        disabledContainerColor = Color(0x00FFFFFF),
        disabledContentColor = Color(0xFFC0C0C0),
        borderColor = Color(0xFFFFFFFF)
    ),
    dividerColor = Color(0xFFFFFFFF),
    iconsTintColor = Color(0xFFFFFFFF),
    mainBackgroundColor = Color(0xFF000000),
    circularProgressIndicatorColor = Color(0xFFFFFFFF),
    floatingButtonColor = Color(0xFF1E1E1E),
    topBarBackgroundColor = Color(0xFF000000),
    radioButtonColors = CustomColors.RadioButtonColors(
        selectedColor = Color(0xFFFFFFFF),
        unselectedColor = Color(0xFFD2D2D2)
    ),
    segmentedButtonColors = CustomColors.SegmentedButtonColors(
        activeBorderColor = Color(0xFFFFFFFF),
        activeContainerColor = Color(0xFF1A1A1A),
        activeContentColor = Color(0xFFFFFFFF),
        inactiveBorderColor = Color(0xFFFFFFFF),
        inactiveContainerColor = Color(0xFF1A1A1A),
        inactiveContentColor = Color(0xFFFFFFFF)
    ),
    sliderColors = CustomColors.SliderColors(
        activeTrackColor = Color(0xFFC9C9C9),
        inactiveTrackColor = Color(0xFFFFFFFF),
        thumbColor = Color(0xFFC9C9C9)
    )
)

@Immutable
data class CustomColors(
    val selectedOptionColor: Color,
    val graphLineColor: Color,
    val pagingIndicatorColor: Color,
    val popUpWindowBackgroundColor: Color,
    val dividerColor: Color,
    val iconsTintColor: Color,
    val mainBackgroundColor: Color,
    val circularProgressIndicatorColor: Color,
    val floatingButtonColor: Color,
    val topBarBackgroundColor: Color,
    val linearProgressIndicatorColor: Color,
    val radioButtonColors: RadioButtonColors,
    val segmentedButtonColors: SegmentedButtonColors,
    val progressColors: ProgressColors,
    val graphColors: GraphColors,
    val searchBarColors: SearchBarColors,
    val boxCardColors: BoxCardColors,
    val textFieldColors: TextFieldColors,
    val nutrientColors: NutrientColors,
    val mainCardColors: MainCardColors,
    val outlinedButtonColors: OutlinedButtonColors,
    val sliderColors: SliderColors,
    val primaryTextColor: Color
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
        val unfocusedOutlineColor: Color,
        val focusedTextColor: Color,
        val unfocusedTextColor: Color
    )
    @Immutable
    data class BoxCardColors(
        val borderColor: Color,
        val containerColor: Color,
        val markedForDeleteColor: Color
    )
    @Immutable
    data class TextFieldColors(
        val textColor: Color,
        val cursorColor: Color,
        val tearDropColor: Color,
        val readOnlyColor: Color
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
        val disabledContentColor: Color,
        val borderColor: Color
    )
    @Immutable
    data class RadioButtonColors(
        val selectedColor: Color,
        val unselectedColor: Color
    )
    @Immutable
    data class SegmentedButtonColors(
        val activeBorderColor: Color,
        val activeContainerColor: Color,
        val activeContentColor: Color,
        val inactiveBorderColor: Color,
        val inactiveContainerColor: Color,
        val inactiveContentColor: Color
    )
    @Immutable
    data class SliderColors(
        val activeTrackColor: Color,
        val inactiveTrackColor: Color,
        val thumbColor: Color
    )
}