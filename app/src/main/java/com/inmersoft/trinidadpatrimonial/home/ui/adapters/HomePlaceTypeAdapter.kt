package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding

class HomePlaceTypeAdapter : RecyclerView.Adapter<HomePlaceTypeViewHolder>() {

    private val mainSectionData = mutableListOf<PlaceTypeWithPlaces>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePlaceTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainPlacesSectionsBinding.inflate(inflater, parent, false)

        return HomePlaceTypeViewHolder(
            binding
        )
    }

    fun setData(mainDataList: List<PlaceTypeWithPlaces>) {
        mainSectionData.clear()
        val listItemsNotEmpty =
            mainDataList.filter { placeTypeWithPlaces -> placeTypeWithPlaces.placesList.isNotEmpty() }
        mainSectionData.addAll(listItemsNotEmpty)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HomePlaceTypeViewHolder, position: Int) {
        holder.bindData(mainSectionData[position])
    }

    override fun getItemCount(): Int {
        return mainSectionData.size
    }


}