package com.example.goanywhere.ui.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.goanywhere.R
import com.example.goanywhere.databinding.FragmentDirectionsBinding
import com.example.goanywhere.models.Coordinates
import com.example.goanywhere.shareable.coordinatesData
import com.example.goanywhere.shareable.getRouteReqData
import com.example.goanywhere.ui.views.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DirectionsFragment : Fragment(R.layout.fragment_directions) {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentDirectionsBinding? = null
    var currentLoc: Coordinates = Coordinates(0.0, 0.0)
    var destLoc: Coordinates = Coordinates(0.0, 0.0)
    private var fusedLocationProvider: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDirectionsBinding.inflate(inflater, container, false)
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermission()

        // Default cause - we retrieve current location and if radio is checked
        // the values will be overwritten
        fusedLocationProvider?.lastLocation
            ?.addOnSuccessListener { location : Location? ->
                if (location != null) {
                    currentLoc.latitude = location.latitude
                    currentLoc.longitude = location.longitude
                }
            }

        _binding!!.useCurrentLocationButton.setOnClickListener{
            _binding!!.currentLocation.isEnabled = false
            Toast.makeText(context, "Using current location", Toast.LENGTH_LONG).show()
        }

        // For current location input box, in case that "use current location" radio
        // is not checked
        if (_binding!!.currentLocation.isEnabled)
        {
            _binding!!.currentLocation.setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                    // If the event is a key-down event on the "enter" button
                    if (event.action == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER
                    ) {
                        // Hide keyboard when pressing enter
                        val imm: InputMethodManager =
                            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(_binding!!.currentLocation.windowToken, 0)
                        val textCurrent = _binding!!.currentLocation.text.toString().trim();
                        if (textCurrent.isNotEmpty()){
                            lifecycleScope.launchWhenCreated {
                                coordinatesData.location = textCurrent
                                viewModel.makeProvideCoordsApiCall()}
                        }
                        return true
                    }
                    return false
                }
            })
        }
        else{
            Toast.makeText(requireContext(), "Current location button is checked", Toast.LENGTH_LONG).show()
        }

        // For destination location input box
        _binding!!.destinationLocation.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Hide keyboard when pressing enter
                    val imm: InputMethodManager =
                        context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(_binding!!.destinationLocation.windowToken, 0)
                    val textDest: String = _binding!!.destinationLocation.text.toString().trim();
                    if (textDest.isNotEmpty()){
                        lifecycleScope.launchWhenCreated {
                            coordinatesData.location = textDest
                            viewModel.makeProvideCoordsApiCall()}
                    }
                    return true
                }
                return false
            }
        })

        _binding!!.button.setOnClickListener {
            if (!_binding!!.currentLocation.isEnabled){
                destLoc.latitude = coordinatesData.latitude[0]
                destLoc.longitude = coordinatesData.longitude[0]
            }
            else{
                currentLoc.latitude = coordinatesData.latitude[0]
                currentLoc.longitude = coordinatesData.longitude[0]
                destLoc.latitude = coordinatesData.latitude[1]
                destLoc.longitude = coordinatesData.longitude[1]
            }
            getRouteReqData.latitudeStart = currentLoc.latitude
            getRouteReqData.longitudeStart = currentLoc.longitude
            getRouteReqData.latitudeEnd = destLoc.latitude
            getRouteReqData.longitudeEnd = destLoc.longitude
            findNavController().navigate(R.id.action_directionsFragment_to_routesFragment)
        }
        return _binding!!.root
    }

    // Location permissions request is from stack overflow but it's adapted for my need
    // https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            checkBackgroundLocation()
        }
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Now check background location - for android above Q
                        checkBackgroundLocation()
                    }

                } else {

                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", requireActivity().packageName, null),
                            ),
                        )
                    }
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(
                            requireContext(),
                            "Granted Background Location Permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return

            }
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

}