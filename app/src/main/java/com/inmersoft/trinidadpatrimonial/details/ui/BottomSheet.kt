package com.inmersoft.trinidadpatrimonial.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.LayoutBottomSheetBinding
import com.inmersoft.trinidadpatrimonial.details.ui.adapter.ViewPagerDetailAdapter
import com.inmersoft.trinidadpatrimonial.details.ui.adapter.ViewPagerDetailFragment


class BottomSheet : SuperBottomSheetFragment() {
    private lateinit var binding: LayoutBottomSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = LayoutBottomSheetBinding.inflate(inflater, container, false)

        val detailsAdapter =
            ViewPagerDetailAdapter(listOf(
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
                ViewPagerDetailFragment(),
            ),requireActivity().supportFragmentManager, lifecycle)

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

}