package me.tankery.justnote.data.db;

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun calendar2Timestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun timestamp2Calendar(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }
}
