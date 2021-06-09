package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.inmersoft.trinidadpatrimonial.databinding.ItemMainPlacesSubsectionsBinding
import javax.inject.Inject

class MainPlaceAdapter(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<MainPlaceViewHolder>() {

    private val subSectionsData = mutableListOf<Place>()

    fun setDataList(newDataList: List<Place>) {
        subSectionsData.clear()
        subSectionsData.addAll(newDataList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainPlaceViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainPlacesSubsectionsBinding.inflate(inflater, parent, false)

        return MainPlaceViewHolder(
            imageLoader,
            binding
        )
    }

    override fun onBindViewHolder(holder: MainPlaceViewHolder, position: Int) {
        holder.bindData(subSectionsData[position])
    }

    override fun getItemCount(): Int {
        return subSectionsData.size
    }
}