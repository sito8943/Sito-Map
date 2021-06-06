package com.inmersoft.trinidadpatrimonial.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFadeThrough
import com.inmersoft.trinidadpatrimonial.databinding.HomeFragmentBinding
import com.inmersoft.trinidadpatrimonial.home.ui.adapters.HomePlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.home.ui.viewmodels.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    private val homeViewModel: HomeFragmentViewModel by viewModels()

    private val mainAdapter = HomePlaceTypeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        val recycleTestView: RecyclerView = binding.mainRecycleview
        recycleTestView.adapter = mainAdapter

        homeViewModel.allPlaceTypeWithRoutes.observe(
            viewLifecycleOwner,
            { placeTypeWithPlacesList ->
                mainAdapter.setData(placeTypeWithPlacesList)
            })

        return binding.root
    }


}