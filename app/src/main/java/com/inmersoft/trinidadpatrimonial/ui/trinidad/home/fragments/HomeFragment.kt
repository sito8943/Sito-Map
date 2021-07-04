package com.inmersoft.trinidadpatrimonial.ui.trinidad.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentHomeBinding
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters.HomeListAdapter
import com.inmersoft.trinidadpatrimonial.utils.fadeTransition
import com.inmersoft.trinidadpatrimonial.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeListAdapter: HomeListAdapter by lazy {
        HomeListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setupUI()

        return binding.root
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
            showToast(requireContext(), "NOT IMPLEMENTED YET!!!")
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
            fadeTransition(binding.root as ViewGroup)
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

