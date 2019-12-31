package com.example.project3pt.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.project3pt.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentID) as NavHostFragment

        val navController = this.findNavController(R.id.fragmentID)

        NavigationUI.setupWithNavController(bottom_nav_view, navController)
    }
}
