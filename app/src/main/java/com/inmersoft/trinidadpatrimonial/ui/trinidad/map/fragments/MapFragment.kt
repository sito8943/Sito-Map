package com.inmersoft.trinidadpatrimonial.ui.trinidad.map.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.FragmentMapBinding
import com.inmersoft.trinidadpatrimonial.extensions.showToastExt
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.map.adapters.MapPlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.ui.trinidad.map.utils.BaseMapFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.map.utils.MapPoint
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MapFragment : BaseFragment(), OnPointAnnotationClickListener, MapPlaceTypeAdapter.Event {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: MapFragmentArgs by navArgs()

    private val placesTypeAdapter: MapPlaceTypeAdapter by lazy {
        MapPlaceTypeAdapter(this@MapFragment)
    }

    private val trinidadGPS: MapPoint by lazy {
        MapPoint(
            -1, "Trinidad",
            21.8055678,
            -79.985233,
            R.drawable.map_icon
        )
    }

    private val mapFragment: BaseMapFragment by lazy {
        childFragmentManager.findFragmentById(R.id.map_fragment) as BaseMapFragment
    }

    private val listOfMarkers = mutableListOf<MapPoint>()

    private val listOfMapPoint = mutableListOf<MapPoint>()

    private val showBottomSheetOnStart: Boolean by lazy { safeArgs.placeID != -1 }

    private val autoCompletePlacesNameAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment.getMapAsync { map ->
            map.loadStyleUri(
                Uri.parse("mapbox://styles/qu35t64/ckskkkh5k2wlr18lugd8arsrp")
                    .toString()
            )
            mapFragment.addOnPointAnnotationClickListener(this@MapFragment)
            map.flyTo(
                CameraOptions.Builder()
                    .center(trinidadGPS.getAsPoint())
                    .zoom(15.0)
                    .build(), null
            )
            // addMapsElements()
        }
        setupUI()
    }


    private fun setupUI() {

        binding.placeTypeList.adapter = placesTypeAdapter

        val autoCompleteTextView: AutoCompleteTextView?
        val textInputLayout: TextInputLayout = binding.searchField
        autoCompleteTextView = textInputLayout.editText as AutoCompleteTextView?
        autoCompleteTextView?.setAdapter(autoCompletePlacesNameAdapter)

        trinidadBottomSheet =
            TrinidadBottomSheet(
                requireContext(),
                started = showBottomSheetOnStart,
                binding.root as ViewGroup,
                findNavController()
            )

        trinidadBottomSheet.addTrindadBottomSheetCallBack(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        mapFlyTo(trinidadGPS.getAsPoint())
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        binding.openDrawer.setOnClickListener {
            openDrawerInTrinidadActivity()
        }
        subscribeObservers()
    }


    private fun subscribeObservers() {
        //BottomSheet Information
        trinidadDataViewModel.currentPlaceToBottomSheet.observe(viewLifecycleOwner,
            { currentPlace ->
                if (trinidadDataViewModel.isParent(this.javaClass.toString())) {
                    if (currentPlace != null) {
                        val nav = MapFragmentDirections.actionNavMapToDetailsFragment(
                            currentPlace.place_id
                        )
                        showTrinidadBottomSheetPlaceInfo(
                            place = currentPlace, navDirections = nav
                        )
                    }
                } else {
                    Log.d(
                        "MApFragment",
                        "subscribeObservers:PARENT:${this.javaClass.toString()}  PARENT IS INCORRECT "
                    )
                }
            })

        //AutoComplete
        trinidadDataViewModel.allPlacesName.observe(viewLifecycleOwner, { placeNamesList ->
            autoCompletePlacesNameAdapter.addAll(placeNamesList)
            autoCompletePlacesNameAdapter.notifyDataSetChanged()
        })

        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(viewLifecycleOwner,
            { placesTypeList ->
                placesTypeAdapter.setData(placesTypeList)
            })

        //Map
        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { places ->
            showPlacesInMap(places)
        }
        )

        trinidadDataViewModel.placeTypeFiltered.observe(viewLifecycleOwner, {
            showPlacesInMap(it.placesList)
        })
    }

    private fun showPlacesInMap(places: List<Place>) {
        val placeIdArgs = safeArgs.placeID
        var flagSafeArgs = false

        listOfMapPoint.clear()

        places.forEach { place ->
            val mapPoint =
                MapPoint(
                    place.place_id,
                    place.place_name,
                    place.location.latitude,
                    place.location.longitude,
                    R.drawable.map_icon
                )
            if (place.place_id == placeIdArgs) {
                flagSafeArgs = true
                val nav = MapFragmentDirections.actionNavMapToDetailsFragment(
                    place.place_id
                )
                showTrinidadBottomSheetPlaceInfo(
                    place, navDirections = nav
                )
                mapFlyTo(Point.fromLngLat(mapPoint.longitude, mapPoint.latitude), 17.0)
            }
            listOfMapPoint.add(mapPoint)
        }
        if (!flagSafeArgs) {
            mapFlyTo(
                Point.fromLngLat(
                    places[0].location.longitude,
                    places[0].location.latitude
                ), 17.0
            )
        }
        mapFragment.setPoints(listOfMapPoint)
    }

    fun mapFlyTo(point: Point, zoom: Double = 15.0) {
        mapFragment.getMapView().getMapboxMap().flyTo(
            CameraOptions.Builder()
                .center(point)
                .zoom(zoom)
                .build(), MapAnimationOptions.mapAnimationOptions { duration(1000L) })
    }


    override fun onDestroy() {
        trinidadDataViewModel.onMapDestroy()
        _binding = null
        super.onDestroy()
    }

    override fun onClickListener(placeId: Int) {
        Log.d("TAG", "onClickListener: Called using ID $placeId")
        listOfMarkers.clear()

        trinidadDataViewModel.onMapFilter(placeId)
        trinidadBottomSheet.hide()
    }

    override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
        val nameTextField = annotation.textField
        if (!nameTextField.isNullOrEmpty()) {
            listOfMapPoint.forEach {
                if (it.text == nameTextField) {
                    //mapFragment.changePointAnnotationOptions(annotation)
                    mapFlyTo(it.getAsPoint(), 19.0)
                    trinidadDataViewModel.onBottomSheetSetInfo(
                        it.placeIdInRoomDB,
                        _parent = this.javaClass.toString()
                    )
                    return@forEach
                }
            }
            //mapFragment.setPoints(listOfMapPoint)
        }

        /*
    val placeID = marker.tag as Int

    listOfMarkers.forEach { currentMarker ->
        currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
    }

    marker.showInfoWindow()
    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

    val cameraPosition = CameraPosition.Builder()
        .target(marker.position)
        .zoom(18f)
        .build()

    trinidadGPS = marker.position

    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    trinidadDataViewModel.onBottomSheetSetInfo(placeID, _parent = this.javaClass.toString())
*/
        return true

    }
}