package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class HomePlaceTypeAdapter(private val mainPlaceAdapter: MainPlaceAdapter) :
    RecyclerView.Adapter<HomePlaceTypeAdapter.ViewHolder>() {

    private val mainSectionData = mutableListOf<PlaceTypeWithPlaces>()

    inner class ViewHolder(private val binding: MainPlacesSectionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mainPlacesRecycleview.adapter = mainPlaceAdapter
        }

        fun bindData(placeType: PlaceTypeWithPlaces) {
            binding.tvPlaceTypeTitle.text = placeType.placeType.type
            subSectionsAdapter.setDataList(placeType.placesList)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainPlacesSectionsBinding.inflate(inflater, parent, false)

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