package com.michaelpessoni.mapdesafiofordiel.ui.showpins

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.michaelpessoni.mapdesafiofordiel.data.Pin
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDAO

class ShowPinsViewModel(private val dataSource: PinsDAO, private val mapView: MapView): ViewModel() {
    fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
        }
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )
    }

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }

    //Setup listeners to user interactions
    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
        mapView.getMapboxMap().addOnCameraChangeListener(onCameraChangeListener)
    }

    // create listeners for screen move
    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {

            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {

        }
    }
    // Encapsulated LiveData to notify that onCameraTrackDismiss was called
    private val _cameraTrackDismissed = MutableLiveData<Boolean>()
    val cameraTrackDismissed: LiveData<Boolean>
        get() = _cameraTrackDismissed

    fun onCameraTrackingDismissed() {
        _cameraTrackDismissed.value = true
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        // mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    private val _currentLongitude = MutableLiveData<Double>()
    val currentLongitude: LiveData<Double>
        get() = _currentLongitude

    private val _currentLatitude = MutableLiveData<Double>()
    val currentLatitude: LiveData<Double>
        get() = _currentLatitude

    private val onCameraChangeListener = OnCameraChangeListener {
        _currentLongitude.value = mapView.getMapboxMap().cameraState.center.longitude()
        _currentLatitude.value = mapView.getMapboxMap().cameraState.center.latitude()
    }

    fun showAllPins() {
        val annotationApi = mapView.annotations
        val polylineAnnotationManager = annotationApi.createPolylineAnnotationManager()

        // Define a list of geographic coordinates to be connected.
        val points = setPinsToShow(pinsList)
        // Set options for the resulting line layer.
        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withPoints(points)
            // Style the line that will be added to the map.
            .withLineColor("#ee4e8b")
        polylineAnnotationManager.create(polylineAnnotationOptions)
    }

    fun setPinsToShow(pinsList: List<Pin>?): List<Point> {
        val pointsList: List<Point>? = pinsList?.map { pin ->
            Point.fromLngLat(pin.longitude, pin.latitude)
        }

        return pointsList ?: emptyList()
    }

    // Get pins from database
     val pinsList =  dataSource.getAllPins()

}