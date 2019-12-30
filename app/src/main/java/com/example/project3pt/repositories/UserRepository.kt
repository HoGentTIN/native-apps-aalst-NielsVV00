package com.example.project3pt.repositories

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.project3pt.App
import com.example.project3pt.models.Wedstrijd
import com.example.project3pt.services.UserService
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.lang.Exception

import javax.inject.Inject


class UserRepository(context: Context) :
    IUserRepository {

    private var userToken: String?
    private var userNaam: String?
    private var sharedPreferences : SharedPreferences

    @Inject
    lateinit var userService: UserService

    init{
        // Dagger injection
        App.appComponent.inject(this)
        //initialize sharedPReferences
        sharedPreferences = context.getSharedPreferences("Preferences", 0)
        //Initialize user token
        this.userToken = (sharedPreferences.getString("userToken", null))
        this.userNaam = (sharedPreferences.getString("userNaam", null))
    }

    override fun saveUserNaam(userNaam: String?) {
        this.userNaam = userNaam
        sharedPreferences.edit().putString("userNaam", userNaam).apply()
    }

    override fun getUserNaam(): String {
        return userNaam!!
    }
    override fun removeUserNaam() {
        this.userNaam = null
        sharedPreferences.edit().putString("userNaam", null).apply()
    }

    /**
     * Save the user token to SharedPreferences by 'updating' the string with the key 'userToken'
     */
    override fun saveUserToken(userToken: String?) {
        this.userToken = userToken
        sharedPreferences.edit().putString("userToken", userToken).apply()
    }

    /**
     * Remove the user token from SharedPreferences by 'updating' the string with the key 'userToken' and adding value 'null'
     */
    override fun removeUserToken() {
        this.userToken = null
        sharedPreferences.edit().putString("userToken", null).apply()
    }

    /**
     * @return the user token, or null
     */
    override fun getUserToken(): String? {
        return userToken
    }

    override suspend fun getMijnWedstrijden(): List<Wedstrijd>{
        try{
            return userService.getWedstrijden()
        }catch (e: Exception){
            Log.i("GetMijnWedstrijden()", e.message)
        }
        return listOf()
    }

    /**
     * Check if the user is logged in by checking if the token is not null
     * @return true if usertoken != null
     */
    override suspend fun isUserLoggedIn(): Boolean {
        if(getUserToken() != null){
            Log.i("token logged in", getUserToken())
            return true
        }
     return false
    }

    /**
     * log de gebruiker uit
     */
    override fun logout() {
        saveUserNaam(null)
        saveUserToken(null)
    }

    /** LOGIN USER **/

    override suspend fun login(email: String, password: String): Boolean {
        // Bouw het JSON object om mee te geven met de request
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        var thisLogin: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), json.toString())
        try{
            var response = userService.login(thisLogin)
            loadUserData(response.voornaam + " " + response.achternaam, response.token)
            return true
        }catch (e: Exception){
            Log.e("ErrorLogin", e.message)
            return false
        }
    }


    // Register User
    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Boolean {

        // Maak het JSON object aan
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)
        json.put("firstName", firstName)
        json.put("lastName", lastName)

        var thisRegister: RequestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
        Log.i("bodyReg", thisRegister.toString())
        try{
            var response = userService.register(thisRegister)
            loadUserData(response.voornaam + " " + response.achternaam, response.token)
            return true
        }catch (e: Exception){
            Log.e("ErrorRegister", e.message)
            return false
        }
    }

    suspend private fun loadUserData(naam: String, token: String){
        saveUserNaam(naam)
        saveUserToken(token)
    }
}