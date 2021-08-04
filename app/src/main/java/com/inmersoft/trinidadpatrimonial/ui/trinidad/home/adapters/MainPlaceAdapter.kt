package com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding
import com.inmersoft.trinidadpatrimonial.extensions.loadImageCenterCropExt
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import java.util.*

class MainPlaceAdapter(val placeItemOnClick: PlaceItemOnClick) :
    ListAdapter<Place, MainPlaceAdapter.ViewHolder>(MainPlaceDiffUtil()) {

    interface PlaceItemOnClick {
        fun showPlaceDetails(placeId: Int, sharedTransitionView: View)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainPlacesSubsectionsBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding
        )
    }

    inner class ViewHolder(
        val binding: ItemMainPlacesSubsectionsBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(place: Place) {
            binding.tvCardHeaderTitle.text = place.place_name
            binding.tvCardSubtitle.text = place.place_description
            // generamos un uuid diferente para cada item
            binding.cardContainer.transitionName = UUID.randomUUID().toString()
            binding.imCardHeader.loadImageCenterCropExt(Uri.parse(
                TrinidadAssets.getAssetFullPath(
                    place.header_images[0],
                    TrinidadAssets.FILE_JPG_EXTENSION
                )
            ))

            itemView.setOnClickListener {
                placeItemOnClick.showPlaceDetails(place.place_id, binding.cardContainer)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }


    class MainPlaceDiffUtil : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.place_id == newItem.place_id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.place_id == newItem.place_id &&
                    oldItem.place_name == newItem.place_name
        }


    }
}
