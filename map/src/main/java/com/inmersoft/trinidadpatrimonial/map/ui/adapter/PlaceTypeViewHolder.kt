package com.inmersoft.trinidadpatrimonial.map.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.map.R
import com.inmersoft.trinidadpatrimonial.map.databinding.ItemPlaceTypeBinding

class PlaceTypeViewHolder(
    private val binding: ItemPlaceTypeBinding,
    private val imageLoader: ImageLoader
) :
    RecyclerView.ViewHolder(binding.root) {
    private val placeTypeImage: ImageView = binding.placeTypeImage

    fun bindData(itemPlaceType: PlaceType) {
        binding.placeType = itemPlaceType
        imageLoader.loadImage(
            itemPlaceType.imgUrl,
            placeTypeImage,
            R.drawable.place_holder_error,
            R.drawable.place_holder_error
        )
    }

}