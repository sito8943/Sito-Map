package com.inmersoft.ecommerce.common

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

object WebUtils {
    fun openWeb(ctx: Context, web: Uri, color: Int) {
        val builder = CustomTabsIntent.Builder();
        val colorInt: Int = color
        val customColor = CustomTabColorSchemeParams.Builder().setToolbarColor(colorInt)
        builder.setDefaultColorSchemeParams(customColor.build())
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(ctx, web)
    }
}