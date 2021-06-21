package com.inmersoft.trinidadpatrimonial.details.places.ui.fragments

import android.Manifest
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.PlaceDetailsFragmentBinding
import com.inmersoft.trinidadpatrimonial.utils.ASSETS_FOLDER
import com.inmersoft.trinidadpatrimonial.utils.ShareIntent
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class PlaceDetailFragment(private val placeData: Place) : Fragment(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var binding: PlaceDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = PlaceDetailsFragmentBinding.inflate(layoutInflater, container, false)
        binding.placeName.text = placeData.place_name
        binding.placeDescription.text = placeData.place_description

        binding.btnGoToMap.setOnClickListener {
            goToMap(placeData.place_id)
        }
        binding.btnSpeechDescription.setOnClickListener {
            speechPlaceDescription(placeData.place_description)
        }
        binding.btnGoWebPage.setOnClickListener {
            goToWebPage(placeData.web)
        }
        binding.btnSharePlaceInformation.setOnClickListener {
            sharePlaceInformation(placeData)
        }

        Glide.with(requireContext())
            .load(
                Uri.parse("$ASSETS_FOLDER/${placeData.header_images[0]}.jpg")
            )
            .error(R.drawable.placeholder_error)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.placeHeader)

        return binding.root
    }

    private fun goToWebPage(web: String) {

    }

    private fun sharePlaceInformation(placeData: Place) {
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
            )
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    ShareIntent.ShareIt(
                        requireActivity(),
                        resource,
                        placeData.place_name,
                        "Trinidad App"
                    )
                    Log.d("TAG", "onResourceReady: IMAGE LOADED")
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Add other icon
                }
            })

    }

    private fun speechPlaceDescription(placeDescription: String) {

    }

    private fun goToMap(placeId: Int) {

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

    companion object {
        const val WRITE_EXTERNAL_PERMISSION_CODE = 5637
    }

}
