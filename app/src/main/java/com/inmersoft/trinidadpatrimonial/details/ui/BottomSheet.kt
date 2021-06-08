package com.inmersoft.trinidadpatrimonial.details.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.LayoutBottomSheetBinding
import com.inmersoft.trinidadpatrimonial.details.ui.adapter.ViewPagerDetailAdapter
import com.inmersoft.trinidadpatrimonial.details.ui.adapter.ViewPagerDetailFragment


class BottomSheet(
    private val listOfPagesDetails: List<ViewPagerDetailFragment>
) : SuperBottomSheetFragment() {
    private lateinit var binding: LayoutBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = LayoutBottomSheetBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationIcon(R.drawable.ic_outline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener {
            this@BottomSheet.dismiss()
        }


        val detailsAdapter =
            ViewPagerDetailAdapter(
                listOfPagesDetails,
                requireActivity().supportFragmentManager,
                lifecycle
            )

        binding.detailsContent.adapter = detailsAdapter

        return binding.root
    }

    override fun animateStatusBar(): Boolean {
        return true
    }

    override fun getCornerRadius() =
        requireContext().resources.getDimension(R.dimen.sheet_rounded_corner)

    override fun getStatusBarColor() =
        resources.getColor(R.color.trinidadColorPrimary)

    override fun onStart() {
        super.onStart()
        Log.d("BOTTOM_SHEET", "onStart: START BOTTOM SHEET FRAGMENT ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("BOTTOM_SHEET", "onStop: STOP BOTTOM SHEET FRAGMENT ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BOTTOM_SHEET", "onDestroy: DESTROY BOTTOM SHEET FRAGMENT ")
    }
}