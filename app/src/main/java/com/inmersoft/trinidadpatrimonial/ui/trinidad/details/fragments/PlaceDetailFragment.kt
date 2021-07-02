package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.fragments

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.FragmentPlaceDetailsBinding
import com.inmersoft.trinidadpatrimonial.utils.ShareIntent
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.TrinidadCustomChromeTab
import com.inmersoft.trinidadpatrimonial.utils.showToast
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.util.*


class PlaceDetailFragment(private val placeData: Place) : Fragment(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var binding: FragmentPlaceDetailsBinding
    private var currentLocale = Locale("es", "ES")

    private lateinit var playerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    var currentWindows = 0
    var playbackPosition = 0L
    var playWhenReady = false

    private lateinit var textToSpeechEngine: TextToSpeech


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textToSpeechEngine=TextToSpeech(requireActivity()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                if (textToSpeechEngine.isLanguageAvailable(
                        currentLocale
                    ) == TextToSpeech.LANG_AVAILABLE
                ) {

                    textToSpeechEngine.language = currentLocale
                } else {
                    showToast(requireContext(), resources.getString(R.string.lang_not_supported))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaceDetailsBinding.inflate(layoutInflater, container, false)
        binding.toolbarLayout.title = placeData.place_name

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        loadPano360(placeData.pano)
        loadHeader(placeData.header_images)

        binding.placeDescription.text = placeData.place_description
        binding.placeName.text = placeData.place_name

        binding.btnGoToMap.apply {
            transitionName = UUID.randomUUID().toString()
            setOnClickListener {
                goToMap(placeData.place_id)
            }
        }
        binding.btnSpeechDescription.setOnClickListener {
            if (it.isSelected) {
                textToSpeechEngine.stop()
                it.isSelected = false
            } else {
                speechPlaceDescription(placeData.place_description)
                it.isSelected = true
            }
        }

        binding.btnGoWebPage.setOnClickListener {
            goToWebPage(placeData.web)
        }
        binding.btnSharePlaceInformation.setOnClickListener {
            sharePlaceInformation()
        }

        initPlayer()
        return binding.root
    }

    private fun initPlayer() {

        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        playerView = binding.videoPlayer
        binding.videoPlayer.player = simpleExoPlayer


        playYoutubeUrl(placeData.video_promo)

    }

    private fun playYoutubeUrl(videoPromo: String) {
        object : YouTubeExtractor(requireContext()) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles != null) {
                    val videoTag = 137 //Tag 1080
                    val audioTag = 140 //Tag 1080
                    val audioSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(ytFiles.get(audioTag).url))
                    val videoSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).url))

                    simpleExoPlayer.setMediaSource(
                        MergingMediaSource(
                            true,
                            videoSource,
                            audioSource
                        ),
                        true
                    )
                    simpleExoPlayer.prepare()
                    simpleExoPlayer.playWhenReady = false
                    simpleExoPlayer.seekTo(currentWindows, playbackPosition)

                }
            }

        }.extract(videoPromo, false, true)
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onStop() {
        releasePlayer()
        super.onStop()

    }

    override fun onResume() {
        super.onResume()
        if (simpleExoPlayer == null) {
            initPlayer()
        }
    }

    private fun releasePlayer() {
        if (playerView != null) {
            playWhenReady = simpleExoPlayer.playWhenReady
            playbackPosition = simpleExoPlayer.currentPosition
            currentWindows = simpleExoPlayer.currentWindowIndex
            simpleExoPlayer.release()
        }
    }

    private fun loadHeader(headerImages: List<String>) {
        Glide.with(requireContext())
            .load(
                Uri.parse(
                    TrinidadAssets.getAssetFullPath(
                        headerImages[0],
                        TrinidadAssets.FILE_JPG_EXTENSION
                    )
                )
            )
            .error(R.drawable.placeholder_error)
            .placeholder(R.drawable.placeholder_error)
            .into(binding.headerImage)
    }

    private fun goToWebPage(web: String) {
        TrinidadCustomChromeTab.launch(requireContext(), Uri.parse(web))
    }

    private fun sharePlaceInformation() {
        if (!hasWriteExternalPermission()) {
            requestWriteExternalPermission()
        } else {
            sharePlaceInfo()
        }
    }

    private fun sharePlaceInfo() {

        ShareIntent.loadImageAndShare(
            requireContext(), Uri.parse(
                TrinidadAssets.getAssetFullPath(
                    placeData.header_images[0],
                    TrinidadAssets.FILE_JPG_EXTENSION
                )
            ), placeData.place_name, resources.getString(R.string.app_name)
        )

    }

    private fun speechPlaceDescription(placeDescription: String) {
        if (placeDescription.isNotEmpty()) {
            textToSpeechEngine.speak(placeDescription, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        }
    }


    private fun goToMap(placeId: Int) {

        binding.placeDetailContainer.transitionName=UUID.randomUUID().toString()

        val extras =
            FragmentNavigatorExtras(
                binding.placeDetailContainer to "bottom_sheet_trinidad"
            )
        val action =
            DetailsFragmentDirections.actionDetailsFragmentToNavMap(placeID = placeId)
        findNavController().navigate(action,extras)
    }



    override fun onPause() {
        textToSpeechEngine.stop()
        binding.btnSpeechDescription.isChecked = false
        releasePlayer()
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeechEngine.shutdown()
        binding.btnSpeechDescription.isChecked = false
        super.onDestroy()
    }
    private fun loadPano360(panoAssetName: List<String>) {
        if (panoAssetName[0].isNotEmpty()) {
            val panoAssetUrl = TrinidadAssets.getAssetFullPath(
                panoAssetName[0],
                TrinidadAssets.FILE_WEBP_EXTENSION
            )
            Glide.with(requireActivity())
                .asBitmap()
                .placeholder(R.drawable.placeholder_error)
                .load(Uri.parse(panoAssetUrl))
                .listener(object : RequestListener<Bitmap?> {

                    private fun loadingDone(done: Boolean) {
                        binding.materialPanoContainer.visibility =
                            if (done) View.VISIBLE else View.GONE
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadingDone(false)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadingDone(true)
                        return false
                    }
                })
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val options = VrPanoramaView.Options()
                        options.inputType = VrPanoramaView.Options.TYPE_MONO
                        binding.run {
                            options.inputType = VrPanoramaView.Options.TYPE_MONO
                            placePanoView.setInfoButtonEnabled(false)
                            placePanoView.setFullscreenButtonEnabled(true)
                            placePanoView.setStereoModeButtonEnabled(true)
                            placePanoView.setTouchTrackingEnabled(true)
                            placePanoView.loadImageFromBitmap(resource, options)

                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Add other icon
                    }
                })
        } else {
            binding.materialPanoContainer.visibility = View.GONE
        }
    }

//WRITE EXTERNAL STORAGE PERMISSIONS

    private fun hasWriteExternalPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun requestWriteExternalPermission() {
        EasyPermissions.requestPermissions(
            this,
            "La aplicación Trinidad Patrimonial necesita permisos  para compartir su " +
                    "contenido con otras aplicaciones.\n " +
                    "La información compartida no se corresponde" +
                    " a su información personal, sino a la informacion contenida " +
                    "en la propia base de datos de la aplicación Trinidad Patrimonial" +
                    " y que hace referencia al contenido tratado por la aplicación; La Ciudad de Trinidad.",
            WRITE_EXTERNAL_PERMISSION_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestWriteExternalPermission()
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

        if (requestCode == WRITE_EXTERNAL_PERMISSION_CODE) {
            sharePlaceInfo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }
    companion object {
        const val WRITE_EXTERNAL_PERMISSION_CODE = 5637
    }

}
