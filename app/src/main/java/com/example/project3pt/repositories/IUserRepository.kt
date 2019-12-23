package com.example.project3pt.repositories

interface IUserRepository {

    /**
     * Save the user token in SharedPreferences
     */
    fun saveUserToken(userToken: String?)

    /**
     * Remove the user token from SharedPreferences
     */
    fun removeUserToken()

    /**
     * @return the user token or null
     */
    fun getUserToken() : String?

    /**
     * @return true if the user is logged in
     */
    suspend fun isUserLoggedIn() : Boolean

    fun saveUserNaam(userNaam: String?)

    fun removeUserNaam()

    fun getUserNaam() : String

    suspend fun logout()

    // LOGIN
    suspend fun login(email: String, password: String): Boolean


    // REGISTER
    suspend fun register(email: String, password: String, firstName: String, lastName: String): Boolean

}