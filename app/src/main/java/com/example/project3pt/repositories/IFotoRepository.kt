package com.example.project3pt.repositories

import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Foto
import com.example.project3pt.models.Wedstrijd

interface IFotoRepository {
    suspend fun getRandom(): Foto?
    suspend fun getAll(): List<Foto>?
    suspend fun post(data: String): Foto?
}