package com.example.project3pt.utils

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toLong(value: Date): Long {
        return value.time
    }
}