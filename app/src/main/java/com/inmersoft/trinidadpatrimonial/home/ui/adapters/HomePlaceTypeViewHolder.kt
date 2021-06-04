package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class HomePlaceTypeViewHolder(private val binding: MainPlacesSectionsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val innerRecyclerView: RecyclerView = binding.mainInnerRecycleview

    fun bindData(placeType: PlaceType) {
        binding.textView2.text = placeType.type
    }
}