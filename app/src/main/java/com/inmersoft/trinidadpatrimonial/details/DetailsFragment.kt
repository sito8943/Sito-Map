package com.inmersoft.trinidadpatrimonial.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.inmersoft.trinidadpatrimonial.databinding.DetailsFragmentBinding
import com.inmersoft.trinidadpatrimonial.details.places.ui.adapter.ViewPagerDetailAdapter
import com.inmersoft.trinidadpatrimonial.details.places.ui.fragments.PlaceDetailFragment
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    lateinit var binding: DetailsFragmentBinding
    private val trinidadDataViewModel: TrinidadDataViewModel by activityViewModels()
    val safeArgs: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postponeEnterTransition()
        binding = DetailsFragmentBinding.inflate(inflater, container, false)

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

            val adapter =
                ViewPagerDetailAdapter(
                    fragmentList,
                    requireActivity().supportFragmentManager,
                    lifecycle
                )

            binding.detailViewPager2Content.adapter = adapter
            binding.detailViewPager2Content.setPageTransformer(DetailsTransformer(50))

            (view?.parent as? ViewGroup)?.doOnPreDraw {
                // Parent has been drawn. Start transitioning!
                startPostponedEnterTransition()
            }

        })

        return binding.root
    }


}