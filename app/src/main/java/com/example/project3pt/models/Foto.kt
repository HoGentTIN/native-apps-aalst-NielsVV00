package com.example.project3pt.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*

class Foto(
    @ColumnInfo(name = "fotoData")
    val fotoData: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 1L

) {
    fun getImage(): Bitmap {
        val imageBytes = Base64.getDecoder().decode(fotoData)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}
