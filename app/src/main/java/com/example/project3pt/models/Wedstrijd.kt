package com.example.project3pt.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project3pt.utils.getShortDateString
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "wedstrijd_table")
data class Wedstrijd(
    val datum: Date,
    val soort : String,
    val plaats : String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1L
){
    fun getDateString(): String{
        return getShortDateString(datum)
    }
}
