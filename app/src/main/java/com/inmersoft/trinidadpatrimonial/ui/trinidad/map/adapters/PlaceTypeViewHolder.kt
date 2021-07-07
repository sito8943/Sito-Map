package com.inmersoft.trinidadpatrimonial.ui.trinidad.map.adapters

import android.net.Uri
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.MapItemPlaceTypeBinding
import com.inmersoft.trinidadpatrimonial.extensions.loadImageCenterInsideExt
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets

class PlaceTypeViewHolder(
    private val binding: MapItemPlaceTypeBinding

) :
    RecyclerView.ViewHolder(binding.root) {
    private val placeTypeImage: ImageView = binding.placeTypeImage

    fun bindData(itemPlaceType: PlaceTypeWithPlaces) {
        binding.placeType = itemPlaceType.placeType
        val icon = Uri.parse(
            TrinidadAssets.getAssetFullPath(
                itemPlaceType.placeType.icon,
                TrinidadAssets.FILE_PNG_EXTENSION
            )
        )

        placeTypeImage.loadImageCenterInsideExt(icon)

    }

}