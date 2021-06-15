package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.HomePlacesItemBinding

class HomeListAdapter() :
    RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    private val mainSectionData = mutableListOf<PlaceTypeWithPlaces>()

    inner class ViewHolder(private val binding: HomePlacesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mainPlaceAdapter by lazy { MainPlaceAdapter() }

        init {
            binding.mainPlacesRecycleview.adapter = mainPlaceAdapter
        }

        fun bindData(placeType: PlaceTypeWithPlaces) {
            binding.tvPlaceTypeTitle.text = placeType.placeType.type
            mainPlaceAdapter.setListPlaceData(placeType.placesList)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomePlacesItemBinding.inflate(inflater, parent, false)

        return ViewHolder(
            binding
        )
    }

    fun setData(mainDataList: List<PlaceTypeWithPlaces>) {
        mainSectionData.clear()
        mainSectionData.addAll(mainDataList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mainSectionData[position])
    }

    override fun getItemCount(): Int {
        return mainSectionData.size
    }

}