package com.inmersoft.trinidadpatrimonial.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.google.android.material.transition.MaterialContainerTransform
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.ui.trinidad.TrinidadActivity
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.SheetData
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    protected val trinidadDataViewModel: TrinidadDataViewModel by activityViewModels()

    protected lateinit var trinidadBottomSheet: TrinidadBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedTransitionEffect = MaterialContainerTransform()
        sharedTransitionEffect.fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
        sharedElementEnterTransition = sharedTransitionEffect
    }

    protected fun showTrinidadBottomSheetPlaceInfo(place: Place, navDirections: NavDirections) {
        val uriImage = Uri.parse(
            TrinidadAssets.getAssetFullPath(
                place.header_images[0],
                TrinidadAssets.FILE_JPG_EXTENSION
            )
        )
        val webURI = Uri.parse(place.web)
        val data =
            SheetData(
                place.place_id,
                uriImage,
                place.place_name,
                place.place_description,
                webURI
            )

        trinidadBottomSheet.navigateTo(
            navDirections
        )
        trinidadBottomSheet.bindData(data)
        trinidadBottomSheet.show()
    }

    protected fun openDrawerInTrinidadActivity(view: View) {
        (activity as TrinidadActivity).openCloseNavigationDrawer(view)
    }
}