package com.example.goanywhere.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goanywhere.R
import com.example.goanywhere.adapters.RoutesAdapter
import com.example.goanywhere.databinding.FragmentRoutesBinding
import com.example.goanywhere.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutesFragment : Fragment(R.layout.fragment_routes){
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentRoutesBinding? = null
    private lateinit var routesAdapter: RoutesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        lifecycleScope.launchWhenCreated {
            _binding!!.progressBar.isVisible = true
             routesAdapter.routes = viewModel.makeGetRouteApiCall()
            _binding!!.progressBar.isVisible = false
        }
        return _binding!!.root
    }

    private fun setupRecyclerView() = _binding!!.rvRoutes.apply {
        routesAdapter = RoutesAdapter()
        adapter =  routesAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}
