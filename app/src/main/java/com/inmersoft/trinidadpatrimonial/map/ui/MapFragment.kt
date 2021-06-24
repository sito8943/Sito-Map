package com.inmersoft.trinidadpatrimonial.map.ui

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
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputLayout
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.MapFragmentBinding
import com.inmersoft.trinidadpatrimonial.home.ui.fragments.HomeFragmentDirections
import com.inmersoft.trinidadpatrimonial.map.ui.adapter.MapPlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment() {
    private lateinit var binding: MapFragmentBinding

    val safeArgs: MapFragmentArgs by navArgs()

    private lateinit var placesTypeAdapter: MapPlaceTypeAdapter

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        //TODO ( La posicion inicial de trinidad se podria pedir a la base de datos )

        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { places ->
            val placeIdArgs = safeArgs.placeID

            var trinidadGPS = LatLng(places[0].location.latitude, places[0].location.longitude)

            places.forEach { place ->
                if (place.place_id == placeIdArgs) {
                    trinidadGPS = LatLng(place.location.latitude, place.location.longitude)
                }
                val gpsPoint = LatLng(place.location.latitude, place.location.longitude)
                googleMap.addMarker(
                    MarkerOptions().position(gpsPoint)
                        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.splash_screen_icon))
                        .title(place.place_name).draggable(true)
                        .snippet(place.place_description)
                )
            }

            val cameraPosition = CameraPosition.Builder()
                .target(trinidadGPS)
                .zoom(18f)
                .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MapFragmentBinding.inflate(inflater, container, false).also {
            (childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment).also {
                initMap(it)
            }
        }

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

        binding.openTestBottomSheet.setOnClickListener {
            val action =
                HomeFragmentDirections.actionNavHomeToDetailsFragment(placeID = 1)
            findNavController().navigate(action)
        }

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

    private fun initMap(mapFragment: SupportMapFragment?) {
        mapFragment?.getMapAsync(callback)

    }
}