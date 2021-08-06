package com.inmersoft.trinidadpatrimonial.ui.onboarding

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.size
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.ActivityOnBoardingBinding
import com.inmersoft.trinidadpatrimonial.extensions.fadeTransitionExt
import com.inmersoft.trinidadpatrimonial.extensions.invisible
import com.inmersoft.trinidadpatrimonial.extensions.visible
import com.inmersoft.trinidadpatrimonial.preferences.UserPreferences
import com.inmersoft.trinidadpatrimonial.preferences.UserPreferencesRepository
import com.inmersoft.trinidadpatrimonial.ui.onboarding.adapters.OnBoardingAdapter
import com.inmersoft.trinidadpatrimonial.ui.onboarding.adapters.OnboardingViewPagerTransformer
import com.inmersoft.trinidadpatrimonial.ui.onboarding.data.OnBoardingData
import com.inmersoft.trinidadpatrimonial.ui.trinidad.TrinidadActivity
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        super.onCreate(savedInstanceState)

        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)

        binding.onboardingStartButton.setOnClickListener {
            trinidadDataViewModel.onSetUserPreferences(UserPreferences(userSeeOnboarding = true))
            goTrinidadHome(binding.onboardingStartButton)
        }

        binding.onboardingViewPage.adapter = onboardingAdapter

        binding.onboardingViewPage.setPageTransformer(OnboardingViewPagerTransformer())

        populateOnboardingPoints()

        setOnboardingPoint(0)

        binding.onboardingViewPage.registerOnPageChangeCallback(viewPager2PageChangeCallback)

        setContentView(binding.root)

    }

    private fun goTrinidadHome(viewShared: View?) {
        val intent = Intent(this, TrinidadActivity::class.java)
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            viewShared,
            "shared_element_container" // The transition name to be matched in Activity B.
        )
        startActivity(intent, options.toBundle())
        finishAfterTransition()
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
            binding.container.fadeTransitionExt()
            binding.onboardingPagePositionContainer.invisible()
            binding.onboardingStartButton.visible()
        } else {
            binding.container.fadeTransitionExt()
            if (binding.onboardingPagePositionContainer.isInvisible) {
                binding.onboardingPagePositionContainer.visible()
            }
            binding.onboardingStartButton.invisible()
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