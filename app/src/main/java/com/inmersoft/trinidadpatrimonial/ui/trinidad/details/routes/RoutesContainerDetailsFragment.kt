package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.inmersoft.trinidadpatrimonial.databinding.FragmentDetailsBinding
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.details.common.DetailsTransformer
import com.inmersoft.trinidadpatrimonial.ui.trinidad.details.common.ViewPagerDetailAdapter
import com.inmersoft.trinidadpatrimonial.ui.trinidad.details.place.fragments.PlaceDetailsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutesContainerDetailsFragment : BaseFragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: RoutesContainerDetailsFragmentArgs by navArgs()
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
        trinidadDataViewModel.allRoutes.observe(viewLifecycleOwner, { allRoutes ->
            val fragmentList = mutableListOf<RoutesDetailsFragment>()
            allRoutes.indices.forEach { index ->
                val currentRoute = allRoutes[index]
                if (safeArgs.routeID == currentRoute.route_id) {
                    //Agregamos la ruta elejida por el usuario com la primera en la lista
                    fragmentList.add(0, RoutesDetailsFragment(currentRoute))
                } else {
                    fragmentList.add(RoutesDetailsFragment(currentRoute))
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
