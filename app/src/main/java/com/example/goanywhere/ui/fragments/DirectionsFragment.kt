package com.example.goanywhere.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.goanywhere.R
import com.example.goanywhere.databinding.FragmentDirectionsBinding
import com.example.goanywhere.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DirectionsFragment : Fragment(R.layout.fragment_directions) {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentDirectionsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDirectionsBinding.inflate(inflater, container, false)
        _binding!!.floatingActionButton2.setOnClickListener{
            findNavController().navigate(R.id.action_directionsFragment_to_routesFragment)
        }
        return _binding!!.root
    }
}