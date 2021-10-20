package com.sito.mapview

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
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
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var points = arrayOf<SPoint>(
    SPoint(18.06, 59.31, "Mi Casa"),
    SPoint(19.06, 60.31, "Mi Casa1"),
    SPoint(20.06, 61.31, "Mi Casa2"),
    SPoint(21.06, 62.31, "Mi Casa3")
)

class MainActivity : AppCompatActivity(), OnPointAnnotationClickListener {

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private var markerSelected = false
    private var consumeClickEvent = false
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
                Style.MAPBOX_STREETS
            ) {
                if (!::pointAnnotationManager.isInitialized) {
                    pointAnnotationManager =
                        mapView.annotations.createPointAnnotationManager(mapView)
                }
                addAnnotationToMap(points)
            }
            if (::pointAnnotationManager.isInitialized) {
                pointAnnotationManager.addClickListener(this@MainActivity)
            }
            /*mapView.gestures.addOnMapClickListener { point ->
                mapView.location
                    .isLocatedAt(point) { isPuckLocatedAtPoint ->
                        if (isPuckLocatedAtPoint) {
                            Toast.makeText(this, "Clicked on location puck", Toast.LENGTH_SHORT)
                                .show()
                        } else if (Casa(point)) {
                            Toast.makeText(this, point.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                true
            }*/
            /*mapView.gestures.addOnMapLongClickListener { point ->
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
            }*/
        }
    }

    private fun Casa(point: Point): Boolean {
        Toast.makeText(this, points[0].toString(), Toast.LENGTH_SHORT)
            .show()
        for (mPoint in points) {
            if (mPoint.lat == point.latitude() && mPoint.lon == point.longitude())
                return true
        }
        return false
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
        if (::pointAnnotationManager.isInitialized) {
            var countPoints = 100.0
            lifecycleScope.launch(Dispatchers.IO) {
                val pointsAnnotationOptions = Points.map { point ->

                    countPoints *= 10.0

                    val futureTarget: FutureTarget<Bitmap> = Glide.with(this@MainActivity)
                        .asBitmap()
                        .load(R.drawable.red_marker)
                        .submit(300, 300)
                    var bitmap = futureTarget.get()
                    bitmapFromDrawableRes(
                        this@MainActivity,
                        R.drawable.red_marker
                    )?.let {
                        bitmap = it
                    }
                    PointAnnotationOptions().apply {
                        withPoint(
                            com.mapbox.geojson.Point.fromLngLat(
                                point.lon,
                                point.lat
                            )
                        )
                        withIconImage(bitmap)
                        withIconSize(1.4)
                        withTextOffset(listOf(0.0, 2.0))
                        withTextSize(20.0)
                        withTextField(point.name)
                        withTextColor(Color.BLACK)
                        withTextHaloWidth(0.8)
                        withTextHaloColor(Color.WHITE)
                        withSymbolSortKey(countPoints)
                    }
                }
                withContext(Dispatchers.Main) {
                    pointAnnotationManager.create(pointsAnnotationOptions)
                }
            }
        }
        /*for (sPoint in Points) {
            // Create an instance of the Annotation API and get the PointAnnotationManager.
            val annotationApi = mapView.annotations
            val pointAnnotationManager = annotationApi.createPointAnnotationManager(mapView).apply {
                addClickListener(
                    OnPointAnnotationClickListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Clicked on location puck",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                )
                addClickListener(
                    OnPointAnnotationClickListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Clicked on location puck",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                )
                addLongClickListener(
                    OnPointAnnotationLongClickListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Long-clicked on location puck",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                )
                addLongClickListener(
                    OnPointAnnotationLongClickListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Long-clicked on location puck",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }
                )
                addInteractionListener(object : OnPointAnnotationInteractionListener {
                    override fun onDeselectAnnotation(annotation: PointAnnotation) {
                        Toast.makeText(
                            this@MainActivity,
                            "Deselected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onSelectAnnotation(annotation: PointAnnotation) {
                        Toast.makeText(
                            this@MainActivity,
                            "Selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
            bitmapFromDrawableRes(
                this@MainActivity,
                R.drawable.red_marker
            )?.let {
                // Set options for the resulting symbol layer.
                val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                    // Define a geographic coordinate.
                    .withPoint(Point.fromLngLat(sPoint.lon, sPoint.lat))
                    // Specify the bitmap you assigned to the point annotation
                    // The bitmap will be added to map style automatically.
                    .withTextField(sPoint.name)
                    .withIconImage(it)
                    .withDraggable(true)
                // Add the resulting pointAnnotation to the map.
                pointAnnotationManager.create(pointAnnotationOptions)
            }
        }*/
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
        private const val SOURCE_URI = "mapbox://examples.ciuz0vpc"
        private const val SOURCE_LAYER_ID = "Yosemite_POI-38jhes"
        private const val RESTROOMS = "restrooms"
        private const val TRAIL_HEAD = "trailhead"
        private const val PICNIC_AREA = "picnic-area"
        private const val KEY_PICNIC_AREA = "Picnic Area"
        private const val KEY_RESTROOMS = "Restroom"
        private const val KEY_TRAIL_HEAD = "Trailhead"
        private const val SOURCE_ID = "source_id"
        private const val LAYER_ID = "layer_id"
        private const val ICON_KEY = "POITYPE"
    }

    override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
        val pointName = annotation.textField
        if (!pointName.isNullOrEmpty()) {
            Log.d(
                "T1",
                pointName
            )
            points.forEach {
                if (it.name == pointName) {
                    mapboxMap.flyTo(
                        CameraOptions.Builder().center(it.getAsPoint()).zoom(19.0).build(),
                        MapAnimationOptions.mapAnimationOptions { duration(1000L) })
                    return@forEach
                }
            }
        }
        return true
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