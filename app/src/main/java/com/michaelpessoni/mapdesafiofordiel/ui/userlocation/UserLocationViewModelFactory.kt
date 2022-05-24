package com.michaelpessoni.mapdesafiofordiel.ui.userlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mapbox.maps.MapView
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDAO

class UserLocationViewModelFactory(
    private val dataSource: PinsDAO,
    private val mapView: MapView
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserLocationViewModel::class.java)){
            return UserLocationViewModel(dataSource, mapView) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}