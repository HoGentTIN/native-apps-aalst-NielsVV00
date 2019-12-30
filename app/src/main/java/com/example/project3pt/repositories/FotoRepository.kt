package com.example.project3pt.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.project3pt.App
import com.example.project3pt.database.Database3PT
import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Foto
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.services.FotoService
import com.example.project3pt.services.WedstrijdService
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject


class FotoRepository(private val context: Context): IFotoRepository {

    @Inject
    lateinit var fotoService: FotoService

    init {
        App.appComponent.inject(this)
    }

    override suspend fun getRandom(): Foto? {
        try{
            return fotoService.getRandom()
        }catch (e: Exception){
            Log.i("fotoRep.getRandom", e.message)
        }
        return null
    }

    override suspend fun getAll(): List<Foto>? {
        try{
            return fotoService.getAll()
        }catch (e: Exception){
            Log.i("fotoRep.getAll", e.message)
        }
        return null
    }

    override suspend fun post(data: String): Foto? {
        try{
            return fotoService.post(data)
        }catch(e: Exception){
            Log.i("fotoPost", e.message)
        }
        return null
    }


}