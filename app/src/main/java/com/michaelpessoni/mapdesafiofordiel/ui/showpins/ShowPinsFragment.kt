package com.michaelpessoni.mapdesafiofordiel.ui.showpins

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mapbox.maps.MapView
import com.michaelpessoni.mapdesafiofordiel.R
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDatabase
import com.michaelpessoni.mapdesafiofordiel.databinding.ShowPinsFragmentBinding
import com.michaelpessoni.mapdesafiofordiel.ui.MapViewModel

class ShowPinsFragment : Fragment() {

    private lateinit var binding: ShowPinsFragmentBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.show_pins_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataSource = PinsDatabase.getInstance(this.requireContext()).pinsDatabaseDAO
        mapView = requireView().findViewById(R.id.mapView)
        viewModel = MapViewModel(dataSource, mapView)

        viewModel.onMapReady()

        viewModel.showAllPins()

    }
}