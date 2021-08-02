package com.inmersoft.trinidadpatrimonial.ui.trinidad.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentHomeBinding
import com.inmersoft.trinidadpatrimonial.extensions.fadeTransitionExt
import com.inmersoft.trinidadpatrimonial.extensions.showToastExt
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.HomeListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeListAdapter: HomeListAdapter by lazy {
        HomeListAdapter()
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
                homeListAdapter.setData(placeTypeWithPlacesList)
            }
        )

        trinidadDataViewModel.showProgressLoading.observe(viewLifecycleOwner, { visibility ->
            (binding.root as ViewGroup).fadeTransitionExt()
            binding.loadingData.visibility = if (visibility) View.VISIBLE else View.INVISIBLE
            binding.homeListRecycleview.visibility =
                if (!visibility) View.VISIBLE else View.INVISIBLE
        })
    }

    override fun onStart() {
        super.onStart()
        subscribeObservers()
    }
}

