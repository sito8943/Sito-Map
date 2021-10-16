package com.sito.mapview

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

var points = arrayOf<SPoint>(
    SPoint(18.06, 59.31, "Mi Casa"),
    SPoint(19.06, 60.31, "Mi Casa1"),
    SPoint(20.06, 61.31, "Mi Casa2"),
    SPoint(21.06, 62.31, "Mi Casa3")
)

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private var markerSelected = false
    private var markerAnimator = ValueAnimator()
    private var firstTime = true
    //private lateinit var binding: ActivityLocationLayerBasicPulsingCircleBinding

    private lateinit var locationPermissionHelper: LocationPermissionHelper
    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        if (firstTime) {
            // Jump to the current indicator position
            mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
            // Set the gestures plugin's focal point to the current indicator location.
            mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
            firstTime = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activity)
        mapView = findViewById(R.id.mapView)
        mapboxMap = mapView.getMapboxMap()
        locationPermissionHelper = LocationPermissionHelper(this)
        locationPermissionHelper.checkPermissions {
            mapboxMap.loadStyleUri(
                Style.DARK
                        + geoJsonSource("marker-source") {
                    featureCollection(
                        FeatureCollection.fromFeatures(markerCoordinates.map {
                            Feature.fromGeometry(
                                it
                            )
                        })
                    )
                }
                        // Add the marker image to map
                        + image("my-marker-image") {
                    bitmap(BitmapFactory.decodeResource(resources, R.drawable.red_marker))
                }
                        // Adding an offset so that the bottom of the blue icon gets fixed to the coordinate, rather than the
                        // middle of the icon being fixed to the coordinate point.
                        + symbolLayer("marker-layer", "marker-source") {
                    iconImage("my-marker-image")
                    iconAllowOverlap(true)
                    iconOffset(listOf(0.0, -9.0))
                }
                        // Add the selected marker source and layer
                        + geoJsonSource("selected-marker") {
                    geometry(Point.fromLngLat(0.0, 0.0))
                }
                        // Adding an offset so that the bottom of the blue icon gets fixed to the coordinate, rather than the
                        // middle of the icon being fixed to the coordinate point.
                        + symbolLayer("selected-marker-layer", "selected-marker") {
                    iconImage("my-marker-image")
                    iconAllowOverlap(true)
                    iconOffset(listOf(0.0, -9.0))
                }
            ) /*{ addAnnotationToMap(points) }*/
            mapView.gestures.addOnMapClickListener { point ->
                mapboxMap.getStyle {
                    val selectedMarkerSymbolLayer =
                        it.getLayer("selected-marker-layer") as SymbolLayer
                    val pixel = mapboxMap.pixelForCoordinate(point)
                    mapboxMap.queryRenderedFeatures(
                        screenBoxFromPixel(pixel),
                        RenderedQueryOptions(listOf("selected-marker-layer"), null)
                    ) { expected ->
                        if (expected.value!!.isNotEmpty() && markerSelected) {
                            return@queryRenderedFeatures
                        }

                        mapboxMap.queryRenderedFeatures(
                            screenBoxFromPixel(pixel),
                            RenderedQueryOptions(listOf("marker-layer"), null)
                        ) InnerRenderedQueryOptions@{ expectedFeatures ->
                            val queriedFeatures = expectedFeatures.value!!
                            if (queriedFeatures.isEmpty()) {
                                if (markerSelected) {
                                    updateMarker(selectedMarkerSymbolLayer, false)
                                }
                                return@InnerRenderedQueryOptions
                            }

                            it.getSourceAs<GeoJsonSource>("selected-marker")!!.apply {
                                queriedFeatures[0].feature.geometry()?.let { value ->
                                    geometry(value)
                                }
                            }

                            if (markerSelected) {
                                updateMarker(selectedMarkerSymbolLayer, false)
                            }
                            if (queriedFeatures.isNotEmpty()) {
                                updateMarker(selectedMarkerSymbolLayer, true)
                            }
                        }
                    }
                }
                /*mapView.location
                    .isLocatedAt(point) { isPuckLocatedAtPoint ->
                        if (isPuckLocatedAtPoint) {
                            Toast.makeText(this, "Clicked on location puck", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }*/
                true
            }
            mapView.gestures.addOnMapLongClickListener { point ->
                mapView.location
                    .isLocatedAt(point) { isPuckLocatedAtPoint ->
                        if (isPuckLocatedAtPoint) {
                            Toast.makeText(
                                this,
                                "Long-clicked on location puck",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                true
            }
        }
    }

    private fun screenBoxFromPixel(pixel: ScreenCoordinate) = ScreenBox(
        ScreenCoordinate(pixel.x - 25.0, pixel.y - 25.0),
        ScreenCoordinate(pixel.x + 25.0, pixel.y + 25.0)
    )

    private fun updateMarker(iconLayer: SymbolLayer, select: Boolean) {
        if (select) markerAnimator = ValueAnimator()
        markerAnimator.apply {
            if (select) {
                setFloatValues(1.0f, 2.0f)
            } else {
                setFloatValues(2.0f, 1.0f)
            }
            duration = 300
            addUpdateListener {
                iconLayer.iconSize((it.animatedValue as Float).toDouble())
            }
            start()
        }
        markerSelected = select
    }

    //function to add markers to map
    private fun addAnnotationToMap(Points: Array<SPoint>) {
        for (sPoint in Points) {
            // Create an instance of the Annotation API and get the PointAnnotationManager.
            bitmapFromDrawableRes(
                this@MainActivity,
                R.drawable.red_marker
            )?.let {
                val annotationApi = mapView.annotations
                val pointAnnotationManager = annotationApi.createPointAnnotationManager(mapView)
                // Set options for the resulting symbol layer.
                val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                    // Define a geographic coordinate.
                    .withPoint(Point.fromLngLat(sPoint.lon, sPoint.lat))
                    // Specify the bitmap you assigned to the point annotation
                    // The bitmap will be added to map style automatically.
                    .withTextField(sPoint.name)
                    .withIconImage(it)
                // Add the resulting pointAnnotation to the map.
                pointAnnotationManager.create(pointAnnotationOptions)
            }
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        mapView.location
            .addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    companion object {
        private val markerCoordinates = arrayListOf<Point>(
            Point.fromLngLat(-71.065634, 42.354950), // Boston Common Park
            Point.fromLngLat(-71.097293, 42.346645), // Fenway Park
            Point.fromLngLat(-71.053694, 42.363725) // The Paul Revere House
        )
    }

}

class LocationPermissionHelper(val activity: Activity) {
    private lateinit var permissionsManager: PermissionsManager

    fun checkPermissions(onMapReady: () -> Unit) {
        if (PermissionsManager.areLocationPermissionsGranted(activity)) {
            onMapReady()
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String>) {
                    Toast.makeText(
                        activity, "You need to accept location permissions.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        onMapReady()
                    } else {
                        activity.finish()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(activity)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}