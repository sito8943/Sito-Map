package com.inmersoft.trinidadpatrimonial.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.inmersoft.trinidadpatrimonial.R

object TrinidadCustomChromeTab {
    fun launch(ctx: Context, web: String) {
        val builder = CustomTabsIntent.Builder();
        val customTabsIntent = builder.build()
        val colorInt: Int = ctx.resources.getColor(R.color.trinidadColorPrimary)
        builder.setToolbarColor(colorInt)
        customTabsIntent.launchUrl(ctx, Uri.parse(web))
    }
}