package com.velosiped.notes.domain.usecase.main

import com.google.common.truth.Truth.assertThat
import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.data.repository.program.FakeProgramRepository
import com.velosiped.notes.data.repository.tempprogress.FakeAppProtoDataStoreRepository
import com.velosiped.notes.utils.toProgramTempProgressItemsList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CheckForProgramUpdateUseCaseTest {
    private lateinit var checkForProgramUpdate: CheckForProgramUpdateUseCase

    private lateinit var programRepository: FakeProgramRepository
    private lateinit var dataStoreRepository: FakeAppProtoDataStoreRepository

    @Before
    fun setup() {
        programRepository = FakeProgramRepository()
        dataStoreRepository = FakeAppProtoDataStoreRepository()
        checkForProgramUpdate = CheckForProgramUpdateUseCase(programRepository, dataStoreRepository)
    }

    @Test
    fun `Saved progress is empty, changes not found`(): Unit = runBlocking {
        val changesFound = checkForProgramUpdate()
        assertThat(changesFound).isFalse()
    }

    @Test
    fun `Saved progress has different size, changes found`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "monday", exercise = "exercise 1", reps = 8),
            Program(dayOfWeek = "monday", exercise = "exercise 2", reps = 7),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 4)
        )
        val savedProgress = listOf(
            ProgramProgress(exercise = "exercise 1", repsPlanned = 8, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 2", repsPlanned = 7, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 3", repsPlanned = 4, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 4", repsPlanned = 6, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
        ).toProgramTempProgressItemsList()
        programRepository.populateProgramForToday(program)
        dataStoreRepository.saveTempProgramProgress(savedProgress)
        val changesFound = checkForProgramUpdate()
        assertThat(changesFound).isTrue()
    }

    @Test
    fun `Saved progress and program are the same size but different exercises, changes found`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "monday", exercise = "exercise 1", reps = 8),
            Program(dayOfWeek = "monday", exercise = "exercise 2", reps = 7),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 4)
        )
        val savedProgress = listOf(
            ProgramProgress(exercise = "exercise 1", repsPlanned = 8, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 4", repsPlanned = 7, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 3", repsPlanned = 4, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
        ).toProgramTempProgressItemsList()
        programRepository.populateProgramForToday(program)
        dataStoreRepository.saveTempProgramProgress(savedProgress)
        val changesFound = checkForProgramUpdate()
        assertThat(changesFound).isTrue()
    }

    @Test
    fun `Saved progress and program are the same size and exercises but different reps, changes found`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "monday", exercise = "exercise 1", reps = 8),
            Program(dayOfWeek = "monday", exercise = "exercise 2", reps = 7),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 4)
        )
        val savedProgress = listOf(
            ProgramProgress(exercise = "exercise 1", repsPlanned = 20, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 2", repsPlanned = 7, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 3", repsPlanned = 4, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
        ).toProgramTempProgressItemsList()
        programRepository.populateProgramForToday(program)
        dataStoreRepository.saveTempProgramProgress(savedProgress)
        val changesFound = checkForProgramUpdate()
        assertThat(changesFound).isTrue()
    }

    @Test
    fun `Saved progress and program are the same size but different exercises and reps, changes found`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "monday", exercise = "exercise 1", reps = 8),
            Program(dayOfWeek = "monday", exercise = "exercise 2", reps = 7),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 4)
        )
        val savedProgress = listOf(
            ProgramProgress(exercise = "exercise 1", repsPlanned = 8, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 33", repsPlanned = 7, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 3", repsPlanned = 14, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
        ).toProgramTempProgressItemsList()
        programRepository.populateProgramForToday(program)
        dataStoreRepository.saveTempProgramProgress(savedProgress)
        val changesFound = checkForProgramUpdate()
        assertThat(changesFound).isTrue()
    }

    @Test
    fun `Saved progress and program have the same size, exercises and reps, changes not found`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "monday", exercise = "exercise 1", reps = 8),
            Program(dayOfWeek = "monday", exercise = "exercise 2", reps = 7),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 4)
        )
        val savedProgress = listOf(
            ProgramProgress(exercise = "exercise 1", repsPlanned = 8, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 2", repsPlanned = 7, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
            ProgramProgress(exercise = "exercise 3", repsPlanned = 4, day = 1, month = 1, year = 1, weight = 5f, reps = 10),
        ).toProgramTempProgressItemsList()
        programRepository.populateProgramForToday(program)
        dataStoreRepository.saveTempProgramProgress(savedProgress)
        val changesFound = checkForProgramUpdate()
        assertThat(changesFound).isFalse()
    }
}