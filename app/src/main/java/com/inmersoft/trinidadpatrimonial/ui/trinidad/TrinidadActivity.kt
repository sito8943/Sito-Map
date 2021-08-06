package com.inmersoft.trinidadpatrimonial.ui.trinidad

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.inmersoft.trinidadpatrimonial.BuildConfig
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.ActivityTrinidadBinding
import com.inmersoft.trinidadpatrimonial.preferences.UserPreferences
import com.inmersoft.trinidadpatrimonial.ui.onboarding.OnBoardingActivity
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Intent


@AndroidEntryPoint
class TrinidadActivity : AppCompatActivity() {

    companion object {
        private const val UPDATE_REQUEST_CODE = 7856
    }

    private lateinit var binding: ActivityTrinidadBinding

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {


        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = "shared_element_container"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform(application, true).apply {
            addTarget(android.R.id.content)
            duration = 250L
        }
        window.sharedElementReturnTransition =
            MaterialContainerTransform(application, false).apply {
                addTarget(android.R.id.content)
                duration = 250L
            }
        setTheme(R.style.Theme_TrinidadPatrimonial)
        super.onCreate(savedInstanceState)
        checkFirstRun()

        binding = ActivityTrinidadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inAppUpdate()
        initUI()
    }

    private fun checkFirstRun() {
        trinidadDataViewModel.userPreferences.observe(this, { userPref ->
            if (userPref != null) {
                if (!userPref.userSeeOnboarding) {
                    Log.d("TAG", "checkFirstRun: USER NOT SEE ONBOARDING")
                    populateDataBase()
                    startOnBoardingPage()
                } else {
                    Log.d("TAG", "checkFirstRun: USER SEE ONBOARDING")
                }
            } else {
                startOnBoardingPage()
            }
        })

    }

    private fun populateDataBase() {
        trinidadDataViewModel.allPlacesName.observe(this, {
            var message = "Is not ready...Populating..."
            if (it.isNotEmpty()) {
                message = "Is Ready"
            }
            Log.d("DATABASE_POPULATE", "initDataBase: DATABASE: READY: $message")
        })
    }

    private fun startOnBoardingPage() {
        val intent = Intent(this@TrinidadActivity, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initUI() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
        setAppBarTranslucent()

        binding.footerTrinidadVersion.text = "Trinidad v${BuildConfig.VERSION_NAME}"

    }


    private fun setAppBarTranslucent() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        setWindowFlag(
            (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION), false
        )
        window.statusBarColor = resources.getColor(R.color.statusBarTranslucent)
        window.navigationBarColor = Color.TRANSPARENT
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win: Window = window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    fun openCloseNavigationDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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


}