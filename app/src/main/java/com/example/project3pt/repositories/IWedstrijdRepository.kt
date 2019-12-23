package com.example.project3pt.repositories

import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Wedstrijd

interface IWedstrijdRepository {
    suspend fun getWedstrijdenFromApi(): List<Wedstrijd>
    suspend fun getWedstrijdFromApi(id: Long): Wedstrijd
    suspend fun getDeelnemersFromWedstrijd(id: Long): List<Deelnemer>
    suspend fun neemDeel(id: Long): Boolean
    suspend fun postWedstrijd(wedstrijd: Wedstrijd): Boolean
}