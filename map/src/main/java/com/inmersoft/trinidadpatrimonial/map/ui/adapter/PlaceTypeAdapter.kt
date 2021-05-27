package com.inmersoft.trinidadpatrimonial.map.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.map.R

class PlaceTypeAdapter(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<PlaceTypeAdapter.ViewHolder>() {

    private var placeTypeList = mutableListOf<PlaceType>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place_type, parent, false)
        )
    }

    fun setData(newPlaceTypeList: List<PlaceType>) {
        placeTypeList.clear()
        placeTypeList.addAll(newPlaceTypeList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(placeTypeList[position])
    }

    override fun getItemCount(): Int {
        return placeTypeList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeTypeImage: ImageView = itemView.findViewById(R.id.place_type_image)
        private val placeTypeName: TextView = itemView.findViewById(R.id.place_type_text)

        fun bindData(itemPlaceType: PlaceType) {
            placeTypeName.text = itemPlaceType.name
            imageLoader.loadImage(
                itemPlaceType.imgUrl,
                placeTypeImage,
                R.drawable.place_holder_error,
                R.drawable.place_holder_error
            )
        }

    }

}
