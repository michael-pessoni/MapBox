package com.michaelpessoni.mapdesafiofordiel.ui.addpin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.Plugin
import com.michaelpessoni.mapdesafiofordiel.R
import com.michaelpessoni.mapdesafiofordiel.databinding.AddPinCardBinding
import com.michaelpessoni.mapdesafiofordiel.databinding.AddPinFragmentBinding
import com.michaelpessoni.mapdesafiofordiel.ui.userlocation.MapViewModel

class AddPinFragment : Fragment() {


    private lateinit var binding: AddPinFragmentBinding
    private lateinit var cardBinding: AddPinCardBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_pin_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]

        mapView = requireView().findViewById(R.id.mapView)

        viewModel.mapView = mapView

        viewModel.onMapReady()

        viewModel.addPinToMap(this.requireContext())

        viewModel.currentLatitude.observe(viewLifecycleOwner, Observer { latitude ->
            cardBinding.latitudeTv.text = latitude.toString()
        })
        viewModel.currentLongitude.observe(viewLifecycleOwner, Observer { longitude ->
            cardBinding.longitudeTv.text = longitude.toString()
        })


    }


}