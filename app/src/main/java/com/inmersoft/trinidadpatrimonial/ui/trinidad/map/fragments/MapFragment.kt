package com.inmersoft.trinidadpatrimonial.ui.trinidad.map.fragments

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.FragmentMapBinding
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.map.adapters.MapPlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener, MapPlaceTypeAdapter.Event {

    private lateinit var binding: FragmentMapBinding

    private val safeArgs: MapFragmentArgs by navArgs()

    private val placesTypeAdapter: MapPlaceTypeAdapter by lazy {
        MapPlaceTypeAdapter(this@MapFragment)
    }

    private lateinit var trinidadGPS: LatLng

    private lateinit var map: GoogleMap

    private val listOfMarkers = mutableListOf<Marker>()

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
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupUI()
        return binding.root
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
                        putMapCameraToStart()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        binding.openDrawer.setOnClickListener {
            openDrawerInTrinidadActivity()
        }
    }

    private fun putMapCameraToStart() {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(trinidadGPS, 17f))
    }

    override fun onStart() {
        super.onStart()
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
                    Log.d("MApFragment",
                        "subscribeObservers:PARENT:${this.javaClass.toString()}  PARENT IS INCORRECT ")
                }
            })

        //AutoComplete
        trinidadDataViewModel.allPlacesName.observe(viewLifecycleOwner, { placeNamesList ->
            autoCompletePlacesNameAdapter.addAll(placeNamesList)
            autoCompletePlacesNameAdapter.notifyDataSetChanged()
        })

        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(viewLifecycleOwner, { placesTypeList ->
            placesTypeAdapter.setData(placesTypeList)
        })

        //Map
        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { places ->
            showPlacesInMap(places)
            trinidadGPS = LatLng(places[0].location.latitude, places[0].location.longitude)
            putMapCameraToStart()
        }
        )

        trinidadDataViewModel.placeTypeFiltered.observe(viewLifecycleOwner, {
            showPlacesInMap(it.placesList)
        })

    }

    private fun showPlacesInMap(places: List<Place>) {
        val placeIdArgs = safeArgs.placeID
        listOfMarkers.clear()
        places.forEach { place ->
            val gpsPoint = LatLng(place.location.latitude, place.location.longitude)
            val marker: Marker?
            if (place.place_id == placeIdArgs) {
                val placeLocation = LatLng(place.location.latitude, place.location.longitude)
                marker = map.addMarker(
                    MarkerOptions().position(gpsPoint)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(place.place_name)
                )

                val nav = MapFragmentDirections.actionNavMapToDetailsFragment(
                    place.place_id
                )
                showTrinidadBottomSheetPlaceInfo(
                    place, navDirections = nav
                )

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 17f))

            } else {
                marker = map.addMarker(
                    MarkerOptions().position(gpsPoint)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .title(place.place_name)
                        .snippet(place.place_description)
                )
                val placeLocation =
                    LatLng(places[0].location.latitude, places[0].location.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 17f))
            }
            marker?.tag = place.place_id
            listOfMarkers.add(marker)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMyLocationClick(p0: Location) {
        TODO("Not yet implemented")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.map_style
            )
        )
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMapToolbarEnabled = true
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
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

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        trinidadDataViewModel.onBottomSheetSetInfo(placeID, _parent = this.javaClass.toString())

        return true
    }

    override fun onDestroy() {
        trinidadDataViewModel.onMapDestroy()
        super.onDestroy()
    }

    override fun onClickListener(placeId: Int) {
        Log.d("TAG", "onClickListener: Called using ID $placeId")
        listOfMarkers.clear()
        map.clear()
        trinidadDataViewModel.onMapFilter(placeId)
        trinidadBottomSheet.hide()
    }
}