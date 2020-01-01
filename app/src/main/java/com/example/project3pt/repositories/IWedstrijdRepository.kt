package com.example.project3pt.repositories

import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Wedstrijd

interface IWedstrijdRepository {
    suspend fun getAll(): List<Wedstrijd>
    suspend fun getWedstrijd(id: Long): Wedstrijd
    suspend fun getDeelnemersFromWedstrijd(id: Long): List<Deelnemer>?
    suspend fun neemDeel(id: Long): Boolean
    suspend fun postWedstrijd(wedstrijd: Wedstrijd): Boolean
    suspend fun refreshDeelnemers(id: Long): List<Deelnemer>?
    suspend fun getMijnWedstrijden(): List<Wedstrijd>?
    fun getMijnWedstrijdenFromShared(): List<Wedstrijd>?
    fun saveMijnWedstrijden(wedstrijden: List<Wedstrijd>)
    suspend fun addOneToMijnWedstrijden(wedstrijd: Wedstrijd)
    suspend fun removeOneFromMijnWedstrijden(wedstrijd: Wedstrijd)
    fun logout()
}
