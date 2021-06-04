package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding

class MainPlaceViewHolder(val binding: ItemMainPlacesSubsectionsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Funcion para unir lso datos con la UI
    fun bindData(place: Place) {
        binding.textView3.text = place.place_name
        binding.textView4.text = place.place_description
    }
}