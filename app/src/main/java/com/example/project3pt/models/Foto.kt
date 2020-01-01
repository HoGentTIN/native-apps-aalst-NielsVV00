package com.example.project3pt.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream
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

    fun getImageLowQ(): Bitmap{
        val baos = ByteArrayOutputStream()
        getImage().compress(Bitmap.CompressFormat.JPEG, 10, baos)
        var imageBytes: ByteArray = baos.toByteArray()
        return stringToBitmap(android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP))
    }

    //decode base64 string to image
    fun stringToBitmap(string: String): Bitmap{
        val imageBytes = android.util.Base64.decode(string, android.util.Base64.NO_WRAP)
        System.out.println(imageBytes)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}
