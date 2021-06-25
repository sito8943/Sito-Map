package com.inmersoft.trinidadpatrimonial.map.ui

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.textfield.TextInputLayout
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.MapFragmentBinding
import com.inmersoft.trinidadpatrimonial.map.ui.adapter.MapPlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: MapFragmentBinding

    val safeArgs: MapFragmentArgs by navArgs()

    private lateinit var placesTypeAdapter: MapPlaceTypeAdapter

    private lateinit var map: GoogleMap

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MapFragmentBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val autoCompletePlacesNameAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
        val autoCompleteTextView: AutoCompleteTextView?
        val textInputLayout: TextInputLayout = binding.searchField
        autoCompleteTextView = textInputLayout.editText as AutoCompleteTextView?
        autoCompleteTextView?.setAdapter(autoCompletePlacesNameAdapter)
        placesTypeAdapter =
            MapPlaceTypeAdapter()


        binding.placeTypeList.adapter = placesTypeAdapter


/*
        binding.btnUserGps.setOnClickListener {
            val action =
                MapFragmentDirections.actionNavMapToDetailsFragment(placeID = 1)
            findNavController().navigate(action)
        }*/

        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(viewLifecycleOwner, { placesTypeList ->
            placesTypeAdapter.setData(placesTypeList)
        })

        //AutoComplete
        trinidadDataViewModel.allPlacesName.observe(viewLifecycleOwner, { placeNamesList ->
            autoCompletePlacesNameAdapter.addAll(placeNamesList)
            autoCompletePlacesNameAdapter.notifyDataSetChanged()
        })

        return binding.root
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
                if (place.place_id == placeIdArgs) {
                    trinidadGPS = LatLng(place.location.latitude, place.location.longitude)
                }
                val gpsPoint = LatLng(place.location.latitude, place.location.longitude)
                val marker = map.addMarker(
                    MarkerOptions().position(gpsPoint)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        .title(place.place_id.toString())
                        .snippet(place.place_description)

                )
                marker?.tag = place.place_id


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
        val action =
            MapFragmentDirections.actionNavMapToDetailsFragment(placeID = marker.tag as Int)
        findNavController().navigate(action)
        return true
    }

}