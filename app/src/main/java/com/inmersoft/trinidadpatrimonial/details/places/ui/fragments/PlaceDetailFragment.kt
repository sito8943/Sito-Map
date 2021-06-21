package com.inmersoft.trinidadpatrimonial.details.places.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.PlaceDetailsFragmentBinding
import com.inmersoft.trinidadpatrimonial.utils.ASSETS_FOLDER


class PlaceDetailFragment(private val placeData: Place) : Fragment() {

    private lateinit var binding: PlaceDetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = PlaceDetailsFragmentBinding.inflate(layoutInflater, container, false)
        binding.placeName.text = placeData.place_name
        binding.placeDescription.text = placeData.place_description
        Glide.with(requireContext())
            .load(
                Uri.parse("$ASSETS_FOLDER/${placeData.header_images[0]}.jpg")
            )
            .error(R.drawable.placeholder_error)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.placeHeader)
        return binding.root
    }
}
