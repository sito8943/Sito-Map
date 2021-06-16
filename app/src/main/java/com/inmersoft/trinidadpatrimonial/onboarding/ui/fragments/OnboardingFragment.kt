package com.inmersoft.trinidadpatrimonial.onboarding.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentOnboardingBinding
import com.inmersoft.trinidadpatrimonial.onboarding.data.OnBoardingData
import com.inmersoft.trinidadpatrimonial.onboarding.ui.adapters.OnBoardingAdapter
import com.inmersoft.trinidadpatrimonial.onboarding.ui.transformer.OnboardingViewPagerTransformer

class OnboardingFragment : Fragment() {

    lateinit var binding: FragmentOnboardingBinding

    private val onboardingAdapter by lazy {
        OnBoardingAdapter(
            listOf(
                OnBoardingData(
                    resources.getString(R.string.onboarding_title_page1),
                    resources.getString(R.string.onboarding_subtitle_page1),
                    R.drawable.ic_onboarding_page_1
                ), OnBoardingData(
                    resources.getString(R.string.onboarding_title_page2),
                    resources.getString(R.string.onboarding_subtitle_page2),
                    R.drawable.ic_onboarding_page_2
                ), OnBoardingData(
                    resources.getString(R.string.onboarding_title_page3),
                    resources.getString(R.string.onboarding_subtitle_page3),
                    R.drawable.ic_onboarding_page_3
                )
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentOnboardingBinding.inflate(inflater, container, false)
        binding.onboardingStartButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_nav_home)
        }

        binding.onboardingViewPage.adapter = onboardingAdapter

        binding.onboardingViewPage.setPageTransformer(OnboardingViewPagerTransformer())

        return binding.root
    }

}