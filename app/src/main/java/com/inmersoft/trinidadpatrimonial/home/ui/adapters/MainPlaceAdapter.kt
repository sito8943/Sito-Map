package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding

class MainPlaceAdapter() :
    RecyclerView.Adapter<MainPlaceAdapter.ViewHolder>() {

    private val subSectionsData = mutableListOf<Place>()

    fun setListPlaceData(newDataList: List<Place>) {
        subSectionsData.clear()
        subSectionsData.addAll(newDataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainPlacesSubsectionsBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding
        )
    }


    inner class ViewHolder(
        val binding: ItemMainPlacesSubsectionsBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        // Funcion para unir lso datos con la UI
        fun bindData(place: Place) {
            binding.tvCardHeaderTitle.text = place.place_name
            binding.tvCardSubtitle.text = place.place_description

            Glide.with(binding.root.context)
                .load(Uri.parse("file:///android_asset/${place.header_images[0]}.jpg"))
                .error(R.drawable.placeholder_error)
                .placeholder(R.drawable.placeholder_error)
                .into(binding.imCardHeader)

            itemView.setOnClickListener {
                val args = bundleOf("placeId" to place.place_id)
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_nav_map_to_viewPagerDetailFragment, args)
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(subSectionsData[position])
    }

    override fun getItemCount(): Int {
        return subSectionsData.size
    }
}