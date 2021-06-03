package com.inmersoft.trinidadpatrimonial

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.inmersoft.trinidadpatrimonial.core.data.AppDatabase
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.data.entity.TypeTranslation
import com.inmersoft.trinidadpatrimonial.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TrinidadPatrimonial)
        super.onCreate(savedInstanceState)
        setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        initUi()

        val database = AppDatabase.getDatabase(this, this.lifecycleScope)

        database.placesDao().getAllPlaces().observe(this@MainActivity, { it ->
            it.forEach { placeItem ->
                Log.d("TAG", "DATABASE-TRINIDAD: ${placeItem.place_name} - ${placeItem.id}  ")
            }
        })

        lifecycleScope.launch(Dispatchers.IO) {
            val p = PlaceType(
                0, "url", Random.nextInt(100, 300).toString(), listOf<TypeTranslation>(
                    TypeTranslation("ex", "GAS")
                )
            )
            database.placesTypeDao().insert(p)
        }
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