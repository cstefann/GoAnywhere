package com.example.goanywhere.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.goanywhere.R
import com.example.goanywhere.databinding.FragmentRoutesBinding
import com.example.goanywhere.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutesFragment : Fragment(R.layout.fragment_routes) {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentRoutesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        _binding!!.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_routesFragment_to_tripFragment )
        }
        return _binding!!.root
    }
}
