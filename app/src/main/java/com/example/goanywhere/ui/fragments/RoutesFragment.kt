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
import com.example.goanywhere.RetrofitInstance
import com.example.goanywhere.adapters.RoutesAdapter
import com.example.goanywhere.databinding.FragmentRoutesBinding
import com.example.goanywhere.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class RoutesFragment : Fragment(R.layout.fragment_routes) {
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
        // make api request

        lifecycleScope.launchWhenCreated {
            _binding!!.progressBar.isVisible = true
            val response = try {
                // test api call
                RetrofitInstance.api.getRoutes("47.1665182", "27.5579386", "47.1631825", "27.5645637")
            } catch (e : IOException){
                Timber.v("IOException: No internet connection!")
                Timber.v("$e")
                _binding!!.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Timber.v("HttpException: Unexpected response!")
                _binding!!.progressBar.isVisible = false
                return@launchWhenCreated
            }
            if(response.isSuccessful && response.body() != null){
                routesAdapter.routes = response.body()!!
            } else {
                Timber.v("Err: Response  not successfully!")
            }
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
