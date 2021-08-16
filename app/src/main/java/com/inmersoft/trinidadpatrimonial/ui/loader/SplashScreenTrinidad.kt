package com.inmersoft.trinidadpatrimonial.ui.loader

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.ui.loader.ui.theme.TrinidadPatrimonialTheme
import com.inmersoft.trinidadpatrimonial.ui.onboarding.OnBoardingActivity
import com.inmersoft.trinidadpatrimonial.ui.trinidad.TrinidadActivity
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenTrinidad : AppCompatActivity() {

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrinidadPatrimonialTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = colorResource(R.color.trinidadColorPrimary),
                    modifier = Modifier.fillMaxHeight()) {
                    SplashScreenContainer(this@SplashScreenTrinidad, trinidadDataViewModel)
                }
            }
        }
    }
}

@Composable
fun SplashScreenContainer(
    lifecycleOwner: LifecycleOwner,
    trinidadDataViewModel: TrinidadDataViewModel,
) {
    val context = LocalContext.current

    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        trinidadDataViewModel.allPlacesName.observe(lifecycleOwner, {
            var message = "Is not ready...Populating..."
            if (it.isNotEmpty()) {
                message = "Is Ready"
            }
            Log.d("DATABASE_POPULATE", "initDataBase: DATABASE: READY: $message")
        })
        delay(2000L)
        trinidadDataViewModel.userPreferences.observe(lifecycleOwner, { userPref ->
            if (userPref != null) {
                if (!userPref.userSeeOnboarding) {
                    Log.d("TAG", "checkFirstRun: USER NOT SEE ONBOARDING")
                    startOnBoardingPage(context = context)

                } else {
                    Log.d("TAG", "checkFirstRun: USER SEE ONBOARDING")
                    startTrinidadPage(context = context)
                }
            } else {
                startOnBoardingPage(context = context)
            }
        })
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    )
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.trinidad_logo),
                contentDescription = "Trinidad Logo",
                modifier = Modifier.scale(scale = scale.value).align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.padding(4.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(120.dp)
                    .alpha(0.4f),
                color = colorResource(R.color.trinidadColorOnPrimary),
                backgroundColor = colorResource(R.color.background_progressbar_color)
            )
        }
    }
}

private fun startTrinidadPage(context: Context) {
    val intent = Intent(context, TrinidadActivity::class.java)
    context.startActivity(intent)
}

private fun startOnBoardingPage(context: Context) {
    val intent = Intent(context, OnBoardingActivity::class.java)
    context.startActivity(intent)
}

