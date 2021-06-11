package com.inmersoft.trinidadpatrimonial.map.ui.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.MapItemPlaceTypeBinding

class PlaceTypeViewHolder(
    private val binding: MapItemPlaceTypeBinding,
    private val imageLoader: ImageLoader
) :
    RecyclerView.ViewHolder(binding.root) {
    private val placeTypeImage: ImageView = binding.placeTypeImage

    fun bindData(itemPlaceType: PlaceTypeWithPlaces) {
        binding.placeType = itemPlaceType.placeType
        imageLoader.loadImage(
            itemPlaceType.placeType.icon,
            placeTypeImage,
            R.drawable.placeholder_error,
            R.drawable.placeholder_error
        )
    }

}