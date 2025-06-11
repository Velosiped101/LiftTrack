package com.velosiped.notes.domain.usecase.training.programedit

import com.google.common.truth.Truth.assertThat
import com.velosiped.notes.data.database.program.Program
import com.velosiped.notes.data.repository.program.FakeProgramRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ManageDayInProgramUseCaseTest {
    private lateinit var useCase: ManageDayInProgramUseCase
    private lateinit var repository: FakeProgramRepository

    @Before
    fun setup() {
        repository = FakeProgramRepository()
        useCase = ManageDayInProgramUseCase(repository)
    }

    @Test
    fun `Delete program for Monday, program doesn't contain elements with day Monday`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "day 1", exercise = "exercise 1", reps = 10),
            Program(dayOfWeek = "day 2", exercise = "exercise 2", reps = 10),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 10),
            Program(dayOfWeek = "monday", exercise = "exercise 4", reps = 10),
            Program(dayOfWeek = "day 3", exercise = "exercise 5", reps = 10)
        )
        program.forEach { repository.insertToProgram(it) }
        val initialProgram = repository.getAll().first()
        assertThat(initialProgram).hasSize(5)
        repository.dropProgramForDay("monday")
        val updatedProgram = repository.getAll().first()
        val programItemsDays = updatedProgram.map { it.dayOfWeek }
        assertThat(updatedProgram).hasSize(3)
        assertThat(programItemsDays).doesNotContain("monday")
    }

    @Test
    fun `Delete program for non-existing day, program doesn't change`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "day 1", exercise = "exercise 1", reps = 10),
            Program(dayOfWeek = "day 2", exercise = "exercise 2", reps = 10),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 10),
            Program(dayOfWeek = "monday", exercise = "exercise 4", reps = 10),
            Program(dayOfWeek = "day 3", exercise = "exercise 5", reps = 10)
        )
        program.forEach { repository.insertToProgram(it) }
        val initialProgram = repository.getAll().first()
        repository.dropProgramForDay("non-existing day")
        val updatedProgram = repository.getAll().first()
        assertThat(updatedProgram).isEqualTo(initialProgram)
    }

    @Test
    fun `Clear program, program empty`(): Unit = runBlocking {
        val program = listOf(
            Program(dayOfWeek = "day 1", exercise = "exercise 1", reps = 10),
            Program(dayOfWeek = "day 2", exercise = "exercise 2", reps = 10),
            Program(dayOfWeek = "monday", exercise = "exercise 3", reps = 10),
            Program(dayOfWeek = "monday", exercise = "exercise 4", reps = 10),
            Program(dayOfWeek = "day 3", exercise = "exercise 5", reps = 10)
        )
        program.forEach { repository.insertToProgram(it) }
        repository.dropProgram()
        val updatedProgram = repository.getAll().first()
        assertThat(updatedProgram).isEmpty()
    }
}