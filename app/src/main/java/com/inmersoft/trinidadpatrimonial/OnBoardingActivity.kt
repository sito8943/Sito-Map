package com.inmersoft.trinidadpatrimonial

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.inmersoft.trinidadpatrimonial.databinding.ActivityOnBoardingBinding
import com.inmersoft.trinidadpatrimonial.onboarding.data.OnBoardingData
import com.inmersoft.trinidadpatrimonial.onboarding.ui.adapters.OnBoardingAdapter
import com.inmersoft.trinidadpatrimonial.onboarding.ui.transformer.OnboardingViewPagerTransformer
import com.inmersoft.trinidadpatrimonial.utils.fadeTransition
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    private lateinit var binding: ActivityOnBoardingBinding

    private val viewPager2PageChangeCallback = ViewPager2PageChangeCallback {
        setOnboardingPoint(it)
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TrinidadPatrimonial)

        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        // Attach a callback used to capture the shared elements from this Activity to be used
        // by the container transform transition
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        // Keep system bars (status bar, navigation bar) persistent throughout the transition.
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)


        binding.onboardingStartButton.setOnClickListener {


            val intent = Intent(this, TrinidadActivity::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                binding.onboardingStartButton,
                "shared_element_container" // The transition name to be matched in Activity B.
            )
            startActivity(intent, options.toBundle())


        }

        binding.onboardingViewPage.adapter = onboardingAdapter

        binding.onboardingViewPage.setPageTransformer(OnboardingViewPagerTransformer())

        populateOnboardingPoints()

        setOnboardingPoint(0)

        binding.onboardingViewPage.registerOnPageChangeCallback(viewPager2PageChangeCallback)

        trinidadDataViewModel.allPlacesName.observe(this, {
            var message = "Is not ready...Populating..."
            if (it.isNotEmpty()) {
                message = "Is Ready"
            }
            Log.d("DATABASE_POPULATE", "initDataBase: DATABASE: READY: $message")
        })


        setContentView(binding.root)
    }

    private fun setOnboardingPoint(index: Int) {
        val max = binding.onboardingPagePositionContainer.size
        (0 until max).forEach { currentPoint ->
            val imv = binding.onboardingPagePositionContainer.getChildAt(currentPoint) as ImageView
            if (currentPoint == index)
                imv.setImageResource(R.drawable.onboarding_item_selected)
            else
                imv.setImageResource(R.drawable.onboarding_item_unselected)
        }

        if (index == max - 1) {
            fadeTransition(binding.container)
            binding.onboardingPagePositionContainer.visibility = View.INVISIBLE
            binding.onboardingStartButton.visibility = View.VISIBLE
        } else {
            fadeTransition(binding.container)
            if (binding.onboardingPagePositionContainer.visibility == View.INVISIBLE) {
                binding.onboardingPagePositionContainer.visibility = View.VISIBLE
            }
            binding.onboardingStartButton.visibility = View.INVISIBLE
        }
    }

    private fun populateOnboardingPoints() {
        for (i in 0 until onboardingAdapter.itemCount) {
            val imv = ImageView(this)
            imv.setImageResource(R.drawable.onboarding_item_unselected)
            binding.onboardingPagePositionContainer.addView(imv)
        }
    }
}

class ViewPager2PageChangeCallback(private val listener: (Int) -> Unit) :
    ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        listener.invoke(position)
    }
}