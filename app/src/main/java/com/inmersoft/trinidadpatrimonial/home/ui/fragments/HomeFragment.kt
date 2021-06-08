package com.inmersoft.trinidadpatrimonial.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.HomeFragmentBinding
import com.inmersoft.trinidadpatrimonial.details.ui.BottomSheet
import com.inmersoft.trinidadpatrimonial.home.ui.adapters.HomePlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    //TODO( Agregr DI )
    private val bottomSheet by lazy { BottomSheet() }

    private lateinit var binding: HomeFragmentBinding

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    private val mainAdapter by lazy { HomePlaceTypeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        binding.fab.setOnClickListener {

            bottomSheet.show(requireActivity().supportFragmentManager, "TrinidadDetailsBottomSheet")

        }
        val recycleTestView: RecyclerView = binding.mainRecycleview
        recycleTestView.adapter = mainAdapter

        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(
            viewLifecycleOwner,
            { placeTypeWithPlacesList ->
                mainAdapter.setData(placeTypeWithPlacesList)
            })

        return binding.root
    }


}