package com.michaelpessoni.mapdesafiofordiel.ui.addpin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.Plugin
import com.michaelpessoni.mapdesafiofordiel.R
import com.michaelpessoni.mapdesafiofordiel.databinding.AddPinFragmentBinding

class AddPinFragment : Fragment() {


    private lateinit var binding: AddPinFragmentBinding
    private lateinit var viewModel: AddPinViewModel
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
        viewModel = ViewModelProvider(this).get(AddPinViewModel::class.java)

        mapView = requireView().findViewById(R.id.mapView)


    }


}