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
import androidx.core.view.size
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
import com.inmersoft.trinidadpatrimonial.ui.onboarding.adapters.OnBoardingAdapter
import com.inmersoft.trinidadpatrimonial.ui.onboarding.adapters.OnboardingViewPagerTransformer
import com.inmersoft.trinidadpatrimonial.ui.onboarding.data.OnBoardingData
import com.inmersoft.trinidadpatrimonial.ui.trinidad.TrinidadActivity
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {

    companion object {
        private const val UPDATE_REQUEST_CODE = 7856
    }

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

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

        inAppUpdate()

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

    private fun inAppUpdate() {
        appUpdateManager.registerListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) {
                showUpdateDownloadedSnackbar()
            }
        }

        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.FLEXIBLE,
                    this,
                    UPDATE_REQUEST_CODE
                )
            }
        }.addOnFailureListener {
            Log.e("FlexibleUpdateActivity", "Failed to check for update: $it")
        }
    }

    private fun showUpdateDownloadedSnackbar() {
        Snackbar.make(
            binding.root,
            resources.getString(R.string.update_downloaded),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(resources.getString(R.string.install_update)) { appUpdateManager.completeUpdate() }
            .show()
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.installStatus() == InstallStatus.DOWNLOADED) {
                showUpdateDownloadedSnackbar()
            }
        }
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
            binding.onboardingPagePositionContainer.visibility = View.INVISIBLE
            binding.onboardingStartButton.visibility = View.VISIBLE
        } else {
            binding.container.fadeTransitionExt()
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