package com.example.project3pt.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deelnemer_table")
data class Deelnemer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1L,
    val voornaam: String,
    val achternaam: String,
    val email: String
)
