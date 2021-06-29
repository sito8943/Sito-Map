package com.inmersoft.trinidadpatrimonial.map.ui

import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialContainerTransform
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.MapFragmentBinding
import com.inmersoft.trinidadpatrimonial.map.ui.adapter.MapPlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.SheetData
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MapFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    private lateinit var binding: MapFragmentBinding

    private val safeArgs: MapFragmentArgs by navArgs()

    private lateinit var placesTypeAdapter: MapPlaceTypeAdapter

    private lateinit var map: GoogleMap

    private val listOfMarkers = mutableListOf<Marker>()

    private val trinidadDataViewModel: TrinidadDataViewModel by activityViewModels()

    private lateinit var trinidadBottomSheet: TrinidadBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedTransitionEffect = MaterialContainerTransform(requireContext(), true)
        sharedTransitionEffect.fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
        sharedElementEnterTransition = sharedTransitionEffect
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MapFragmentBinding.inflate(inflater, container, false)

        val started = safeArgs.placeID != -1

        trinidadBottomSheet =
            TrinidadBottomSheet(
                requireContext(),
                started = started,
                binding.root as ViewGroup,
                findNavController()
            )

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

        placesTypeAdapter = MapPlaceTypeAdapter()

        binding.placeTypeList.adapter = placesTypeAdapter

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
                    showTrinidadBottomSheet(place)
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

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("TAG", "onMarkerClick: $placeID")
            val place = trinidadDataViewModel.getPlaceById(placeID)
            withContext(Dispatchers.Main) {
                showTrinidadBottomSheet(place)
            }
        }

        return true
    }

    private fun showTrinidadBottomSheet(place: Place) {
        val uriImage = Uri.parse(
            TrinidadAssets.getAssetFullPath(
                place.header_images[0],
                TrinidadAssets.FILE_JPG_EXTENSION
            )
        )
        val webURI = Uri.parse(place.web)
        val data =
            SheetData(
                place.place_id,
                uriImage,
                place.place_name,
                place.place_description,
                webURI
            )

        trinidadBottomSheet.navigateTo(
            MapFragmentDirections.actionNavMapToDetailsFragment(
                place.place_id
            )
        )
        trinidadBottomSheet.bindData(data)
        trinidadBottomSheet.show()
    }


}