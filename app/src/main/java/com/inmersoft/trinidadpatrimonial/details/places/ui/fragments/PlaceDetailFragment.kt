package com.inmersoft.trinidadpatrimonial.details.places.ui.fragments

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.media2.exoplayer.external.ExoPlayerFactory
import androidx.media2.exoplayer.external.source.ExtractorMediaSource
import androidx.media2.exoplayer.external.upstream.DefaultHttpDataSourceFactory
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.PlaceDetailsFragmentBinding
import com.inmersoft.trinidadpatrimonial.details.DetailsFragmentDirections
import com.inmersoft.trinidadpatrimonial.utils.ShareIntent
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.showToast
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.util.*


class PlaceDetailFragment(private val placeData: Place) : Fragment(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var binding: PlaceDetailsFragmentBinding
    private var currentLocale = Locale("es", "ES")
    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(requireActivity()) { status ->
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
        binding = PlaceDetailsFragmentBinding.inflate(layoutInflater, container, false)
        binding.toolbarLayout.title = placeData.place_name
        loadPano360(placeData.pano)
        loadHeader(placeData.header_images)

        binding.placeDescription.text = placeData.place_description

        binding.btnGoToMap.apply {
            transitionName = UUID.randomUUID().toString()
            setOnClickListener {
                goToMap(placeData.place_id)
            }

        }
        binding.btnSpeechDescription.setOnClickListener {
            if (!textToSpeechEngine.isSpeaking) {
                speechPlaceDescription(placeData.place_description)
                binding.btnSpeechDescription.icon =
                    resources.getDrawable(R.drawable.ic_baseline_hearing_disabled_24)
            } else {
                textToSpeechEngine.stop()
                binding.btnSpeechDescription.icon =
                    resources.getDrawable(R.drawable.ic_baseline_hearing_24)
            }
        }

        binding.btnGoWebPage.transitionName = "button_link_${placeData.place_id}"
        binding.btnGoWebPage.setOnClickListener {

            goToWebPage(placeData.web)
        }

        binding.btnSharePlaceInformation.transitionName = "button_share_${placeData.place_id}"
        binding.btnSharePlaceInformation.setOnClickListener {
            sharePlaceInformation()
        }



        return binding.root
    }

    private fun loadHeader(headerImages: List<String>) {

        Glide.with(requireContext())
            .load(
                Uri.parse(TrinidadAssets.getAssetFullPath(headerImages[0]))
            )
            .error(R.drawable.placeholder_error)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.headerImage)
    }

    private fun goToWebPage(web: String) {
        showToast(requireContext(), "NOT IMPLEMENTED YET")
    }

    private fun sharePlaceInformation() {
        if (!hasWriteExternalPermission()) {
            requestWriteExternalPermission()
        } else {
            sharePlaceInfo()
        }
    }

    private fun sharePlaceInfo() {
        Glide.with(requireContext())
            .asBitmap()
            .load(
                Uri.parse(TrinidadAssets.getAssetFullPath(placeData.header_images[0]))
            ).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    ShareIntent.shareIt(
                        requireActivity(),
                        resource,
                        placeData.place_name,
                        resources.getString(R.string.app_name)
                    )
                    Log.d("TAG", "onResourceReady: IMAGE LOADED")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Add other icon
                }
            })

    }

    private fun speechPlaceDescription(placeDescription: String) {
        if (placeDescription.isNotEmpty()) {
            textToSpeechEngine.speak(placeDescription, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        }
    }


    private fun goToMap(placeId: Int) {
        val action =
            DetailsFragmentDirections.actionDetailsFragmentToNavMap(placeID = placeId)
        findNavController().navigate(action)
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
                    " a su informacón personal, sino a la informacion contenida " +
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

            Toast.makeText(
                requireContext(),
                "La aplicación Trinidad Patrimonial esta lista para " +
                        "compartir su contenido con otras aplicaciones.",
                Toast.LENGTH_LONG
            )
                .show()

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

    override fun onPause() {
        textToSpeechEngine.stop()
        binding.btnSpeechDescription.isChecked = false
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeechEngine.shutdown()
        binding.btnSpeechDescription.isChecked = false
        super.onDestroy()
    }


    private fun loadPano360(panoAssetName: List<String>) {
        if (panoAssetName[0].isNotEmpty()) {
            val panoAssetUrl = TrinidadAssets.getPanoAssetFullPath(panoAssetName[0])
            Glide.with(requireActivity())
                .asBitmap()
                .placeholder(R.drawable.ic_placeholder)
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


    companion object {
        const val WRITE_EXTERNAL_PERMISSION_CODE = 5637
    }

}
