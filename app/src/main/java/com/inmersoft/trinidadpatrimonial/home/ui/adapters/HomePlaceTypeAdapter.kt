package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.MainPlacesSectionsBinding
import com.inmersoft.trinidadpatrimonial.home.ui.utils.PlaceTypeFilter

class HomePlaceTypeAdapter(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<HomePlaceTypeViewHolder>() {

    private val mainSectionData = mutableListOf<PlaceTypeWithPlaces>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePlaceTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainPlacesSectionsBinding.inflate(inflater, parent, false)

        return HomePlaceTypeViewHolder(
            binding, imageLoader
        )
    }

    fun setData(mainDataList: List<PlaceTypeWithPlaces>) {
        mainSectionData.clear()
        mainSectionData.addAll(PlaceTypeFilter.filterNotEmptyPlaces(mainDataList))
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HomePlaceTypeViewHolder, position: Int) {
        holder.bindData(mainSectionData[position])
    }

    override fun getItemCount(): Int {
        return mainSectionData.size
    }


}