package com.inmersoft.trinidadpatrimonial.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.databinding.ItemHomePlacesBinding

class HomeListAdapter() :
    RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    private val mainSectionData = mutableListOf<PlaceTypeWithPlaces>()

    inner class ViewHolder(private val binding: ItemHomePlacesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val mainPlaceAdapter by lazy { MainPlaceAdapter() }

        init {
            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(binding.mainPlacesRecycleview)

            binding.mainPlacesRecycleview.layoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
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
        val binding = ItemHomePlacesBinding.inflate(inflater, parent, false)

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