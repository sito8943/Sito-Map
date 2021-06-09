package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding


class MainPlaceViewHolder(
    private val imageLoader: ImageLoader,
    val binding: ItemMainPlacesSubsectionsBinding
) :
    RecyclerView.ViewHolder(binding.root) {


    // Funcion para unir lso datos con la UI
    fun bindData(place: Place) {
        binding.tvCardHeaderTitle.text = place.place_name
        binding.tvCardSubtitle.text = place.place_description
        imageLoader.loadImage(
            place.header_images[0],
            binding.imCardHeader,
            R.drawable.placeholder_error,
            R.drawable.placeholder_error
        )
        itemView.setOnClickListener {
            val args = bundleOf("placeId" to place.place_id)
            Navigation.findNavController(itemView)
                .navigate(R.id.action_nav_map_to_viewPagerDetailFragment, args)
        }
    }
}