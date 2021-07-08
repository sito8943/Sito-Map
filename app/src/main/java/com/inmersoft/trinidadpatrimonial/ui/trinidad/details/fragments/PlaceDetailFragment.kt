package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.fragments

import android.Manifest
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
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.FragmentPlaceDetailsBinding
import com.inmersoft.trinidadpatrimonial.extensions.loadImageCenterCropExt
import com.inmersoft.trinidadpatrimonial.extensions.loadPano360WithGlideExt
import com.inmersoft.trinidadpatrimonial.extensions.smartTruncate
import com.inmersoft.trinidadpatrimonial.utils.RomanNumbers
import com.inmersoft.trinidadpatrimonial.utils.ShareIntent
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.TrinidadCustomChromeTab
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

    private var canSpeak = true

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
                    canSpeak = false
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
        setupUI()
        initPlayer()
        return binding.root
    }

    private fun setupUI() {


        binding.placeName.text = placeData.place_name

        binding.btnGoToMap.apply {
            transitionName = UUID.randomUUID().toString()
            setOnClickListener {
                goToMap(placeData.place_id)
            }
        }

        //Set fade Animaton text changed
        binding.placeDescription.setInAnimation(context, android.R.anim.fade_in);
        binding.placeDescription.setOutAnimation(context, android.R.anim.fade_out);

        val description = placeData.place_description
        val shortDescription = description.smartTruncate(MAX_SMART_TRUNCATE_STRINGS)

        binding.placeDescription.setText(shortDescription)

        binding.seeMoreButtonToogle.visibility =
            if (placeData.place_description.length > MAX_SMART_TRUNCATE_STRINGS) View.VISIBLE else View.GONE

        binding.seeMoreButtonToogle.addOnButtonCheckedListener { _, _, isChecked ->
            binding.placeDescription.setText(if (isChecked) {
                binding.seeMoreButton.text = getString(R.string.see_less)
                description
            } else {
                binding.seeMoreButton.text = getString(R.string.see_more)
                shortDescription
            }
            )
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
                videoMeta: VideoMeta?,
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
        textToSpeechEngine.stop()
        super.onStop()

    }

    override fun onResume() {
        textToSpeechEngine.stop()
        super.onResume()
        if (simpleExoPlayer == null) {
            initPlayer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textToSpeechEngine.stop()
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
        binding.headerImage.loadImageCenterCropExt(Uri.parse(
            TrinidadAssets.getAssetFullPath(
                headerImages[0],
                TrinidadAssets.FILE_JPG_EXTENSION
            )
        ))
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
            val descriptionToSpeech = RomanNumbers.replaceRomanNumber(placeDescription)
            textToSpeechEngine.speak(descriptionToSpeech, TextToSpeech.QUEUE_FLUSH, null, "tts1")
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
        textToSpeechEngine.stop()
        binding.btnSpeechDescription.isChecked = false
        super.onDestroy()
    }

    private fun loadPano360(panoAssetName: List<String>) {
        val panoAssetUrl = TrinidadAssets.getAssetFullPath(
            panoAssetName[0],
            TrinidadAssets.FILE_WEBP_EXTENSION
        )

        binding.placePanoView.loadPano360WithGlideExt(Uri.parse(panoAssetUrl),
            container = binding.materialPanoContainer)

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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    companion object {
        const val WRITE_EXTERNAL_PERMISSION_CODE = 5637
        const val MAX_SMART_TRUNCATE_STRINGS = 256
    }

}
