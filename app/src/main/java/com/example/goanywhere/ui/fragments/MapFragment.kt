package com.example.goanywhere.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.goanywhere.R
import com.example.goanywhere.databinding.FragmentMapBinding
import com.example.goanywhere.models.Coordinates
import com.example.goanywhere.shareable.coordinatesData
import com.example.goanywhere.ui.views.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMapBinding? = null
    var coordinates: Coordinates = Coordinates(0.0, 0.0)

    private fun initMap(lat: Double, lng: Double, vehicle: String){
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment!!.getMapAsync { googleMap ->
            val coordinates = LatLng(lat, lng)
            val markerOptions = MarkerOptions()
            markerOptions.position(coordinates)
            markerOptions.title(vehicle)
            googleMap.clear()
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 20f))
            googleMap.addMarker(markerOptions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        _binding!!.vehicleName.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Hide keyboard when pressing enter
                    val imm: InputMethodManager =
                        context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(_binding!!.vehicleName.windowToken, 0)
                    var textCurrent = _binding!!.vehicleName.text.toString().trim();
                    val fullVehicleName = textCurrent
                    if (textCurrent.contains("Autobuzul ")){
                        textCurrent = textCurrent.replace("Autobuzul ", "bus_")
                    }
                    if (textCurrent.contains("Tramvaiul ")){
                        textCurrent = textCurrent.replace("Tramvaiul ", "tram_")
                    }
                    if (textCurrent.isNotEmpty()){
                        lifecycleScope.launchWhenCreated {
                            coordinatesData.location = textCurrent
                            coordinates = viewModel.makeProvideLastStatusApiCall()
                            initMap(coordinates.latitude, coordinates.longitude, fullVehicleName)
                        }
                    }
                    return true
                }
                return false
            }
        })
        return _binding!!.root
    }
}
