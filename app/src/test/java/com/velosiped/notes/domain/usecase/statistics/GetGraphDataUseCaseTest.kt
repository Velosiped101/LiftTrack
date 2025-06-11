package com.velosiped.notes.domain.usecase.statistics

import com.google.common.truth.Truth.assertThat
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.data.repository.program.FakeProgramRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetGraphDataUseCaseTest {
    private lateinit var getGraphData: GetGraphDataUseCase
    private lateinit var repository: FakeProgramRepository

    @Before
    fun setup() {
        repository = FakeProgramRepository()
        getGraphData = GetGraphDataUseCase(repository)
    }

    @Test
    fun `Get graph data for volume formula, map size equals 3`(): Unit = runBlocking {
        val programProgressList = listOf(
            ProgramProgress(day = 1, month = 1, year = 2025, exercise = "exercise 1", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 10, month = 1, year = 2025, exercise = "exercise 2", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 20, month = 1, year = 2025, exercise = "exercise 3", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 5, month = 2, year = 2025, exercise = "exercise 1", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 15, month = 2, year = 2025, exercise = "exercise 3", reps = 5, repsPlanned = 8, weight = 100f),
        )
        repository.populateProgramProgress(programProgressList)
        val graphDataMap = getGraphData(GraphDataFormula.Volume).first()
        assertThat(graphDataMap).hasSize(3)
    }

    @Test
    fun `Get graph data for one rep max formula, map size equals 3`(): Unit = runBlocking {
        val programProgressList = listOf(
            ProgramProgress(day = 1, month = 1, year = 2025, exercise = "exercise 1", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 10, month = 1, year = 2025, exercise = "exercise 2", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 20, month = 1, year = 2025, exercise = "exercise 3", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 5, month = 2, year = 2025, exercise = "exercise 1", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 15, month = 2, year = 2025, exercise = "exercise 3", reps = 5, repsPlanned = 8, weight = 100f),
        )
        repository.populateProgramProgress(programProgressList)
        val graphDataMap = getGraphData(GraphDataFormula.OneRepMax).first()
        assertThat(graphDataMap).hasSize(3)
    }

    @Test
    fun `Get graph data for raw formula, map size equals 3`(): Unit = runBlocking {
        val programProgressList = listOf(
            ProgramProgress(day = 1, month = 1, year = 2025, exercise = "exercise 1", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 10, month = 1, year = 2025, exercise = "exercise 2", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 20, month = 1, year = 2025, exercise = "exercise 3", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 5, month = 2, year = 2025, exercise = "exercise 1", reps = 5, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 15, month = 2, year = 2025, exercise = "exercise 3", reps = 5, repsPlanned = 8, weight = 100f),
        )
        repository.populateProgramProgress(programProgressList)
        val graphDataMap = getGraphData(GraphDataFormula.Raw).first()
        assertThat(graphDataMap).hasSize(3)
    }

    @Test
    fun `Get graph data from empty source, map empty`(): Unit = runBlocking {
        val graphDataMap = getGraphData(GraphDataFormula.Volume).first()
        assertThat(graphDataMap).isEmpty()
    }

    @Test
    fun `Get graph data for volume formula, volume for exercise 1 calculated right`(): Unit = runBlocking {
        val programProgressList = listOf(
            ProgramProgress(day = 1, month = 1, year = 2025, exercise = "exercise 1", reps = 4, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 10, month = 1, year = 2025, exercise = "exercise 2", reps = 2, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 20, month = 1, year = 2025, exercise = "exercise 1", reps = 3, repsPlanned = 8, weight = 100f),
        )
        val volume = (4 * 100f).toDouble()
        repository.populateProgramProgress(programProgressList)
        val graphDataVolume = getGraphData(GraphDataFormula.Volume).first()["exercise 1"]!!.first().value!!
        assertThat(graphDataVolume).isEqualTo(volume)
    }

    @Test
    fun `Get graph data for one rep max formula, one rep max for exercise 1 calculated right`(): Unit = runBlocking {
        val programProgressList = listOf(
            ProgramProgress(day = 1, month = 1, year = 2025, exercise = "exercise 1", reps = 4, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 10, month = 1, year = 2025, exercise = "exercise 2", reps = 2, repsPlanned = 8, weight = 100f),
            ProgramProgress(day = 20, month = 1, year = 2025, exercise = "exercise 1", reps = 3, repsPlanned = 8, weight = 100f),
        )
        val oneRepMax = (100f * (1 + 4 / 30.0))
        repository.populateProgramProgress(programProgressList)
        val graphDataOneRepMax = getGraphData(GraphDataFormula.OneRepMax).first()["exercise 1"]!!.first().value!!
        assertThat(graphDataOneRepMax).isEqualTo(oneRepMax)
    }
}