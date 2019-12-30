package com.example.project3pt.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class Foto(
    val id: Long,
    val fotoData: String
){
    fun getImage(): Bitmap {
        val imageBytes = Base64.getDecoder().decode(fotoData)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}