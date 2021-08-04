package com.inmersoft.trinidadpatrimonial.ui.trinidad.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.inmersoft.trinidadpatrimonial.database.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.ItemHomePlacesBinding

class HomeListAdapter(val itemOnClick: MainPlaceAdapter.ItemOnCLick) :
    ListAdapter<PlaceTypeWithPlaces, HomeListAdapter.ViewHolder>(HomeDiffUtil()) {

    inner class ViewHolder(private val binding: ItemHomePlacesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mainPlaceAdapter by lazy { MainPlaceAdapter(itemOnClick) }

        init {
            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(binding.mainPlacesRecycleview)

            binding.mainPlacesRecycleview.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.mainPlacesRecycleview.adapter = mainPlaceAdapter
        }

        fun bindData(placeType: PlaceTypeWithPlaces) {
            binding.tvPlaceTypeTitle.text = placeType.placeType.type
            mainPlaceAdapter.submitList(placeType.placesList)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHomePlacesBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    class HomeDiffUtil : DiffUtil.ItemCallback<PlaceTypeWithPlaces>() {
        override fun areItemsTheSame(
            oldItem: PlaceTypeWithPlaces,
            newItem: PlaceTypeWithPlaces,
        ): Boolean {
            return oldItem.placeType.place_type_id == newItem.placeType.place_type_id
        }

        override fun areContentsTheSame(
            oldItem: PlaceTypeWithPlaces,
            newItem: PlaceTypeWithPlaces,
        ): Boolean {
            return oldItem.placeType.place_type_id == newItem.placeType.place_type_id &&
                    oldItem.placesList.size == newItem.placesList.size
        }

    }
}
