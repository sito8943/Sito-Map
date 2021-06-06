package com.inmersoft.trinidadpatrimonial.details.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inmersoft.trinidadpatrimonial.databinding.FragmentDetailsPlaceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsPlaceFragment : Fragment() {

    private lateinit var binding: FragmentDetailsPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailsPlaceBinding.inflate(layoutInflater, container, false)
        val placeId = requireArguments().getInt("placeId")
        binding.placeName.text = "$placeId"
        return binding.root
    }


}