package com.velosiped.notes.domain.usecase.training

import com.velosiped.notes.domain.usecase.training.programedit.GetExerciseListUseCase
import com.velosiped.notes.domain.usecase.training.programedit.ManageDayInProgramUseCase
import com.velosiped.notes.domain.usecase.training.programedit.ManageExerciseInProgramUseCase
import com.velosiped.notes.domain.usecase.training.programedit.ObserveProgramUseCase
import com.velosiped.notes.domain.usecase.training.programexec.FinishTrainingUseCase
import com.velosiped.notes.domain.usecase.training.programexec.GetTrainingStateUseCase
import com.velosiped.notes.domain.usecase.training.programexec.UpdateStoredProgressUseCase

data class TrainingUseCase(
    val observeProgramUseCase: ObserveProgramUseCase,
    val getExerciseListUseCase: GetExerciseListUseCase,
    val manageExerciseInProgramUseCase: ManageExerciseInProgramUseCase,
    val manageDayInProgramUseCase: ManageDayInProgramUseCase,
    val getTrainingStateUseCase: GetTrainingStateUseCase,
    val finishTrainingUseCase: FinishTrainingUseCase,
    val updateStoredProgressUseCase: UpdateStoredProgressUseCase
)