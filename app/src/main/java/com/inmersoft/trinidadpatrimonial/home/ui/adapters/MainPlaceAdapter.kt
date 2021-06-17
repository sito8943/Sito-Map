package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding
import com.inmersoft.trinidadpatrimonial.home.ui.fragments.HomeFragmentDirections
import com.inmersoft.trinidadpatrimonial.utils.ASSETS_FOLDER

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
        fun bindData(place: Place) {
            binding.tvCardHeaderTitle.text = place.place_name
            binding.tvCardSubtitle.text = place.place_description
            binding.imCardHeader.transitionName = place.place_name

            Glide.with(binding.root.context)
                .load(Uri.parse("$ASSETS_FOLDER/${place.header_images[0]}.jpg"))
                .error(R.drawable.placeholder_error)
                .placeholder(R.drawable.placeholder_error)
                .into(binding.imCardHeader)



            itemView.setOnClickListener {
                val extras =
                    FragmentNavigatorExtras(
                          binding.imCardHeader to "shared_place_image_header",
                        binding.tvCardHeaderTitle to "shared_place_title",
                        binding.tvCardSubtitle to "shared_place_subtitle"
                    )
                val action =
                    HomeFragmentDirections.actionNavHomeToDetailsFragment(placeID = place.place_id)
                Navigation.findNavController(itemView)
                    .navigate(action, extras)
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