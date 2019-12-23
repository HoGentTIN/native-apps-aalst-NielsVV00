package com.example.project3pt.models

data class AuthResponse(
    val id: String = "",
    val email: String = "",
    val voornaam: String = "",
    val achternaam: String = "",
    val roles : Array<String>,
    val token: String = ""
)