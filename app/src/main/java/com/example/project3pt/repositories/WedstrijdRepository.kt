package com.example.project3pt.repositories

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import com.example.project3pt.database.WedstrijdDao
import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.MijnWedstrijden
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.services.WedstrijdService
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class WedstrijdRepository(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
    private val connectivityManager: ConnectivityManager,
    private val wedstrijdDao: WedstrijdDao,
    private val wedstrijdService: WedstrijdService
) : IWedstrijdRepository {

    override suspend fun getAll(): List<Wedstrijd> {
        if(isNetworkAvailable()){
            try {
                val wedstrijden = wedstrijdService.getWedstrijden()
                Log.i("wedstrijden", wedstrijden.toString())
                saveWedstrijdenToDb(wedstrijden)
                return wedstrijden
            } catch (e: Exception) {
                Log.e("loadWedstrijden", "Failed to load: " + e.message)
            }
        }
        else if(wedstrijdDao.getAll().isNotEmpty()){
            return wedstrijdDao.getAll()
        }
        return ArrayList()
    }

    private suspend fun saveWedstrijdenToDb(wedstrijden: List<Wedstrijd>) {
        wedstrijdDao.nukeTable()
        wedstrijden.forEach {
            wedstrijdDao.insert(it)
        }
    }

    override suspend fun getWedstrijd(id: Long): Wedstrijd {
        return wedstrijdDao.get(id)
    }

    override suspend fun refreshDeelnemers(id: Long): List<Deelnemer>? {
        if (isNetworkAvailable()) {
            return getDeelnemersFromWedstrijd(id)
        }
        return ArrayList()
    }

    override suspend fun getMijnWedstrijden(): List<Wedstrijd>? {
        val mijnWedstrijden = getMijnWedstrijdenFromShared()
        if (!mijnWedstrijden.isNullOrEmpty()) {
            return mijnWedstrijden
        } else {
            val mijnWedstrijdenFromApi = userRepository.getMijnWedstrijden()
            saveMijnWedstrijden(mijnWedstrijdenFromApi)
            return mijnWedstrijdenFromApi
        }
    }

    override fun getMijnWedstrijdenFromShared(): List<Wedstrijd>? {
        val mijnWedstrijdenString = sharedPreferences.getString("mijnWedstrijden", null)
        Log.i("mijnWedstrijden", mijnWedstrijdenString)
        if(mijnWedstrijdenString.isNullOrEmpty()){
            return null
        }else{
            return Gson().fromJson(mijnWedstrijdenString, MijnWedstrijden::class.java).wedstrijden
        }
    }

    override fun saveMijnWedstrijden(wedstrijden: List<Wedstrijd>) {
        val mijnWedstrijden = MijnWedstrijden(wedstrijden)
        sharedPreferences.edit().putString("mijnWedstrijden", Gson().toJson(mijnWedstrijden)).apply()
    }

    override suspend fun addOneToMijnWedstrijden(wedstrijd: Wedstrijd) {
        var mijn: MutableList<Wedstrijd>? = getMijnWedstrijden()?.toMutableList()
        if (mijn.isNullOrEmpty()) {
            mijn = MutableList(1) { wedstrijd }
        } else {
            mijn.add(wedstrijd)
        }
        saveMijnWedstrijden(mijn)
    }

    override suspend fun removeOneFromMijnWedstrijden(wedstrijd: Wedstrijd) {
        var mijn: MutableList<Wedstrijd>? = getMijnWedstrijden()?.toMutableList()
        if (mijn.isNullOrEmpty()) {
            mijn = MutableList(0){wedstrijd}
        } else {
            mijn.remove(wedstrijd)
        }
        saveMijnWedstrijden(mijn)
    }

    override fun logout() {
        saveMijnWedstrijden(listOf())
        userRepository.logout()
    }

    override suspend fun getDeelnemersFromWedstrijd(id: Long): List<Deelnemer>? {
        var deelnemers: List<Deelnemer>? = null
        try {
            deelnemers = wedstrijdService.getDeelnemers(id).sortedWith(compareBy({ it.voornaam.toLowerCase() }, { it.achternaam.toLowerCase() }))
        } catch (e: Exception) {
            Log.e("loadDeelnemers", "Failed to load: " + e.message)
        }
        return deelnemers
    }

    override suspend fun postWedstrijd(wedstrijd: Wedstrijd): Boolean {

        val json = JSONObject()
        json.put("datum", wedstrijd.datum)
        json.put("soort", wedstrijd.soort)
        json.put("plaats", wedstrijd.plaats)

        var jsonWedstrijd: RequestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
        var bool = false

        try {
            wedstrijdService.postWedstrijd(jsonWedstrijd)
            bool = true
        } catch (e: Exception) {
            Log.e("postWedstrijd", "failed to post: " + e.message)
        }
        return bool
    }

    override suspend fun neemDeel(id: Long): Boolean {
        var bool = false
        try {
            Log.i("id", id.toString())
            var response = wedstrijdService.neemDeel(id)
            Log.i("antwoord neemDeel", response.string())
            bool = true
        } catch (e: Exception) {
            Log.e("neemDeel", "Failed because: " + e.message)
        }
        return bool
    }

    private fun isNetworkAvailable(): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
