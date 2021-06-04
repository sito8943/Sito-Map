package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class HomePlaceTypeViewHolder(private val binding: MainPlacesSectionsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val subSectionsAdapter = MainPlaceAdapter()

    init {
        binding.mainInnerRecycleview.adapter = subSectionsAdapter
    }

    fun bindData(placeType: PlaceTypeWithPlaces) {
        binding.textView2.text = placeType.placeType.type
        subSectionsAdapter.setDataList(placeType.placesList)
    }
}