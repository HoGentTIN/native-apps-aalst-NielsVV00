package com.example.project3pt.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


fun getShortDateString(date: Date): String{
    Log.i("date", date.toString())
    var df = SimpleDateFormat("dd/MM/yyyy")
    return df.format(date)
}
