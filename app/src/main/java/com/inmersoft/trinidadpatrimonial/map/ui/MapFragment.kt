package com.inmersoft.trinidadpatrimonial.map.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputLayout
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.imageloader.GlideImageLoader
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.MapFragmentBinding
import com.inmersoft.trinidadpatrimonial.map.ui.adapter.PlaceTypeAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : Fragment() {


    private lateinit var binding: MapFragmentBinding

    private lateinit var imageLoader: ImageLoader

    private lateinit var placesTypeAdapter: PlaceTypeAdapter

    private val mapFragmentViewModel: MapFragmentViewModel by viewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        //TODO ( La posicion inicial de trinidad se podria pedir a la base de datos )
        val trinidadGPS = LatLng(21.796282222968483, -79.98046886229075)
        val trinidadGPS2 = LatLng(21.800735, -79.984793)
        googleMap.addMarker(
            MarkerOptions().position(trinidadGPS)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.splash_screen_icon))
                .title("Trinidad")
        )

        googleMap.addMarker(
            MarkerOptions().position(trinidadGPS2)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.splash_screen_icon))
                .title("Plaza Carillo")
        )
        val cameraPosition = CameraPosition.Builder()
            .target(trinidadGPS)
            .zoom(15f)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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

        imageLoader = GlideImageLoader(requireContext())

        placesTypeAdapter =
            PlaceTypeAdapter(imageLoader)

        binding.placeTypeList.adapter = placesTypeAdapter

        mapFragmentViewModel.allPlaceTypeWithPlaces.observe(viewLifecycleOwner, { placesTypeList ->
            placesTypeAdapter.setData(placesTypeList)
        })

        //AutoComplete

        mapFragmentViewModel.allPlaces.observe(viewLifecycleOwner, {

            val arrayPlacesName = it.map { place -> place.place_name }.toList()

            autoCompletePlacesNameAdapter.addAll(arrayPlacesName)
            val autoCompleteTextView: AutoCompleteTextView?
            val textInputLayout: TextInputLayout = binding.searchField
            autoCompleteTextView = textInputLayout.editText as AutoCompleteTextView?
            autoCompleteTextView?.setAdapter(autoCompletePlacesNameAdapter)
            autoCompletePlacesNameAdapter.notifyDataSetChanged()
        })

        return binding.root
    }

    private fun initMap(mapFragment: SupportMapFragment?) {
        mapFragment?.getMapAsync(callback)
    }
}