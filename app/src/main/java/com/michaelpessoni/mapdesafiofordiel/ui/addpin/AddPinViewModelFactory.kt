package com.michaelpessoni.mapdesafiofordiel.ui.addpin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mapbox.maps.MapView
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDAO

class AddPinViewModelFactory(
    private val dataSource: PinsDAO,
    private val mapView: MapView
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPinViewModel::class.java)){
            return AddPinViewModel(dataSource, mapView) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
