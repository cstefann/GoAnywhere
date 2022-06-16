package com.example.goanywhere.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.goanywhere.R
import com.example.goanywhere.databinding.FragmentDirectionsBinding
import com.example.goanywhere.ui.views.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import timber.log.Timber

@AndroidEntryPoint
class DirectionsFragment : Fragment(R.layout.fragment_directions){
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentDirectionsBinding? = null
    var deniedPermissionsCount: Int = 0

    // adapted solution from here for permissions: https://stackoverflow.com/questions/67747784/how-to-manage-the-permission-request-code-using-navigation-component
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Timber.v("Permission already granted!")
            } else {
                deniedPermissionsCount += 1
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDirectionsBinding.inflate(inflater, container, false)

        // checking for permissions
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermission.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        Timber.v("Number of denied permissions $deniedPermissionsCount")

        if ((deniedPermissionsCount > 1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) ||
            (deniedPermissionsCount > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            _binding!!.floatingActionButton2.setOnClickListener {
                findNavController().navigate(R.id.action_directionsFragment_to_routesFragment)
            }
        }

        return _binding!!.root
    }
}
