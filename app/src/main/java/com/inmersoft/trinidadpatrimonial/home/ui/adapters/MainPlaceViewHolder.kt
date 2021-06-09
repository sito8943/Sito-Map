package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding

class MainPlaceViewHolder(
    val binding: ItemMainPlacesSubsectionsBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    // Funcion para unir lso datos con la UI
    fun bindData(place: Place) {
        binding.textView3.text = place.place_name
        binding.textView4.text = place.place_description
        itemView.setOnClickListener {
            val args = bundleOf("placeId" to place.place_id)
            Navigation.findNavController(itemView)
                .navigate(R.id.action_nav_home_to_detailsPlaceFragment, args)
        }
    }
}