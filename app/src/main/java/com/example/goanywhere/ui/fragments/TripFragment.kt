package com.example.goanywhere.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.goanywhere.R
import com.example.goanywhere.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripFragment : Fragment(R.layout.fragment_trip) {
    private val viewModel: MainViewModel by viewModels()
}