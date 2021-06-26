package com.inmersoft.trinidadpatrimonial.map.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputLayout
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.MapFragmentBinding
import com.inmersoft.trinidadpatrimonial.map.ui.adapter.MapPlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.utils.ShareIntent
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.TrinidadCustomChromeTab
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@AndroidEntryPoint
class MapFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

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

        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(viewLifecycleOwner, { placesTypeList ->
            placesTypeAdapter.setData(placesTypeList)
        })

        //AutoComplete
        trinidadDataViewModel.allPlacesName.observe(viewLifecycleOwner, { placeNamesList ->
            autoCompletePlacesNameAdapter.addAll(placeNamesList)
            autoCompletePlacesNameAdapter.notifyDataSetChanged()
        })

        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

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
                } else {
                    marker = map.addMarker(
                        MarkerOptions().position(gpsPoint)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                            .title(place.place_name)
                            .snippet(place.place_description)
                    )
                }
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
        val placeID = marker.tag as Int

        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("TAG", "onMarkerClick: $placeID")
            val place = trinidadDataViewModel.getPlaceById(placeID)
            withContext(Dispatchers.Main) {
                Glide.with(requireContext())
                    .asBitmap()
                    .load(Uri.parse(TrinidadAssets.getAssetFullPath(place.header_images[0])))
                    .placeholder(R.drawable.placeholder_error)
                    .error(R.drawable.placeholder_error)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            binding.mapBottomSheetImageHeader.setImageBitmap(resource)
                            binding.bottomSheetShare.setOnClickListener {
                                ShareIntent.shareIt(
                                    requireContext(),
                                    resource,
                                    place.place_name,
                                    getString(R.string.app_name)
                                )
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                    })


                binding.bottomSheetPlaceName.text = place.place_name
                binding.bottomSheetPlaceName.isSelected = true
                binding.bottomSheetPlaceDescription.text = place.place_description

                binding.bottomSheetWebpage.setOnClickListener {
                    TrinidadCustomChromeTab.launch(requireContext(), place.web)
                }

                binding.seeMoreButton.setOnClickListener {
                    binding.bottomSheetImage.transitionName = UUID.randomUUID().toString()

                    val extras =
                        FragmentNavigatorExtras(
                            binding.bottomSheetImage to "shared_view_container"
                        )
                    val action =
                        MapFragmentDirections.actionNavMapToDetailsFragment(placeID = placeID)
                    findNavController().navigate(action, extras)
                }

                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return true
    }

}