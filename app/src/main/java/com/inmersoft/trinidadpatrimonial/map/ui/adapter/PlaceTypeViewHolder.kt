package com.inmersoft.trinidadpatrimonial.map.ui.adapter

import android.net.Uri
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.MapItemPlaceTypeBinding

class PlaceTypeViewHolder(
    private val binding: MapItemPlaceTypeBinding

) :
    RecyclerView.ViewHolder(binding.root) {
    private val placeTypeImage: ImageView = binding.placeTypeImage

    fun bindData(itemPlaceType: PlaceTypeWithPlaces) {
        binding.placeType = itemPlaceType.placeType
        Glide.with(binding.root.context)
            .load(
                Uri.parse(itemPlaceType.placeType.icon)
            )
            .error(R.drawable.placeholder_error)
            .placeholder(R.drawable.placeholder_error)
            .into(placeTypeImage)
    }

}