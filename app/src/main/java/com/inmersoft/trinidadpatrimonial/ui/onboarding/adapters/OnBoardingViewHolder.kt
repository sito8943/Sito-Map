package com.inmersoft.trinidadpatrimonial.ui.onboarding.adapters

import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.databinding.FragmentOnboardingScreenBinding
import com.inmersoft.trinidadpatrimonial.extensions.loadImageWithGlideExt
import com.inmersoft.trinidadpatrimonial.ui.onboarding.data.OnBoardingData

class OnBoardingViewHolder(private val binding: FragmentOnboardingScreenBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(currenOnboardingScreen: OnBoardingData) {
        binding.imageViewParallaxEffect.loadImageWithGlideExt(currenOnboardingScreen.imageResource)
        binding.title.text = currenOnboardingScreen.title
        binding.subtitle.text = currenOnboardingScreen.subtitle
    }

}