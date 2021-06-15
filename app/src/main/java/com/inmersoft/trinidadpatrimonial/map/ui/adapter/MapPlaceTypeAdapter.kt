package com.inmersoft.trinidadpatrimonial.map.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.MapItemPlaceTypeBinding
import com.inmersoft.trinidadpatrimonial.utils.PlaceTypeFilter

class MapPlaceTypeAdapter() :
    RecyclerView.Adapter<PlaceTypeViewHolder>() {

    private var placeTypeList = mutableListOf<PlaceTypeWithPlaces>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MapItemPlaceTypeBinding.inflate(inflater, parent, false)

        return PlaceTypeViewHolder(
            binding
        )
    }

    fun setData(newPlaceTypeList: List<PlaceTypeWithPlaces>) {
        placeTypeList.clear()
        placeTypeList.addAll(PlaceTypeFilter.filterNotEmptyPlaces(newPlaceTypeList))
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PlaceTypeViewHolder, position: Int) {
        holder.bindData(placeTypeList[position])
    }

    override fun getItemCount(): Int {
        return placeTypeList.size
    }

}
