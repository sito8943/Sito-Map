package com.inmersoft.trinidadpatrimonial.composables

import android.net.Uri
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.ui.PlayerView
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.inmersoft.trinidadpatrimonial.extensions.loadPano360WithGlideInComposeExt


@Composable
fun ComposePanoView(context: FragmentActivity, panoUri: Uri, modifier: Modifier = Modifier) {
    AndroidView(
        {
            VrPanoramaView(context).apply {
                loadPano360WithGlideInComposeExt(panoUri)
            }
        },
        modifier = modifier // Occupy the max size in the Compose UI tree
    )
}

@Composable
fun ComposeExoPlayer() {
    AndroidView(
        {
            PlayerView(it)
        }
    )
}