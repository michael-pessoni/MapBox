package com.michaelpessoni.mapdesafiofordiel

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.mapbox.maps.MapView
import com.michaelpessoni.mapdesafiofordiel.databinding.ActivityUserLocationBinding
import com.michaelpessoni.mapdesafiofordiel.util.EventObserver
import com.michaelpessoni.mapdesafiofordiel.util.LocationPermissionHelper
import java.lang.ref.WeakReference


class UserLocationActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var binding : ActivityUserLocationBinding
    private lateinit var viewModel: UserLocationViewModel
    private lateinit var locationPermissionHelper: LocationPermissionHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_location)
        mapView = binding.mapView

        viewModel = UserLocationViewModel()

        viewModel.mapView = mapView

        //Verifies the location permission
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
           viewModel.onMapReady(this)
        }

        viewModel.cameraTrackDismissed.observe(this, Observer {
            Toast.makeText(this, "Camera Tracking Dismissed", Toast.LENGTH_SHORT).show()
        })

        setupNavigation()

    }

    private fun setupNavigation() {
        viewModel.addNewPinEvent.observe(this, EventObserver{
            TODO()
        })

        viewModel.showPinsEvent.observe(this, EventObserver{
            TODO()
        })
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