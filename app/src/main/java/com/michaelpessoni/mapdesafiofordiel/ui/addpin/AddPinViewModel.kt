package com.michaelpessoni.mapdesafiofordiel.ui.addpin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.michaelpessoni.mapdesafiofordiel.R
import com.michaelpessoni.mapdesafiofordiel.data.Pin
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDAO
import kotlinx.coroutines.launch

class AddPinViewModel(private val dataSource: PinsDAO, private val mapView: MapView) :ViewModel() {
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
            initLocation()
        }
    }

    private fun initLocation() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
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

    fun addPinToMap(context: Context) {

        // Create an instance of the Annotation API and get the PointAnnotationManager.
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS,
            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    bitmapFromDrawableRes(
                        context,
                        R.drawable.new_pin_icon
                    )?.let { bitmap ->
                        val annotationApi = mapView.annotations
                        val pointAnnotationManager = annotationApi.createPointAnnotationManager()
                        // Set options for the resulting symbol layer.
                        val pointAnnotationOptions: PointAnnotationOptions =
                            PointAnnotationOptions()
                                // Define a geographic coordinate.
                                .withPoint(
                                    Point.fromLngLat(
                                        mapView.getMapboxMap().cameraState.center.longitude(),
                                        mapView.getMapboxMap().cameraState.center.latitude()
                                    )
                                )
                                // Specify the bitmap you assigned to the point annotation
                                // The bitmap will be added to map style automatically.
                                .withIconImage(bitmap)
                        // Add the resulting pointAnnotation to the map.
                        pointAnnotationManager.create(pointAnnotationOptions)
                    }
                }
            }
        )
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
            // copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    fun savePin(pin: Pin) {
        viewModelScope.launch {
            dataSource.insert(pin)
        }
    }
}