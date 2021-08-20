package com.inmersoft.trinidadpatrimonial.ui.trinidad.map.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.database.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.MapItemPlaceTypeBinding
import com.inmersoft.trinidadpatrimonial.extensions.loadImageCenterInsideExt
import com.inmersoft.trinidadpatrimonial.utils.PlaceTypeFilter
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets

class MapPlaceTypeAdapter(private val event: MapPlaceTypeAdapter.Event) :
    RecyclerView.Adapter<MapPlaceTypeAdapter.PlaceTypeViewHolder>() {

    interface Event {
        fun onClickListener(placeId: Int)
    }

    private var placeTypeList = mutableListOf<PlaceTypeWithPlaces>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
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

    inner class PlaceTypeViewHolder(
        private val binding: MapItemPlaceTypeBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val placeTypeImage: ImageView = binding.placeTypeImage

        fun bindData(itemPlaceType: PlaceTypeWithPlaces) {
            binding.placeType = itemPlaceType.placeType
            val icon = Uri.parse(
                TrinidadAssets.getAssetFullPath(
                    itemPlaceType.placeType.icon,
                    TrinidadAssets.FILE_PNG_EXTENSION
                )
            )

            binding.materialCardviewContainer.setOnClickListener {
                event.onClickListener(placeId = itemPlaceType.placeType.place_type_id)
            }

            placeTypeImage.loadImageCenterInsideExt(icon)

        }

    }

}
