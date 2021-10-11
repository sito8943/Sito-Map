package com.inmersoft.trinidadpatrimonial.composables

import android.net.Uri
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
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

    val sampleVideo = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    val context = LocalContext.current
    val player = SimpleExoPlayer.Builder(context).build()
    val playerView = PlayerView(context)
    val mediaItem = MediaItem.fromUri(sampleVideo)
    val playWhenReady by remember {
        mutableStateOf(true)
    }
    player.setMediaItem(mediaItem)
    playerView.player = player
    LaunchedEffect(player) {
        player.prepare()
        player.playWhenReady = playWhenReady
    }
    AndroidView(factory = {
        playerView
    })

}