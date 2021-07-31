package com.inmersoft.trinidadpatrimonial.ui.onboarding.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.inmersoft.trinidadpatrimonial.R

class OnboardingViewPagerTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val onboardingTitle = page.findViewById<TextView>(R.id.title)
        val onboardingSubTitle = page.findViewById<TextView>(R.id.subtitle)
        val imageOnboarding = page.findViewById<ImageView>(R.id.image_view_parallax_effect)

        val translation = position * (pageWidth / 2)
        imageOnboarding.translationX = -translation
        onboardingTitle.translationX = translation
        onboardingSubTitle.translationX = translation

    }
}