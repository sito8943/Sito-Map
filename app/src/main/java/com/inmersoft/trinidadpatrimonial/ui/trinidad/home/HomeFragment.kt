package com.inmersoft.trinidadpatrimonial.ui.trinidad.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentHomeBinding
import com.inmersoft.trinidadpatrimonial.extensions.fadeTransitionExt
import com.inmersoft.trinidadpatrimonial.extensions.invisible
import com.inmersoft.trinidadpatrimonial.extensions.showToastExt
import com.inmersoft.trinidadpatrimonial.extensions.visible
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.HomeListAdapter
import com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.MainPlaceAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment(), MainPlaceAdapter.PlaceItemOnClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeListAdapter: HomeListAdapter by lazy {
        HomeListAdapter(placeItemOnClick = this@HomeFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.toolbar.menu.findItem(R.id.action_search)
            .setOnMenuItemClickListener {
                Log.d("TAG", "onCreateView: CLICKED")
                true
            }
        binding.toolbar.setNavigationOnClickListener {
            openDrawerInTrinidadActivity()
        }


        binding.fab.setOnClickListener {
            requireContext().showToastExt("NOT IMPLEMENTED YET!!!")
        }

        binding.homeListRecycleview.layoutManager = LinearLayoutManager(requireContext())
        binding.homeListRecycleview.adapter = homeListAdapter


        //Active the marquee text
        binding.trinidadDesctiptionTxt.isSelected = true

        trinidadDataViewModel.onLoadMainRecycleViewData()
    }

    private fun subscribeObservers() {
        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(
            viewLifecycleOwner,
            { placeTypeWithPlacesList ->
                homeListAdapter.submitList(placeTypeWithPlacesList)
            }
        )

        trinidadDataViewModel.showProgressLoading.observe(viewLifecycleOwner, { visibility ->
            (binding.root as ViewGroup).fadeTransitionExt()
            if (visibility) {
                binding.loadingData.visible()
                binding.homeListRecycleview.invisible()
                binding.homeRoutesRecycleview.invisible()
            } else {
                binding.loadingData.invisible()
                binding.homeListRecycleview.visible()
                binding.homeRoutesRecycleview.visible()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        subscribeObservers()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun showPlaceDetails(placeId: Int, sharedTransitionView: View) {
        val extras =
            FragmentNavigatorExtras(
                sharedTransitionView to "shared_view_container"
            )
        val action =
            HomeFragmentDirections.actionNavHomeToDetailsFragment(placeID = placeId)
        Navigation.findNavController(requireView())
            .navigate(action, extras)
    }
}

