package com.example.project3pt.database

import androidx.room.*
import com.example.project3pt.models.Wedstrijd

@Dao

interface WedstrijdDao {

    @Query("SELECT * FROM wedstrijd_table")
    suspend fun getAll(): List<Wedstrijd>

    @Query("DELETE FROM wedstrijd_table")
    suspend fun nukeTable()

    @Query("SELECT * FROM wedstrijd_table WHERE Id = :id")
    suspend fun get(id: Long): Wedstrijd

    @Insert
    suspend fun insert(wedstrijd: Wedstrijd)
}
