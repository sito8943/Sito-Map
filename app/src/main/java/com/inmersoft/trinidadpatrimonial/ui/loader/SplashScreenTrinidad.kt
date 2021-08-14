package com.inmersoft.trinidadpatrimonial.ui.loader

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.ui.loader.ui.theme.TrinidadPatrimonialTheme
import com.inmersoft.trinidadpatrimonial.ui.onboarding.OnBoardingActivity
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreenTrinidad : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrinidadPatrimonialTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = colorResource(R.color.trinidadColorPrimary),
                    modifier = Modifier.fillMaxHeight()) {
                    SplashScreenContainer()
                }
            }
        }
    }
}

@Composable
fun SplashScreenContainer() {
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
        delay(2000L)
        context.startActivity(Intent(context, OnBoardingActivity::class.java))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxHeight().fillMaxWidth(),
    )
    {
        Image(
            painter = painterResource(R.drawable.trinidad_logo),
            contentDescription = "Trinidad Logo",
            modifier = Modifier.scale(scale = scale.value)
        )
    }
}
