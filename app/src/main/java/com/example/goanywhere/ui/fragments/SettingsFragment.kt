package com.example.goanywhere.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.goanywhere.R
import com.example.goanywhere.ui.views.MainViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val viewModel: MainViewModel by viewModels()
}