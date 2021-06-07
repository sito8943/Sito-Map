package com.inmersoft.trinidadpatrimonial.details.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inmersoft.trinidadpatrimonial.databinding.ViewPagerDetailFragmentBinding


class ViewPagerDetailFragment : Fragment() {

    private lateinit var binding: ViewPagerDetailFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        binding = ViewPagerDetailFragmentBinding.inflate(layoutInflater, container, false)

        binding.placeName.text = "Trinidad Cuba"

        return binding.root

    }


}
