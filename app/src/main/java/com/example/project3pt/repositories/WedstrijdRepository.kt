package com.example.project3pt.repositories

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.project3pt.App
import com.example.project3pt.database.Database3PT
import com.example.project3pt.models.Deelnemer
import com.example.project3pt.models.MijnWedstrijden
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.services.WedstrijdService
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject


class WedstrijdRepository(private val context: Context): IWedstrijdRepository {


    @Inject
    lateinit var userRepository: IUserRepository

    @Inject
    lateinit var wedstrijdService: WedstrijdService

    private val database = Database3PT.getInstance(context)
    private val wedstrijdDao = database.wedstrijdDao
    private var sharedPreferences : SharedPreferences = context.getSharedPreferences("Preferences", 0)

    init {
        App.appComponent.inject(this)
    }

    override suspend fun refreshWedstrijden(): List<Wedstrijd>{
        Log.i("net", isNetworkAvailable().toString())
        if(isNetworkAvailable()){
            wedstrijdDao.nukeTable()
            getWedstrijdenFromApi().forEach {
                wedstrijdDao.insert(it)
            }
        }
        return wedstrijdDao.getAll()
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
        return ArrayList()
    }

    override suspend fun getWedstrijd(id: Long): Wedstrijd {
        return wedstrijdDao.get(id)
    }

    override suspend fun refreshDeelnemers(id: Long): List<Deelnemer>?{
        if(isNetworkAvailable()){
            return getDeelnemersFromWedstrijd(id)
        }
        return ArrayList()
    }

    override suspend fun getMijnWedstrijden(): List<Wedstrijd>? {
        val mijnWedstrijden = getMijnWedstrijdenFromShared()
        if(!mijnWedstrijden.isNullOrEmpty()){
            return mijnWedstrijden
        }else{
            val mijnWedstrijdenFromApi =  userRepository.getMijnWedstrijden()
            saveMijnWedstrijden(mijnWedstrijdenFromApi)
            return mijnWedstrijdenFromApi
        }
    }

    override fun getMijnWedstrijdenFromShared(): List<Wedstrijd>?{
        val mijnWedstrijdenString = sharedPreferences.getString("mijnWedstrijden", null)
        return Gson().fromJson(mijnWedstrijdenString, MijnWedstrijden::class.java).wedstrijden
    }

    override fun saveMijnWedstrijden(wedstrijden: List<Wedstrijd>) {
        val mijnWedstrijden = MijnWedstrijden(wedstrijden)
        sharedPreferences.edit().putString("mijnWedstrijden", Gson().toJson(mijnWedstrijden)).apply()
    }

    override suspend fun addOneToMijnWedstrijden(wedstrijd: Wedstrijd) {
        var mijn: MutableList<Wedstrijd>? = getMijnWedstrijden()?.toMutableList()
        if(mijn.isNullOrEmpty()){
            mijn = MutableList(1){wedstrijd}
        }else{
            mijn.add(wedstrijd)
        }
        saveMijnWedstrijden(mijn)
    }

    override fun logout() {
        saveMijnWedstrijden(listOf())
        userRepository.logout()
    }

    override suspend fun getDeelnemersFromWedstrijd(id: Long): List<Deelnemer>? {
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

    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}