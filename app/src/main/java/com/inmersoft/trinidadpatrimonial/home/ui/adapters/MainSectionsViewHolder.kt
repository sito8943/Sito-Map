package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class MainSectionsViewHolder(private val binding: MainPlacesSectionsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val innerRecyclerView: RecyclerView = binding.mainInnerRecycleview

    // Funcion para unir lso datos con la UI
    fun bindData(place: Place) {
        binding.textView2.text = place.place_name

    }
}