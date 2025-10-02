package com.velosiped.utility.datehelper

import com.velosiped.utility.extensions.ONE
import java.util.Calendar

object DateHelper {
    fun getCurrentDay(): Int {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getCurrentMonth(): Int {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + Int.ONE
    }

    fun getCurrentYear(): Int {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }
}