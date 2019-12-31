package com.example.project3pt.repositories

import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Wedstrijd

interface IWedstrijdRepository {
    suspend fun getWedstrijdenFromApi(): List<Wedstrijd>
    suspend fun getWedstrijd(id: Long): Wedstrijd
    suspend fun getDeelnemersFromWedstrijd(id: Long): List<Deelnemer>?
    suspend fun neemDeel(id: Long): Boolean
    suspend fun postWedstrijd(wedstrijd: Wedstrijd): Boolean
    suspend fun refreshWedstrijden(): List<Wedstrijd>?
    suspend fun refreshDeelnemers(id: Long): List<Deelnemer>?
    suspend fun getMijnWedstrijden(): List<Wedstrijd>?
    fun getMijnWedstrijdenFromShared(): List<Wedstrijd>?
    fun saveMijnWedstrijden(wedstrijden: List<Wedstrijd>)
    suspend fun addOneToMijnWedstrijden(wedstrijd: Wedstrijd)
    fun logout()
}
