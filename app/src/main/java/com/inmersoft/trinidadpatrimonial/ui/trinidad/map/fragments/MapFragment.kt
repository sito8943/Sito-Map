package com.inmersoft.trinidadpatrimonial.ui.trinidad.map.fragments

import android.location.Location
import android.os.Bundle
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
import com.google.android.material.textfield.TextInputLayout
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentMapBinding
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.map.adapters.MapPlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var binding: FragmentMapBinding

    private val safeArgs: MapFragmentArgs by navArgs()

    private val placesTypeAdapter: MapPlaceTypeAdapter by lazy {
        MapPlaceTypeAdapter()
    }

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

    }

    override fun onStart() {
        super.onStart()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        //BottomSheet Information
        trinidadDataViewModel.currentPlaceToBottomSheet.observe(viewLifecycleOwner,
            { currentPlace ->
                if (currentPlace != null) {
                    val nav = MapFragmentDirections.actionNavMapToDetailsFragment(
                        currentPlace.place_id
                    )
                    showTrinidadBottomSheetPlaceInfo(
                        place = currentPlace, navDirections = nav
                    )
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

        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { places ->
            val placeIdArgs = safeArgs.placeID
            var trinidadGPS = LatLng(places[0].location.latitude, places[0].location.longitude)

            places.forEach { place ->
                val gpsPoint = LatLng(place.location.latitude, place.location.longitude)
                val marker: Marker?

                if (place.place_id == placeIdArgs) {
                    trinidadGPS = LatLng(place.location.latitude, place.location.longitude)

                    marker = map.addMarker(
                        MarkerOptions().position(gpsPoint)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title(place.place_name)
                            .snippet(place.place_description)
                    )
                    marker.showInfoWindow()
                    val nav = MapFragmentDirections.actionNavMapToDetailsFragment(
                        place.place_id
                    )
                    showTrinidadBottomSheetPlaceInfo(
                        place, navDirections = nav
                    )
                } else {
                    marker = map.addMarker(
                        MarkerOptions().position(gpsPoint)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .title(place.place_name)
                            .snippet(place.place_description)
                    )
                }
                marker?.tag = place.place_id
                listOfMarkers.add(marker)
            }
            val cameraPosition = CameraPosition.Builder()
                .target(trinidadGPS)
                .zoom(18f)
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        )
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

        trinidadDataViewModel.onBottomSheetShow(placeID)

        return true
    }


    override fun onDestroy() {
        trinidadDataViewModel.onMapDestroy()
        super.onDestroy()
    }
}