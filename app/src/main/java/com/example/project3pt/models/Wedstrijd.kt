package com.example.project3pt.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project3pt.utils.getShortDateString
import java.util.*

@Entity(tableName = "wedstrijd_table")
data class Wedstrijd(
    @ColumnInfo(name = "datum")
    val datum: Date,
    @ColumnInfo(name = "soort")
    val soort: String = "",
    @ColumnInfo(name = "plaats")
    val plaats: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1L
) {
    fun getDateString(): String {
        return getShortDateString(datum)
    }
}
