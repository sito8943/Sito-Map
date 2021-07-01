package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.databinding.FragmentDetailsBinding
import com.inmersoft.trinidadpatrimonial.ui.trinidad.details.adapters.DetailsTransformer
import com.inmersoft.trinidadpatrimonial.ui.trinidad.details.adapters.ViewPagerDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val safeArgs: DetailsFragmentArgs by navArgs()
    private val viewPagerAdapter: ViewPagerDetailAdapter by lazy {
        ViewPagerDetailAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        setupUI()

        return binding.root
    }

    fun setupUI() {
        binding.detailViewPager2Content.adapter = viewPagerAdapter
        binding.detailViewPager2Content.setPageTransformer(DetailsTransformer(50))
    }

    override fun onStart() {
        super.onStart()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { allPlaces ->
            val fragmentList = mutableListOf<PlaceDetailFragment>()
            allPlaces.indices.forEach { index ->
                val currentPlace = allPlaces[index]
                if (safeArgs.placeID == currentPlace.place_id) {
                    //Agregamos el lugar elejido por el usuario como primero en la lista
                    fragmentList.add(0, PlaceDetailFragment(currentPlace))
                } else {
                    fragmentList.add(PlaceDetailFragment(currentPlace))
                }
            }
            viewPagerAdapter.setFragments(fragmentList)
        })
    }
}
