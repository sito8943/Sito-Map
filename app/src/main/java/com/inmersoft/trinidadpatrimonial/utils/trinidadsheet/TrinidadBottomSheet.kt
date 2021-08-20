package com.inmersoft.trinidadpatrimonial.utils.trinidadsheet

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.BottomSheetTrinidadBinding
import com.inmersoft.trinidadpatrimonial.extensions.loadImageCenterCropExt
import com.inmersoft.trinidadpatrimonial.extensions.loadImageCenterCropWithTransitionExt
import com.inmersoft.trinidadpatrimonial.utils.ShareIntent
import com.inmersoft.trinidadpatrimonial.utils.TrinidadCustomChromeTab
import java.util.*

class TrinidadBottomSheet(
    private val context: Context, started: Boolean,
    private val rootLayout: ViewGroup,
    private val navController: NavController
) {
    private var binding: BottomSheetTrinidadBinding

    private var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    private var inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    init {
        binding = BottomSheetTrinidadBinding.inflate(inflater, rootLayout, true)
        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        if (!started) hide() else show()
    }

    fun addTrindadBottomSheetCallBack(callback: BottomSheetBehavior.BottomSheetCallback) {
        bottomSheet.addBottomSheetCallback(callback)
    }

    fun bindData(data: SheetData) {

        binding.bottomSheetImageHeader.loadImageCenterCropWithTransitionExt(data.imageURI)

        binding.bottomSheetHeaderTitle.text = data.headerTitle
        binding.bottomSheetHeaderTitle.isSelected = true
        binding.bottomSheetMiniDescription.text = data.miniDescription

        binding.bottomSheetShare.setOnClickListener {

            ShareIntent.loadImageAndShare(
                context,
                data.imageURI,
                data.headerTitle,
                context.getString(R.string.app_name)
            )
        }
        binding.bottomSheetWebpage.setOnClickListener {
            TrinidadCustomChromeTab.launch(context, data.webUrl)
        }
        Log.d("TAGXHD", "bindData: CALLED BINDING...")

    }

    fun navigateTo(
        destination: NavDirections
    ) {
        binding.bottomSheetHeaderCardview.transitionName = UUID.randomUUID().toString()
        val extras =
            FragmentNavigatorExtras(
                binding.bottomSheetHeaderCardview to "shared_view_container"
            )

        binding.seeMoreBottomSheetButton.setOnClickListener {
            navController.navigate(destination, extras)
            hide()
        }

        binding.bottomSheetHeaderCardview.setOnClickListener {
            navController.navigate(destination, extras)
            hide()
        }

    }

    fun show() {
        Log.d("TAG", "BottomShretshow: Showing bottom sheet")
        if (bottomSheet.state != BottomSheetBehavior.STATE_EXPANDED)
            bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hide() {
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    companion object {
        var currentDataId: Int = -1
    }


}