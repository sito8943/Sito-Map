package com.inmersoft.trinidadpatrimonial.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)

        //TODO cojer del argumento el id del elemento
        //TODO y el tipo de lugar que es o sea si es ruta o lugar
        //TODO cojer tambien a que tipo pertenece para mostrar todos los typos en el carrousel

        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { allPlaces ->
            val fragmentList = mutableListOf<PlaceDetailFragment>()
            allPlaces.indices.forEach { index ->
                val currentPlace = allPlaces[index]
                if (safeArgs.placeID == currentPlace.place_id) {
                    //Agregamos el lugar elejido por el usuario como primero enla lista
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
            binding.detailViewPager2Content.setPageTransformer(DetailsTransformer(3))
        })
        return binding.root
    }


}