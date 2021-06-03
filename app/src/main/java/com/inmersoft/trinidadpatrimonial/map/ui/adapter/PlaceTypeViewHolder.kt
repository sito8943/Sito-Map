package com.inmersoft.trinidadpatrimonial.map.ui.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.ItemPlaceTypeBinding

class PlaceTypeViewHolder(
    private val binding: ItemPlaceTypeBinding,
    private val imageLoader: ImageLoader
) :
    RecyclerView.ViewHolder(binding.root) {
    private val placeTypeImage: ImageView = binding.placeTypeImage

    fun bindData(itemPlaceType: PlaceType) {
        binding.placeType = itemPlaceType
        imageLoader.loadImage(
            itemPlaceType.icon,
            placeTypeImage,
            R.drawable.placeholder_error,
            R.drawable.placeholder_error
        )
    }

}