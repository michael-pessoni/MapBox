package com.michaelpessoni.mapdesafiofordiel.ui.showpins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mapbox.maps.MapView
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDAO
import com.michaelpessoni.mapdesafiofordiel.ui.addpin.AddPinViewModel

class ShowPinsViewModelFactory(
    private val dataSource: PinsDAO,
    private val mapView: MapView
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowPinsViewModel::class.java)){
            return ShowPinsViewModel(dataSource, mapView) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
