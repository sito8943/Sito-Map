package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class HomePlaceTypeViewHolder(
    private val binding: MainPlacesSectionsBinding,
    private val imageLoader: ImageLoader

) :
    RecyclerView.ViewHolder(binding.root) {
    private val subSectionsAdapter by lazy { MainPlaceAdapter(imageLoader) }

    init {
        binding.mainPlacesRecycleview.adapter = subSectionsAdapter
    }

    fun bindData(placeType: PlaceTypeWithPlaces) {
        binding.tvPlaceTypeTitle.text = placeType.placeType.type
        subSectionsAdapter.setDataList(placeType.placesList)
    }
}