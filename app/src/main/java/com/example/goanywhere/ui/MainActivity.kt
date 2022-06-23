package com.example.goanywhere.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.goanywhere.R
import com.example.goanywhere.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id) {
                    R.id.directionsFragment, R.id.routesFragment, R.id.mapFragment ->
                        bottomNavigationView.visibility = View.VISIBLE
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
    }
}
