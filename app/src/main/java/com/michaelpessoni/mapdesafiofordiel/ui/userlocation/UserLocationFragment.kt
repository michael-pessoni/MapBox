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
import com.michaelpessoni.mapdesafiofordiel.databinding.UserLocationFragmentBinding
import com.michaelpessoni.mapdesafiofordiel.util.LocationPermissionHelper
import java.lang.ref.WeakReference


class UserLocationFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var binding : UserLocationFragmentBinding
    private lateinit var viewModel: UserLocationViewModel
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

        mapView = requireView().findViewById(R.id.mapView)

        viewModel = UserLocationViewModel()

        viewModel.mapView = mapView

        //Verifies the location permission
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this.activity))
        locationPermissionHelper.checkPermissions {
            viewModel.onMapReady(this.requireContext())
        }

        viewModel.cameraTrackDismissed.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.context, "Camera Tracking Dismissed", Toast.LENGTH_SHORT).show()
        })

        val goToAddPinButton = requireView().findViewById<CardView>(R.id.add_pins_button)
        goToAddPinButton.setOnClickListener{
            findNavController().navigate(R.id.action_userLocationFragment_to_addPinFragment2)
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