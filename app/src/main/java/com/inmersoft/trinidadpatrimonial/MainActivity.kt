package com.inmersoft.trinidadpatrimonial

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inmersoft.trinidadpatrimonial.core.data.AppDatabase
import com.inmersoft.trinidadpatrimonial.databinding.ActivityMainBinding
import com.inmersoft.trinidadpatrimonial.details.ui.BottomSheet
import com.inmersoft.trinidadpatrimonial.details.ui.adapter.ViewPagerDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TrinidadPatrimonial)
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        initUi()

    }

    private fun initUi() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host)
        navView.setupWithNavController(navController)
    setAppBarTranslucent()
    }

    private fun setAppBarTranslucent() {
        if (Build.VERSION.SDK_INT >= 21) {
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


}