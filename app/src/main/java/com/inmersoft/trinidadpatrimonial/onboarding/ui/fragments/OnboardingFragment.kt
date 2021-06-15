package com.inmersoft.trinidadpatrimonial.onboarding.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentOnboardingBinding.inflate(inflater, container, false)
        binding.skipOnboarding.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_nav_home)
        }

        binding.viewPageWallpaper.adapter = wallpaperAdapter

        wallpaperViewModel.allWallpapers.observe(viewLifecycleOwner, { listChanges ->
            wallpaperAdapter.setData(listChanges)
        })


        binding.viewPageWallpaper.setPageTransformer(WallpaperTransformer())

        return binding.root
    }

}