package com.example.project3pt.repositories

import android.content.Context
import android.util.Log
import com.example.project3pt.App
import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.services.WedstrijdService
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class WedstrijdRepository(private val context: Context): IWedstrijdRepository {

    @Inject
    lateinit var wedstrijdService: WedstrijdService

    init {
        App.appComponent.inject(this)
    }

    override suspend fun getWedstrijdenFromApi(): List<Wedstrijd> {
        lateinit var wedstrijden: List<Wedstrijd>
        try {
            wedstrijden = wedstrijdService.getWedstrijden()
            Log.i("wedstrijden", wedstrijden.toString())
            return wedstrijden
        }catch (e: Exception){
            Log.e("loadWedstrijden", "Failed to load: " + e.message)
        }
        return ArrayList<Wedstrijd>()
    }

    override suspend fun getWedstrijdFromApi(id: Long): Wedstrijd {
        lateinit var wedstrijd: Wedstrijd
        Log.i("wedstrijdid", id.toString())
        try {
            wedstrijd = wedstrijdService.getWedstrijd(id)
            Log.i("wedstrijd", wedstrijd.toString())

        }catch (e: Exception){
            Log.e("LoadWedstrijd", "Failed to load: " + e.message)
        }
        return wedstrijd
    }

    override suspend fun getDeelnemersFromWedstrijd(id: Long): List<Deelnemer> {
        lateinit var deelnemers: List<Deelnemer>
        try {
            deelnemers = wedstrijdService.getDeelnemers(id).sortedWith(compareBy({it.voornaam.toLowerCase()}, {it.achternaam.toLowerCase()}))
        }catch (e: Exception){
            Log.e("loadDeelnemers", "Failed to load: " + e.message)
        }
        return deelnemers
    }

    override suspend fun postWedstrijd(wedstrijd: Wedstrijd): Boolean{

        val json = JSONObject()
        json.put("datum", wedstrijd.datum)
        json.put("soort", wedstrijd.soort)
        json.put("plaats", wedstrijd.plaats)

        var jsonWedstrijd: RequestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
        var bool = false

        try{
            wedstrijdService.postWedstrijd(jsonWedstrijd)
            bool = true
        }catch(e: Exception){
            Log.e("postWedstrijd", "failed to post: " + e.message)
        }
        return bool
    }

    override suspend fun neemDeel(id: Long): Boolean {
        var bool = false
        try{
            Log.i("id", id.toString())
            var response = wedstrijdService.neemDeel(id)
            Log.i("antwoord neemDeel", response.string())
            bool = true
        }catch (e: Exception){
            Log.e("neemDeel", "Failed because: " + e.message)
        }
        return bool
    }
}