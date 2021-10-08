package com.inmersoft.trinidadpatrimonial.ui.trinidad.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.inmersoft.ecommerce.presentation.EcommerceActivity
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentHomeBinding
import com.inmersoft.trinidadpatrimonial.extensions.fadeTransitionExt
import com.inmersoft.trinidadpatrimonial.extensions.invisible
import com.inmersoft.trinidadpatrimonial.extensions.showToastExt
import com.inmersoft.trinidadpatrimonial.extensions.visible
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.places.HomePlaceTypeListAdapter
import com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.places.InnerPlaceSubListAdapter
import com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.routes.HomeRouteListAdapter
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalAnimationApi
@ExperimentalFoundationApi
@AndroidEntryPoint
class HomeFragment : BaseFragment(), InnerPlaceSubListAdapter.PlaceItemOnClick,
    HomeRouteListAdapter.RouteItemOnClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homePlaceTypeListAdapter: HomePlaceTypeListAdapter by lazy {
        HomePlaceTypeListAdapter(placeSubListItemOnClick = this@HomeFragment)
    }
    private val homeRoutesListAdapter: HomeRouteListAdapter by lazy {
        HomeRouteListAdapter(routeItemOnClick = this@HomeFragment)
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

        binding.toolbar.menu.findItem(R.id.action_ecommerce)
            .setOnMenuItemClickListener {
                val ecomerce = Intent(requireContext(), EcommerceActivity::class.java)
                startActivity(ecomerce)
                true
            }
        binding.toolbar.setNavigationOnClickListener {
            openDrawerInTrinidadActivity()
        }


        binding.fab.setOnClickListener {
            requireContext().showToastExt("SIN IMPLEMENTAR!!!")
        }

        binding.homeListRecycleview.layoutManager = LinearLayoutManager(requireContext())
        binding.homeListRecycleview.adapter = homePlaceTypeListAdapter

        //RecycleView for routes
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.homeRoutesRecycleview)

        binding.homeRoutesRecycleview.adapter = homeRoutesListAdapter


        //Active the marquee text
        binding.trinidadDesctiptionTxt.isSelected = true

        trinidadDataViewModel.onLoadMainRecycleViewData()
    }

    private fun subscribeObservers() {
        trinidadDataViewModel.allPlaceTypeWithPlaces.observe(
            viewLifecycleOwner,
            { placeTypeWithPlacesList ->
                homePlaceTypeListAdapter.submitList(placeTypeWithPlacesList)
            }
        )

        trinidadDataViewModel.allRoutes.observe(
            viewLifecycleOwner,
            { allRoutes ->
                homeRoutesListAdapter.submitList(allRoutes)
            }
        )

        trinidadDataViewModel.showProgressLoading.observe(viewLifecycleOwner, { visibility ->
            (binding.root as ViewGroup).fadeTransitionExt()
            if (visibility) {
                binding.loadingData.visible()
                binding.homeListRecycleview.invisible()
                binding.materialCardviewRoutes.invisible()
            } else {
                binding.loadingData.invisible()
                binding.homeListRecycleview.visible()
                binding.materialCardviewRoutes.visible()
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

    override fun showRouteDetails(routeId: Int, sharedTransitionView: View) {
        val extras =
            FragmentNavigatorExtras(
                sharedTransitionView to "shared_view_container"
            )
        val action =
            HomeFragmentDirections.actionNavHomeToRoutesContainerDetailsFragment(routeId)
        Navigation.findNavController(requireView())
            .navigate(action, extras)
    }
}

