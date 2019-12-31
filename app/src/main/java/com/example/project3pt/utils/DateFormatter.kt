package com.example.project3pt.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getShortDateString(date: Date): String {
    Log.i("date", date.toString())
    val df = SimpleDateFormat("dd/MM/yyyy")
    return df.format(date)
}
