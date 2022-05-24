package com.michaelpessoni.mapdesafiofordiel.ui.userlocation

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings
import com.mapbox.maps.plugin.locationcomponent.location
import com.michaelpessoni.mapdesafiofordiel.R
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDAO

class UserLocationViewModel(private val dataSource: PinsDAO, private val mapView: MapView) : ViewModel() {
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

    fun initLocationPuck(context: Context) {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            setLocationPuck(context)
        }
    }

    private fun LocationComponentSettings.setLocationPuck(
        context: Context
    ) {
        this.enabled = true
        this.locationPuck = LocationPuck2D(
            bearingImage = AppCompatResources.getDrawable(
                context,
                R.drawable.user_pin,
            ),
            shadowImage = AppCompatResources.getDrawable(
                context,
                R.drawable.user_puck_icon,
            ),
            scaleExpression = interpolate {
                linear()
                zoom()
                stop {
                    literal(0.0)
                    literal(0.6)
                }
                stop {
                    literal(20.0)
                    literal(1.0)
                }
            }.toJson()
        )
    }

    fun onDestroy() {
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }
}