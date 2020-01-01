package com.example.project3pt.repositories

import com.example.project3pt.models.Foto

interface IFotoRepository {
    suspend fun getRandom(): Foto?
    suspend fun getAll(): List<Foto>?
    suspend fun post(foto: Foto): Foto?
}
