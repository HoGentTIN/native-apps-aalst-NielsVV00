package com.example.project3pt.repositories

import android.util.Log
import com.example.project3pt.models.Foto
import com.example.project3pt.services.FotoService
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class FotoRepository(
    private val fotoService: FotoService
) : IFotoRepository {

    override suspend fun getRandom(): Foto? {
        try {
            return fotoService.getRandom()
        } catch (e: Exception) {
            Log.i("fotoRep.getRandom", e.message)
        }
        return null
    }

    override suspend fun getAll(): List<Foto>? {
        try {
            return fotoService.getAll()
        } catch (e: Exception) {
            Log.i("fotoRep.getAll", e.message)
        }
        return null
    }

    override suspend fun post(foto: Foto): Foto? {
        try {
            return fotoService.post(foto)
        } catch (e: Exception) {
            Log.i("fotoPost", e.message)
        }
        return null
    }
}
