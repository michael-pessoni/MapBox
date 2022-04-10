package com.michaelpessoni.mapdesafiofordiel.ui.addpin

import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.michaelpessoni.mapdesafiofordiel.R
import com.michaelpessoni.mapdesafiofordiel.data.Pin
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDatabase
import com.michaelpessoni.mapdesafiofordiel.databinding.AddPinFragmentBinding
import com.michaelpessoni.mapdesafiofordiel.ui.MapViewModel

class AddPinFragment : Fragment(), OnCameraChangeListener {


    private lateinit var binding: AddPinFragmentBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var currentLocation: com.mapbox.geojson.Point

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_pin_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSource = PinsDatabase.getInstance(this.requireContext()).pinsDatabaseDAO
        mapView = requireView().findViewById(R.id.mapView)
        viewModel = MapViewModel(dataSource, mapView)

        viewModel.onMapReady()

        viewModel.addPinToMap(this@AddPinFragment.requireContext())

        currentLocation = mapView.getMapboxMap().cameraState.center

        setCoordinatesText()

        setOnClickListener()

        setOnCameraChangeListener()

    }

    private fun setOnCameraChangeListener() {
        val onCameraChangeListener = OnCameraChangeListener {
            onCameraChanged(it)
        }

        mapView.getMapboxMap().addOnCameraChangeListener(onCameraChangeListener)
    }

    private fun setOnClickListener() {
        val savePinButton = requireView().findViewById<ImageButton>(R.id.save_pin_button)
        savePinButton.setOnClickListener {

            // Get latitude an longitude from the screen
            val latitude = mapView.getMapboxMap().cameraState.center.latitude()
            val longitude = mapView.getMapboxMap().cameraState.center.longitude()

            // Create a new pin to save
            val pin = Pin(latitude, longitude)
            viewModel.savePin(pin)
            navigateBack()

            // Show success
            Toast.makeText(this.requireContext(), "Pin salvo!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.action_addPinFragment2_to_userLocationFragment)
    }



    // Set coordinates to TextViews
    private fun setCoordinatesText() {
            val latitudeTv = requireView().findViewById<TextView>(R.id.latitude_tv)
        latitudeTv.text = currentLocation.latitude().toString()

            val longitudeTv = requireView().findViewById<TextView>(R.id.longitude_tv)
        longitudeTv.text = currentLocation.longitude().toString()

    }

    override fun onCameraChanged(eventData: CameraChangedEventData) {
        currentLocation = mapView.getMapboxMap().cameraState.center
        val latitudeTv = requireView().findViewById<TextView>(R.id.latitude_tv)
        latitudeTv.text = currentLocation.latitude().toString()
        val longitudeTv = requireView().findViewById<TextView>(R.id.longitude_tv)
        longitudeTv.text = currentLocation.longitude().toString()

    }


}