package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding

class MainPlaceAdapter(private val imageLoader: ImageLoader) :
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
            imageLoader.loadImage(
                Uri.parse("file:///android_asset/${place.header_images[0]}.jpg").toString(),
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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(subSectionsData[position])
    }

    override fun getItemCount(): Int {
        return subSectionsData.size
    }
}