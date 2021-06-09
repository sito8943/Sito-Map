package com.inmersoft.trinidadpatrimonial.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.HomeFragmentBinding
import com.inmersoft.trinidadpatrimonial.details.bottomsheet.BottomSheet
import com.inmersoft.trinidadpatrimonial.details.ui.fragments.ViewPagerDetailFragment
import com.inmersoft.trinidadpatrimonial.home.ui.adapters.HomePlaceTypeAdapter
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    private val trinidadDataViewModel: TrinidadDataViewModel by activityViewModels()

    private val mainAdapter by lazy { HomePlaceTypeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }


        binding.fab.setOnClickListener {

            val listDetails = listOf(
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
            )
            val bottomSheet = BottomSheet(listDetails)
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