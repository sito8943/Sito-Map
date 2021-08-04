package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.inmersoft.trinidadpatrimonial.databinding.FragmentDetailsBinding
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.details.common.DetailsTransformer
import com.inmersoft.trinidadpatrimonial.ui.trinidad.details.common.ViewPagerDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceContainerDetailsFragment : BaseFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    private val safeArgs: PlaceContainerDetailsFragmentArgs by navArgs()
    private val viewPagerAdapter: ViewPagerDetailAdapter by lazy {
        ViewPagerDetailAdapter(
            childFragmentManager,
            lifecycle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.detailViewPager2Content.adapter = viewPagerAdapter
        binding.detailViewPager2Content.setPageTransformer(DetailsTransformer(50))
    }

    override fun onStart() {
        super.onStart()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { allPlaces ->
            val fragmentList = mutableListOf<PlaceDetailsFragment>()
            allPlaces.indices.forEach { index ->
                val currentPlace = allPlaces[index]
                if (safeArgs.placeID == currentPlace.place_id) {
                    //Agregamos el lugar elejido por el usuario como primero en la lista
                    fragmentList.add(0, PlaceDetailsFragment(currentPlace))
                } else {
                    fragmentList.add(PlaceDetailsFragment(currentPlace))
                }
            }
            viewPagerAdapter.setFragments(fragmentList)
        })
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
