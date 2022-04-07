package com.michaelpessoni.mapdesafiofordiel.ui.userlocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mapbox.maps.MapView
import com.michaelpessoni.mapdesafiofordiel.R
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDatabase
import com.michaelpessoni.mapdesafiofordiel.databinding.OptionsBottomSheetBinding
import com.michaelpessoni.mapdesafiofordiel.databinding.UserLocationFragmentBinding
import com.michaelpessoni.mapdesafiofordiel.ui.MapViewModel
import com.michaelpessoni.mapdesafiofordiel.util.LocationPermissionHelper
import java.lang.ref.WeakReference


class UserLocationFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var binding : UserLocationFragmentBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.user_location_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSource = PinsDatabase.getInstance(this.requireActivity().application).pinsDatabaseDAO
        mapView = requireView().findViewById(R.id.mapView)
        viewModel = MapViewModel(dataSource, mapView)

        checkLocationPermission()

        onCameraTrackingDismiss()

        setOnClickListeners()
    }

    private fun onCameraTrackingDismiss() {
        viewModel.cameraTrackDismissed.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context, "Camera Tracking Dismissed", Toast.LENGTH_SHORT).show()
        })
    }

    private fun checkLocationPermission() {
        //Verifies the location permission
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this.activity))
        locationPermissionHelper.checkPermissions {
            viewModel.onMapReady()
            viewModel.initLocationPuck(this.requireContext())
        }
    }

    private fun setOnClickListeners() {
        val goToAddPinButton = requireView().findViewById<CardView>(R.id.add_pins_button)
        goToAddPinButton.setOnClickListener {
            findNavController().navigate(R.id.action_userLocationFragment_to_addPinFragment)
        }

        val showPinsButton = requireView().findViewById<CardView>(R.id.show_pins_button)
        showPinsButton.setOnClickListener {
            findNavController().navigate(R.id.action_userLocationFragment_to_showPinsFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}