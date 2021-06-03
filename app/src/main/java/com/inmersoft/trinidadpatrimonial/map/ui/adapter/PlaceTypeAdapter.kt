package com.inmersoft.trinidadpatrimonial.map.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.ItemPlaceTypeBinding

class PlaceTypeAdapter(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<PlaceTypeViewHolder>() {

    private var placeTypeList = mutableListOf<PlaceType>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaceTypeBinding.inflate(inflater, parent, false)

        return PlaceTypeViewHolder(
            binding, imageLoader
        )
    }

    fun setData(newPlaceTypeList: List<PlaceType>) {
        placeTypeList.clear()
        placeTypeList.addAll(newPlaceTypeList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PlaceTypeViewHolder, position: Int) {
        holder.bindData(placeTypeList[position])
    }

    override fun getItemCount(): Int {
        return placeTypeList.size
    }

}
