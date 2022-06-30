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
import com.example.goanywhere.adapters.TripAdapter
import com.example.goanywhere.databinding.FragmentTripBinding
import com.example.goanywhere.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripFragment : Fragment(R.layout.fragment_trip) {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var tripAdapter: TripAdapter
    private var _binding: FragmentTripBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripBinding.inflate(inflater, container, false)
        lifecycleScope.launchWhenCreated {
            setupRecyclerView()
            _binding!!.progressBarTrip.isVisible = true
            viewModel.makeInfoApiCall()
            _binding!!.progressBarTrip.isVisible = false
        }
        tripAdapter.setData(viewModel.getTrip())
        return _binding!!.root
    }

    private fun setupRecyclerView() = _binding!!.rvTrip.apply {
        tripAdapter = TripAdapter()
        adapter =  tripAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}
