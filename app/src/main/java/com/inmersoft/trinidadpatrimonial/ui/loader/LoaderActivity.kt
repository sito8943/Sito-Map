package com.inmersoft.trinidadpatrimonial.ui.loader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.ui.onboarding.OnBoardingActivity
import com.inmersoft.trinidadpatrimonial.ui.trinidad.TrinidadActivity
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoaderActivity : AppCompatActivity() {

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)

        checkFirstRun()


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

    private fun checkFirstRun() {
        trinidadDataViewModel.userPreferences.observe(this, { userPref ->
            if (userPref != null) {
                if (!userPref.userSeeOnboarding) {
                    Log.d("TAG", "checkFirstRun: USER NOT SEE ONBOARDING")
                    populateDataBase()
                    startOnBoardingPage()
                } else {
                    Log.d("TAG", "checkFirstRun: USER SEE ONBOARDING")
                    startTrinidadPage()
                }
            } else {
                startOnBoardingPage()
            }
        })

    }

    private fun startTrinidadPage() {
        val intent = Intent(this@LoaderActivity, TrinidadActivity::class.java)
        startActivity(intent)
    }

    private fun startOnBoardingPage() {
        val intent = Intent(this@LoaderActivity, OnBoardingActivity::class.java)
        startActivity(intent)
    }
}